package io.github.ThatRobin.ccpacks.serializableData;

import com.google.gson.Gson;
import com.mojang.authlib.minecraft.client.ObjectMapper;
import io.github.ThatRobin.ccpacks.Power.ChoiceScreenPower;
import io.github.ThatRobin.ccpacks.Power.InterfacePower;
import io.github.ThatRobin.ccpacks.Power.StatBar;
import io.github.ThatRobin.ccpacks.Screen.ChoiceScreen;
import io.github.ThatRobin.ccpacks.networkin.ModPackets;
import io.github.ThatRobin.ccpacks.util.AdvancedHudRender;
import io.github.ThatRobin.ccpacks.util.Choice;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.*;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.ThatRobin.ccpacks.util.CustomCraftingTable;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Triple;

import java.io.ObjectOutputStream;
import java.util.List;

public class CCPackFactory {

    public static void register() {
        registerEntityAction(new ActionFactory<>(CCPacksMain.identifier("crafting_gui"), new SerializableData(),
                (data, entity) -> {
                    if (entity instanceof PlayerEntity) {
                        PlayerEntity player = (PlayerEntity)entity;

                        if (!player.world.isClient) {
                            player.openHandledScreen(craftingTable(player.world, player.getBlockPos()));
                            player.incrementStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
                        }
                    }
                }));
        registerItemAction(new ActionFactory<>(CCPacksMain.identifier("remove_durability"), new SerializableData()
                .add("amount", SerializableDataTypes.INT, 1),
                (data, stack) -> {
                    if(stack.getDamage() > -1){
                        stack.setDamage(stack.getDamage()+data.getInt("amount"));
                    }
                }));
        registerItemAction(new ActionFactory<>(CCPacksMain.identifier("change_name"), new SerializableData()
                .add("name", SerializableDataTypes.STRING),
                (data, stack) -> {
                    stack.setCustomName(new TranslatableText(data.getString("name")));
                }));
        registerItemCondition(new ConditionFactory<>(CCPacksMain.identifier("compare_durability"), new SerializableData()
                .add("comparison", ApoliDataTypes.COMPARISON)
                .add("compare_to", SerializableDataTypes.INT),
                (data, stack) -> {
                    int durability = 0;
                    durability = stack.getMaxDamage() - stack.getDamage();
                    return ((Comparison)data.get("comparison")).compare(durability, data.getInt("compare_to"));
                }));
        registerEntityAction(new ActionFactory<>(CCPacksMain.identifier("raycast_block"), new SerializableData()
                .add("distance", SerializableDataTypes.DOUBLE, 2.0)
                .add("entity_action", ApoliDataTypes.ENTITY_ACTION, null)
                .add("block_action", ApoliDataTypes.BLOCK_ACTION, null)
                .add("block_condition", ApoliDataTypes.BLOCK_CONDITION, null),
                (data, entity) -> {
                    ActionFactory<Triple<World, BlockPos, Direction>>.Instance blockAction = (ActionFactory<Triple<World, BlockPos, Direction>>.Instance)data.get("block_action");
                    ActionFactory<Entity>.Instance entityAction = (ActionFactory<Entity>.Instance)data.get("entity_action");
                    ConditionFactory<CachedBlockPosition>.Instance blockCondition = (ConditionFactory<CachedBlockPosition>.Instance)data.get("block_condition");


                    BlockHitResult blockHitResult = entity.world.raycast(new RaycastContext(entity.getCameraPosVec(0.0F), entity.getCameraPosVec(0.0F).add(entity.getRotationVec(0.0F).x * data.getDouble("distance"), entity.getRotationVec(0.0F).y * data.getDouble("distance"), entity.getRotationVec(0.0F).z * data.getDouble("distance")), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity));
                        if (blockHitResult.getType() == HitResult.Type.BLOCK && blockHitResult != null) {
                            if(blockCondition != null) {
                                if (blockCondition.test(new CachedBlockPosition(entity.world, blockHitResult.getBlockPos(), true))) {
                                    if (entityAction != null) {
                                        entityAction.accept(entity);
                                    }
                                    if (blockAction != null) {
                                        blockAction.accept(Triple.of(entity.world, blockHitResult.getBlockPos(), Direction.UP));
                                    }
                                }
                            }
                        }
                }));
        registerItemCondition(new ConditionFactory<>(CCPacksMain.identifier("compare_amount"), new SerializableData()
                .add("comparison", ApoliDataTypes.COMPARISON)
                .add("compare_to", SerializableDataTypes.INT),
                (data, stack) -> {
                    int count = 0;
                    count = stack.getCount();
                    return ((Comparison)data.get("comparison")).compare(count, data.getInt("compare_to"));
                }));
        registerPowerType(new PowerFactory<>(CCPacksMain.identifier("stat_bar"),
                new SerializableData()
                        .add("base_value", SerializableDataTypes.INT, 5)
                        .add("hud_render", CCPackDataTypes.HUD_RENDER),
                data ->
                        (type, player) -> {
                            StatBar power = new StatBar(type, player, (AdvancedHudRender) data.get("hud_render"));
                            return power;
                        })
                .allowCondition());
        registerEntityAction(new ActionFactory<>(CCPacksMain.identifier("change_stat"), new SerializableData()
                        .add("stat_bar", ApoliDataTypes.POWER_TYPE)
                        .add("change", SerializableDataTypes.INT),
                (data, entity) -> {
                    if(entity instanceof PlayerEntity) {
                        PowerHolderComponent component = PowerHolderComponent.KEY.get(entity);
                        Power p = component.getPower((PowerType<?>)data.get("stat_bar"));
                        if(p instanceof StatBar) {
                            StatBar statBar = (StatBar) p;
                            statBar.getHudRender().addValue(data.getInt("change"));
                            PowerHolderComponent.sync((PlayerEntity)entity);
                        }
                    }
                }));
        registerEntityAction(new ActionFactory<>(CCPacksMain.identifier("set_stat"), new SerializableData()
                .add("stat_bar", ApoliDataTypes.POWER_TYPE)
                .add("value", SerializableDataTypes.INT),
                (data, entity) -> {
                    if(entity instanceof PlayerEntity) {
                        PowerHolderComponent component = PowerHolderComponent.KEY.get(entity);
                        Power p = component.getPower((PowerType<?>)data.get("stat_bar"));
                        if(p instanceof StatBar) {
                            StatBar statBar = (StatBar) p;
                            int newValue = data.getInt("value");
                            statBar.getHudRender().setValue(newValue);
                            PowerHolderComponent.sync((PlayerEntity)entity);
                        }
                    }
                }));

        registerEntityCondition(new ConditionFactory<>(CCPacksMain.identifier("check_stat"), new SerializableData()
                .add("stat_bar", ApoliDataTypes.POWER_TYPE)
                .add("comparison", ApoliDataTypes.COMPARISON)
                .add("compare_to", SerializableDataTypes.INT),
                (data, entity) -> {
                    int resourceValue = 0;
                    PowerHolderComponent component = PowerHolderComponent.KEY.get(entity);
                    Power p = component.getPower((PowerType<?>)data.get("stat_bar"));
                    if(p instanceof StatBar) {
                        resourceValue = ((StatBar)p).getHudRender().getValue();
                    }
                    return ((Comparison)data.get("comparison")).compare(resourceValue, data.getInt("compare_to"));
                }));
        registerEntityAction(new ActionFactory<>(CCPacksMain.identifier("swing_hand"), new SerializableData()
                .add("hand", SerializableDataType.enumValue(Hand.class), Hand.MAIN_HAND),
                (data, entity) -> {
                    ((PlayerEntity)entity).swingHand((Hand) data.get("hand"));
                }
        ));

        registerEntityAction(new ActionFactory<>(CCPacksMain.identifier("open_interface"), new SerializableData()
                .add("interface_power", ApoliDataTypes.POWER_TYPE, null),
                (data, entity) -> {
                    PowerHolderComponent component = PowerHolderComponent.KEY.get(entity);
                    Power p = component.getPower((PowerType<?>)data.get("interface_power"));
                    if(p instanceof InterfacePower) {
                        ((PlayerEntity)entity).openHandledScreen(new SimpleNamedScreenHandlerFactory(((InterfacePower) p).factory,((InterfacePower) p).containerName));
                    }

                }
        ));

        registerPowerType(new PowerFactory<>(CCPacksMain.identifier("interface"),
                new SerializableData()
                        .add("interface_type", SerializableDataTypes.STRING, "minecraft:dropper")
                        .add("rows", SerializableDataTypes.INT, 1)
                        .add("name", SerializableDataTypes.STRING, "container.inventory")
                        .add("drop_on_death", SerializableDataTypes.BOOLEAN, false)
                        .add("drop_on_death_filter", ApoliDataTypes.ITEM_CONDITION, null),
                data ->
                        (type, player) -> {
                            InterfacePower power = new InterfacePower(type, player, data.getString("name"),data.getString("interface_type"), 9, data.getInt("rows"),
                                    data.getBoolean("drop_on_death"),
                                    data.isPresent("drop_on_death_filter") ? (ConditionFactory<ItemStack>.Instance) data.get("drop_on_death_filter") :
                                            itemStack -> true);
                            return power;
                        }));

        registerEntityAction(new ActionFactory<>(CCPacksMain.identifier("open_decision_screen"), new SerializableData()
                .add("decision_screen", ApoliDataTypes.POWER_TYPE, null),
                (data, entity) -> {
                    if(entity instanceof PlayerEntity){
                        PowerHolderComponent component = PowerHolderComponent.KEY.get(entity);
                        Power p = component.getPower((PowerType<?>)data.get("decision_screen"));
                        if(p instanceof ChoiceScreenPower) {
                            if (entity.getEntityWorld().isClient()) {
                                MinecraftClient.getInstance().openScreen(((ChoiceScreenPower) p).getScreen());
                            }
                            if (!entity.getEntityWorld().isClient()) {
                                PacketByteBuf data2 = new PacketByteBuf(Unpooled.buffer());
                                ChoiceScreen stream = (((ChoiceScreenPower) p).getScreen());
                                String str = new ObjectMapper(new Gson()).writeValueAsString(stream);
                                data2.writeString(str);
                                ServerSidePacketRegistry.INSTANCE.sendToPlayer((PlayerEntity) entity, ModPackets.OPEN_CHOICE_SCREEN, data2);
                            }
                        }
                    }
                }
        ));

        registerPowerType(new PowerFactory<>(CCPacksMain.identifier("decision_screen"),
                new SerializableData()
                        .add("title", SerializableDataTypes.STRING, "")
                        .add("show_dirt", SerializableDataTypes.BOOLEAN, false)
                        .add("choices",CCPackDataTypes.OUTCOMES),
                data ->
                        (type, player) -> {
                            ChoiceScreen screen = new ChoiceScreen(data.getBoolean("show_dirt"), null);
                            ChoiceScreenPower power = new ChoiceScreenPower(type, player, screen);
                            return power;
                        }));

    }

    private static NamedScreenHandlerFactory craftingTable(World world_1, BlockPos blockPos_1) {
        return new SimpleNamedScreenHandlerFactory(
                (int_1, playerInventory_1, playerEntity_1)
                        ->
                        new CustomCraftingTable(int_1, playerInventory_1,
                                ScreenHandlerContext.create(world_1, blockPos_1)),
                new TranslatableText("container.crafting", new Object[0])
        );
    }

    private static void registerItemCondition(ConditionFactory<ItemStack> conditionFactory) {
        Registry.register(ApoliRegistries.ITEM_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }

    private static void registerItemAction(ActionFactory<ItemStack> actionFactory) {
        Registry.register(ApoliRegistries.ITEM_ACTION, actionFactory.getSerializerId(), actionFactory);
    }

    private static void registerEntityAction(ActionFactory<Entity> actionFactory) {
        Registry.register(ApoliRegistries.ENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }

    private static void registerEntityCondition(ConditionFactory<LivingEntity> conditionFactory) {
        Registry.register(ApoliRegistries.ENTITY_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }

    private static void registerPowerType(PowerFactory serializer) {
        Registry.register(ApoliRegistries.POWER_FACTORY, serializer.getSerializerId(), serializer);
    }
}
