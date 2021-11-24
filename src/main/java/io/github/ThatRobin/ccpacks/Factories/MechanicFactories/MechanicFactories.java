package io.github.ThatRobin.ccpacks.Factories.MechanicFactories;

import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.ThatRobin.ccpacks.DataDrivenClasses.Entities.goals.DDAttackGoal;
import io.github.ThatRobin.ccpacks.DataDrivenClasses.mechanics.*;
import io.github.ThatRobin.ccpacks.Registries.CCPacksRegistries;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.block.Block;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Triple;

import java.util.function.Consumer;

public class MechanicFactories {

    public static void register() {
        register(new MechanicFactory<>(CCPacksMain.identifier("tick"),
                new SerializableData()
                        .add("interval", SerializableDataTypes.INT, 1)
                        .add("block_action", ApoliDataTypes.BLOCK_ACTION, null)
                        .add("block_condition", ApoliDataTypes.BLOCK_CONDITION, null),
                data ->
                        (identifier, factory) -> new DDTickMechanic(identifier, factory, data.getInt("interval"), (ActionFactory<Triple<World, BlockPos, Direction>>.Instance)data.get("block_action"), (ConditionFactory<CachedBlockPosition>.Instance)data.get("block_condition")))
        );

        register(new MechanicFactory<>(CCPacksMain.identifier("on_use"),
                new SerializableData()
                        .add("block_action", ApoliDataTypes.BLOCK_ACTION, null)
                        .add("entity_action", ApoliDataTypes.ENTITY_ACTION, null)
                        .add("block_condition", ApoliDataTypes.BLOCK_CONDITION, null),
                data ->
                        (identifier, factory) -> new DDUseMechanic(identifier, factory, (ActionFactory<Entity>.Instance)data.get("entity_action"), (ActionFactory<Triple<World, BlockPos, Direction>>.Instance)data.get("block_action"), (ConditionFactory<CachedBlockPosition>.Instance)data.get("block_condition")))
        );

        register(new MechanicFactory<>(CCPacksMain.identifier("on_step"),
                new SerializableData()
                        .add("block_action", ApoliDataTypes.BLOCK_ACTION, null)
                        .add("entity_action", ApoliDataTypes.ENTITY_ACTION, null),
                data ->
                        (identifier, factory) -> new DDStepMechanic(identifier, factory, (ActionFactory<Entity>.Instance)data.get("entity_action"), (ActionFactory<Triple<World, BlockPos, Direction>>.Instance)data.get("block_action")))
        );

        register(new MechanicFactory<>(CCPacksMain.identifier("on_neighbour_update"),
                new SerializableData()
                        .add("block_action", ApoliDataTypes.BLOCK_ACTION, null)
                        .add("neighbour_action", ApoliDataTypes.BLOCK_ACTION, null)
                        .add("direction", SerializableDataType.enumValue(Direction.class), null)
                        .add("entity_action", ApoliDataTypes.ENTITY_ACTION, null),
                data ->
                        (identifier, factory) -> new DDNeighourUpdateMechanic(identifier, factory, (Direction)data.get("direction"), (ActionFactory<Triple<World, BlockPos, Direction>>.Instance)data.get("block_action"), (ActionFactory<Triple<World, BlockPos, Direction>>.Instance)data.get("neighbour_action")))
        );

        register(new MechanicFactory<>(CCPacksMain.identifier("on_land"),
                new SerializableData()
                        .add("damage_multiplier", SerializableDataTypes.FLOAT, 1.0f)
                        .add("block_action", ApoliDataTypes.BLOCK_ACTION, null)
                        .add("entity_action", ApoliDataTypes.ENTITY_ACTION, null),
                data ->
                        (identifier, factory) -> new DDFallMechanic(identifier, factory, data.getFloat("damage_multiplier"), (ActionFactory<Entity>.Instance)data.get("entity_action"), (ActionFactory<Triple<World, BlockPos, Direction>>.Instance)data.get("block_action")))
        );

        register(new MechanicFactory<>(CCPacksMain.identifier("connection"),
                new SerializableData()
                        .add("tag", SerializableDataTypes.BLOCK_TAG)
                        .add("block_condition", ApoliDataTypes.BLOCK_CONDITION, null),
                data ->
                        (identifier, factory) -> new DDConnectionMechanic(identifier, factory, (Tag<Block>) data.get("tag")))
        );
    }

    private static void register(MechanicFactory serializer) {
        Registry.register(CCPacksRegistries.MECHANIC_FACTORY, serializer.getSerializerId(), serializer);
    }

}
