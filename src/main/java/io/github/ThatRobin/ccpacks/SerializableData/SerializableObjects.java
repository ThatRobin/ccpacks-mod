package io.github.ThatRobin.ccpacks.SerializableData;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;

public class SerializableObjects {

    public static SerializableData getItemType = new SerializableData()
            .add("type", SerializableDataTypes.STRING)
            .add("subtype", SerializableDataTypes.STRING);

    public static SerializableData getEntityType = new SerializableData()
            .add("type", SerializableDataTypes.STRING)
            .add("subtype", SerializableDataTypes.STRING);


    public static SerializableData particleData = new SerializableData()
            .add("type", SerializableDataTypes.STRING)
            .add("identifier", SerializableDataTypes.IDENTIFIER);


    public static SerializableData mooshroomEntityData = new SerializableData()
            .add("type", SerializableDataTypes.STRING)
            .add("identifier", SerializableDataTypes.IDENTIFIER)
            .add("texture", SerializableDataTypes.IDENTIFIER)
            .add("back_item", SerializableDataTypes.IDENTIFIER);

    public static SerializableData statusEffectData = new SerializableData()
            .add("type", SerializableDataTypes.STRING)
            .add("identifier", SerializableDataTypes.IDENTIFIER)
            .add("color", SerializableDataTypes.STRING, "0x00FFFF")
            .add("powers", CCPackDataTypes.POWER_TYPES, null);

    public static SerializableData universalPowerData = new SerializableData()
            .add("type", SerializableDataTypes.STRING)
            .add("powers", CCPackDataTypes.POWER_TYPES, null);

    public static SerializableData portalData = new SerializableData()
            .add("type", SerializableDataTypes.STRING)
            .add("dimension", SerializableDataTypes.IDENTIFIER)
            .add("block", SerializableDataTypes.BLOCK)
            .add("ignition_item", SerializableDataTypes.ITEM)
            .add("red", SerializableDataTypes.INT, 255)
            .add("green", SerializableDataTypes.INT, 255)
            .add("blue", SerializableDataTypes.INT, 255);

    public static SerializableData keybindData = new SerializableData()
            .add("type", SerializableDataTypes.STRING)
            .add("name", SerializableDataTypes.STRING)
            .add("category", SerializableDataTypes.STRING);

    public static SerializableData itemData = new SerializableData()
            .add("type",SerializableDataTypes.STRING)
            .add("identifier", SerializableDataTypes.IDENTIFIER)
            .add("lore", CCPackDataTypes.STRINGS, null)
            .add("durability", SerializableDataTypes.INT, null)
            .add("max_count", SerializableDataTypes.INT, 64);

    public static SerializableData shieldData = new SerializableData()
            .add("type",SerializableDataTypes.STRING)
            .add("identifier", SerializableDataTypes.IDENTIFIER)
            .add("lore", CCPackDataTypes.STRINGS, null)
            .add("durability", SerializableDataTypes.INT, null)
            .add("cooldown", SerializableDataTypes.INT, 60)
            .add("enchantability",SerializableDataTypes.INT, 0)
            .add("repair_item", SerializableDataTypes.ITEM, null);

    public static SerializableData toolData = new SerializableData()
            .add("type",SerializableDataTypes.STRING)
            .add("identifier", SerializableDataTypes.IDENTIFIER)
            .add("durability", SerializableDataTypes.INT, 10)
            .add("mining_speed_multiplier",SerializableDataTypes.FLOAT, 0f)
            .add("attack_speed",SerializableDataTypes.INT, 0)
            .add("mining_level",SerializableDataTypes.INT, 0)
            .add("enchantability",SerializableDataTypes.INT, 0)
            .add("attack_damage",SerializableDataTypes.INT, 0)
            .add("lore", CCPackDataTypes.STRINGS, null);

    public static SerializableData blockData = new SerializableData()
            .add("type", SerializableDataTypes.STRING)
            .add("identifier", SerializableDataTypes.IDENTIFIER)
            .add("material", SerializableDataTypes.STRING)
            .add("effective_tool", SerializableDataTypes.STRING)
            .add("sound", SerializableDataTypes.STRING)
            .add("collidable", SerializableDataTypes.BOOLEAN, true)
            .add("transparent", SerializableDataTypes.BOOLEAN, false)
            .add("hardness", SerializableDataTypes.INT, 3)
            .add("slipperiness", SerializableDataTypes.FLOAT, 0.6f)
            .add("resistance", SerializableDataTypes.INT, 3)
            .add("luminance", SerializableDataTypes.INT, 0)
            .add("mining_level", SerializableDataTypes.INT, 1)
            .add("action", ApoliDataTypes.ENTITY_ACTION, null)
            .add("condition", ApoliDataTypes.ENTITY_CONDITION, null)
            .add("loot_table", SerializableDataTypes.IDENTIFIER);

    public static SerializableData stairsData = new SerializableData()
            .add("type", SerializableDataTypes.STRING)
            .add("identifier", SerializableDataTypes.IDENTIFIER)
            .add("material", SerializableDataTypes.STRING)
            .add("effective_tool", SerializableDataTypes.STRING)
            .add("sound", SerializableDataTypes.STRING)
            .add("collidable", SerializableDataTypes.BOOLEAN, true)
            .add("transparent", SerializableDataTypes.BOOLEAN, false)
            .add("hardness", SerializableDataTypes.INT, 3)
            .add("slipperiness", SerializableDataTypes.FLOAT, 0.6f)
            .add("resistance", SerializableDataTypes.INT, 3)
            .add("luminance", SerializableDataTypes.INT, 0)
            .add("mining_level", SerializableDataTypes.INT, 1)
            .add("action", ApoliDataTypes.ENTITY_ACTION, null)
            .add("condition", ApoliDataTypes.ENTITY_CONDITION, null)
            .add("loot_table", SerializableDataTypes.IDENTIFIER)
            .add("base_block", SerializableDataTypes.IDENTIFIER);

    public static SerializableData armorData = new SerializableData()
            .add("type",SerializableDataTypes.STRING)
            .add("identifier", SerializableDataTypes.IDENTIFIER)
            .add("name", SerializableDataTypes.STRING)
            .add("durability", SerializableDataTypes.INT, 10)
            .add("protection",SerializableDataTypes.INT, 0)
            .add("toughness",SerializableDataTypes.INT, 0)
            .add("knockback_resistance",SerializableDataTypes.INT, 0)
            .add("enchantability",SerializableDataTypes.INT, 0)
            .add("lore", CCPackDataTypes.STRINGS, null)
            .add("repair_item", SerializableDataTypes.ITEM, null);

    public static SerializableData foodData = new SerializableData()
            .add("type",SerializableDataTypes.STRING)
            .add("identifier", SerializableDataTypes.IDENTIFIER)
            .add("max_count", SerializableDataTypes.INT, 64)
            .add("hunger",SerializableDataTypes.INT, 4)
            .add("saturation",SerializableDataTypes.FLOAT, 8f)
            .add("meat",SerializableDataTypes.BOOLEAN, false)
            .add("always_edible",SerializableDataTypes.BOOLEAN, false)
            .add("lore", CCPackDataTypes.STRINGS, null)
            .add("snack",SerializableDataTypes.BOOLEAN, false);
}
