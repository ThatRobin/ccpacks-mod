package io.github.ThatRobin.ccpacks.SerializableData;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketInventory;
import dev.emi.trinkets.api.TrinketsApi;
import io.github.ThatRobin.ccpacks.CustomElytraFlightPower;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.ElytraFlightPower;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.ThatRobin.ccpacks.Util.CustomCraftingTable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.*;
import net.minecraft.stat.Stats;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Triple;

public class CCPackFactory {

    public static void register() {
        registerPowerType(new PowerFactory<>(CCPacksMain.identifier("elytra_flight"),
                new SerializableData()
                        .add("render_elytra", SerializableDataTypes.BOOLEAN)
                        .add("sprite_location", SerializableDataTypes.IDENTIFIER),
                data ->
                        (type, player) -> new CustomElytraFlightPower(type, player, data.getBoolean("render_elytra"),((Identifier) data.get("sprite_location"))))
                .allowCondition());
        registerEntityAction(new ActionFactory<>(CCPacksMain.identifier("block_looking_action"), new SerializableData()
                .add("block_action", ApoliDataTypes.BLOCK_ACTION),
                (data, entity) -> {
                    ((ActionFactory<Triple<World, BlockPos, Direction>>.Instance)data.get("block_action")).accept(
                            Triple.of(entity.world, entity.getBlockPos(), Direction.UP));
                }));
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
        registerEntityCondition(new ConditionFactory<>(CCPacksMain.identifier("equipped_trinket"), new SerializableData()
                .add("item_condition", ApoliDataTypes.ITEM_CONDITION),
                (data, user) -> {
                    var optional = TrinketsApi.getTrinketComponent(user);
                    if (optional.isPresent()) {
                        TrinketComponent comp = optional.get();
                        for (var group : comp.getInventory().values()) {
                            for (TrinketInventory inv : group.values()) {
                                for (int i = 0; i < inv.size(); i++) {
                                    return ((ConditionFactory<ItemStack>.Instance)data.get("item_condition")).test(inv.getStack(i));
                                }
                            }
                        }
                    }
                    return false;
                }));

        registerItemCondition(new ConditionFactory<>(CCPacksMain.identifier("compare_amount"), new SerializableData()
                .add("comparison", ApoliDataTypes.COMPARISON)
                .add("compare_to", SerializableDataTypes.INT),
                (data, stack) -> {
                    int count = 0;
                    count = stack.getCount();
                    return ((Comparison)data.get("comparison")).compare(count, data.getInt("compare_to"));
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
