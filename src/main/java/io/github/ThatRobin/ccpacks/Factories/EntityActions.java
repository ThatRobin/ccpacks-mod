package io.github.ThatRobin.ccpacks.Factories;

import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.ThatRobin.ccpacks.Networking.CCPacksModPackets;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.VariableIntPower;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.apoli.util.ResourceOperation;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Triple;

public class EntityActions {

    public static void register() {
        register(new ActionFactory<>(CCPacksMain.identifier("open_choice_screen"), new SerializableData()
                .add("choice_layer", SerializableDataTypes.IDENTIFIER),
                (data, entity) -> {
                    if(!entity.getEntityWorld().isClient()) {
                        if (entity instanceof PlayerEntity player) {
                            PacketByteBuf data2 = new PacketByteBuf(Unpooled.buffer());
                            data2.writeBoolean(false);
                            data2.writeIdentifier(data.getId("choice_layer"));
                            ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, CCPacksModPackets.OPEN_CHOICE_SCREEN, data2);
                        }
                    }
                }
        ));

        register(new ActionFactory<>(CCPacksMain.identifier("swing_hand"), new SerializableData()
                .add("hand", SerializableDataType.enumValue(Hand.class), Hand.MAIN_HAND),
                (data, entity) -> ((PlayerEntity)entity).swingHand((Hand) data.get("hand"))
        ));

        register(new ActionFactory<>(CCPacksMain.identifier("change_stat"), new SerializableData()
                .add("stat_bar", ApoliDataTypes.POWER_TYPE)
                .add("change", SerializableDataTypes.INT)
                .add("operation", ApoliDataTypes.RESOURCE_OPERATION, ResourceOperation.ADD),
                (data, entity) -> {
                    if(entity instanceof LivingEntity) {
                        PowerHolderComponent component = PowerHolderComponent.KEY.get(entity);
                        Power p = component.getPower((PowerType<?>)data.get("stat_bar"));
                        ResourceOperation operation = (ResourceOperation) data.get("operation");
                        int change = data.getInt("change");
                        if(p instanceof VariableIntPower) {
                            VariableIntPower vip = (VariableIntPower)p;
                            if (operation == ResourceOperation.ADD) {
                                int newValue = vip.getValue() + change;
                                vip.setValue(newValue);
                            } else if (operation == ResourceOperation.SET) {
                                vip.setValue(change);
                            }
                            PowerHolderComponent.sync(entity);
                        }
                    }
                }));

        register(new ActionFactory<>(CCPacksMain.identifier("raycast_block"), new SerializableData()
                .add("distance", SerializableDataTypes.DOUBLE, 2.0)
                .add("entity_action", ApoliDataTypes.ENTITY_ACTION, null)
                .add("block_action", ApoliDataTypes.BLOCK_ACTION, null)
                .add("block_condition", ApoliDataTypes.BLOCK_CONDITION, null),
                (data, entity) -> {
                    ActionFactory<Triple<World, BlockPos, Direction>>.Instance blockAction = (ActionFactory<Triple<World, BlockPos, Direction>>.Instance)data.get("block_action");
                    ActionFactory<Entity>.Instance entityAction = (ActionFactory<Entity>.Instance)data.get("entity_action");
                    ConditionFactory<CachedBlockPosition>.Instance blockCondition = (ConditionFactory<CachedBlockPosition>.Instance)data.get("block_condition");


                    BlockHitResult blockHitResult = entity.world.raycast(new RaycastContext(entity.getCameraPosVec(0.0F), entity.getCameraPosVec(0.0F).add(entity.getRotationVec(0.0F).x * data.getDouble("distance"), entity.getRotationVec(0.0F).y * data.getDouble("distance"), entity.getRotationVec(0.0F).z * data.getDouble("distance")), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity));
                    if (blockHitResult.getType() == HitResult.Type.BLOCK) {
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
                })
        );
    }

    private static void register(ActionFactory<Entity> actionFactory) {
        Registry.register(ApoliRegistries.ENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }
}
