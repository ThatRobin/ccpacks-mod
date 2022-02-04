package io.github.thatrobin.ccpacks.factories.mechanic_factories;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.thatrobin.ccpacks.CCPacksMain;
import io.github.thatrobin.ccpacks.data_driven_classes.mechanics.*;
import io.github.thatrobin.ccpacks.registries.CCPacksRegistries;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;

public class MechanicFactories {

    public static void register() {
        register(new MechanicFactory<>(CCPacksMain.identifier("tick"),
                new SerializableData()
                        .add("interval", SerializableDataTypes.INT, 1)
                        .add("block_action", ApoliDataTypes.BLOCK_ACTION, null)
                        .add("block_condition", ApoliDataTypes.BLOCK_CONDITION, null),
                data ->
                        (identifier, factory) -> new DDTickMechanic(identifier, factory, data.getInt("interval"), data.get("block_action"), data.get("block_condition")))
        );

        register(new MechanicFactory<>(CCPacksMain.identifier("resource"),
                new SerializableData()
                        .add("min", SerializableDataTypes.INT, 0)
                        .add("max", SerializableDataTypes.INT, 1)
                        .add("start_value", SerializableDataTypes.INT, 0),
                data ->
                        (identifier, factory) -> new DDResourceMechanic(identifier, factory, data.getInt("start_value"), data.getInt("min"), data.getInt("max")))
        );

        register(new MechanicFactory<>(CCPacksMain.identifier("on_use"),
                new SerializableData()
                        .add("block_action", ApoliDataTypes.BLOCK_ACTION, null)
                        .add("entity_action", ApoliDataTypes.ENTITY_ACTION, null)
                        .add("block_condition", ApoliDataTypes.BLOCK_CONDITION, null),
                data ->
                        (identifier, factory) -> new DDUseMechanic(identifier, factory, data.get("entity_action"), data.get("block_action"), data.get("block_condition")))
        );

        register(new MechanicFactory<>(CCPacksMain.identifier("on_step"),
                new SerializableData()
                        .add("block_action", ApoliDataTypes.BLOCK_ACTION, null)
                        .add("entity_action", ApoliDataTypes.ENTITY_ACTION, null),
                data ->
                        (identifier, factory) -> new DDStepMechanic(identifier, factory, data.get("entity_action"), data.get("block_action")))
        );

        register(new MechanicFactory<>(CCPacksMain.identifier("on_neighbour_update"),
                new SerializableData()
                        .add("block_action", ApoliDataTypes.BLOCK_ACTION, null)
                        .add("neighbour_action", ApoliDataTypes.BLOCK_ACTION, null)
                        .add("direction", SerializableDataType.enumValue(Direction.class), null)
                        .add("entity_action", ApoliDataTypes.ENTITY_ACTION, null),
                data ->
                        (identifier, factory) -> new DDNeighourUpdateMechanic(identifier, factory, data.get("direction"), data.get("block_action"), data.get("neighbour_action")))
        );

        register(new MechanicFactory<>(CCPacksMain.identifier("on_land"),
                new SerializableData()
                        .add("damage_multiplier", SerializableDataTypes.FLOAT, 1.0f)
                        .add("block_action", ApoliDataTypes.BLOCK_ACTION, null)
                        .add("entity_action", ApoliDataTypes.ENTITY_ACTION, null),
                data ->
                        (identifier, factory) -> new DDFallMechanic(identifier, factory, data.getFloat("damage_multiplier"), data.get("entity_action"), data.get("block_action")))
        );

        register(new MechanicFactory<>(CCPacksMain.identifier("triggerable"),
                new SerializableData()
                        .add("self_action", ApoliDataTypes.BLOCK_ACTION, null)
                        .add("self_condition", ApoliDataTypes.BLOCK_CONDITION, null)
                        .add("neighbour_action", ApoliDataTypes.BLOCK_ACTION, null)
                        .add("neighbour_condition", ApoliDataTypes.BLOCK_CONDITION, null),
                data ->
                        (identifier, factory) -> new DDFindBlockMechanic(identifier, factory, data.get("self_action"), data.get("self_condition"), data.get("neighbour_action"), data.get("neighbour_condition")))
        );
    }

    private static void register(MechanicFactory<?> serializer) {
        Registry.register(CCPacksRegistries.MECHANIC_FACTORY, serializer.getSerializerId(), serializer);
    }

}
