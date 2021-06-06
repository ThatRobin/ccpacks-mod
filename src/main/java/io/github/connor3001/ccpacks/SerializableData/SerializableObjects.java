package io.github.connor3001.ccpacks.SerializableData;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Active;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.java.games.input.Component;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;

public class SerializableObjects {

    public static SerializableData getItemType = new SerializableData()
            .add("type",SerializableDataTypes.STRING);

    public static SerializableData itemData = new SerializableData()
            .add("type",SerializableDataTypes.STRING)
            .add("namespace", SerializableDataTypes.STRING)
            .add("id", SerializableDataTypes.STRING)
            .add("max_count", SerializableDataTypes.INT, 64)
            .add("consume", SerializableDataTypes.BOOLEAN, false)
            .add("action", ApoliDataTypes.ENTITY_ACTION, null)
            .add("condition", ApoliDataTypes.ENTITY_CONDITION, null);

    public static SerializableData toolData = new SerializableData()
            .add("type",SerializableDataTypes.STRING)
            .add("namespace", SerializableDataTypes.STRING)
            .add("id", SerializableDataTypes.STRING)
            .add("durability", SerializableDataTypes.INT, 10)
            .add("mining_speed_multiplier",SerializableDataTypes.FLOAT, 0f)
            .add("attack_speed",SerializableDataTypes.INT, 0)
            .add("mining_level",SerializableDataTypes.INT, 0)
            .add("enchantability",SerializableDataTypes.INT, 0)
            .add("attack_damage",SerializableDataTypes.INT, 0);

    public static SerializableData blockData = new SerializableData()
            .add("type",SerializableDataTypes.STRING)
            .add("namespace", SerializableDataTypes.STRING)
            .add("id", SerializableDataTypes.STRING)
            .add("hardness", SerializableDataTypes.INT, 3)
            .add("slipperiness",SerializableDataTypes.FLOAT, 0.6f)
            .add("resistance",SerializableDataTypes.INT, 3)
            .add("action", ApoliDataTypes.ENTITY_ACTION, null)
            .add("condition", ApoliDataTypes.ENTITY_CONDITION, null);

    public static SerializableData armorData = new SerializableData()
            .add("type",SerializableDataTypes.STRING)
            .add("namespace", SerializableDataTypes.STRING)
            .add("id", SerializableDataTypes.STRING)
            .add("name", SerializableDataTypes.STRING)
            .add("durability", SerializableDataTypes.INT, 10)
            .add("protection",SerializableDataTypes.INT, 0)
            .add("toughness",SerializableDataTypes.INT, 0)
            .add("knockback_resistance",SerializableDataTypes.INT, 0)
            .add("enchantability",SerializableDataTypes.INT, 0);

    public static SerializableData foodData = new SerializableData()
            .add("type",SerializableDataTypes.STRING)
            .add("namespace", SerializableDataTypes.STRING)
            .add("id", SerializableDataTypes.STRING)
            .add("max_count", SerializableDataTypes.INT, 64)
            .add("hunger",SerializableDataTypes.INT, 4)
            .add("saturation",SerializableDataTypes.FLOAT, 8f)
            .add("meat",SerializableDataTypes.BOOLEAN, false)
            .add("always_edible",SerializableDataTypes.BOOLEAN, false)
            .add("snack",SerializableDataTypes.BOOLEAN, false);
}
