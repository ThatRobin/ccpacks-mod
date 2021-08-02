package io.github.ThatRobin.ccpacks.serializableData;

import com.google.gson.JsonParseException;
import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.calio.Calio;
import io.github.apace100.calio.ClassUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagGroup;
import net.minecraft.tag.TagManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Collection;
import java.util.List;

public class SerializableObjects {

    public static SerializableData getType = new SerializableData()
            .add("type", SerializableDataTypes.STRING);


    public static SerializableData getItemType = new SerializableData()
            .add("type", SerializableDataTypes.STRING)
            .add("subtype", SerializableDataTypes.STRING, null);

    public static SerializableData getEntityType = new SerializableData()
            .add("type", SerializableDataTypes.STRING)
            .add("subtype", SerializableDataTypes.STRING);

    public static SerializableData biomeData = new SerializableData()
            .add("type", SerializableDataTypes.STRING)
            .add("identifier", SerializableDataTypes.IDENTIFIER)
            .add("primary_block", SerializableDataTypes.BLOCK)
            .add("secondary_block", SerializableDataTypes.BLOCK)
            .add("tertiary_block", SerializableDataTypes.BLOCK)
            .add("weight", SerializableDataTypes.DOUBLE)
            .add("climates", CCPackDataTypes.OVERWORLD_CLIMATES);


    public static SerializableData particleData = new SerializableData()
            .add("type", SerializableDataTypes.STRING)
            .add("identifier", SerializableDataTypes.IDENTIFIER)
            .add("glowing", SerializableDataTypes.BOOLEAN, false)
            .add("red", SerializableDataTypes.FLOAT, null)
            .add("green", SerializableDataTypes.FLOAT, null)
            .add("blue", SerializableDataTypes.FLOAT, null)
            .add("size", SerializableDataTypes.FLOAT, 0.25f)
            .add("max_age", SerializableDataTypes.INT, 100)
            .add("collides_with_world", SerializableDataTypes.BOOLEAN, false)
            .add("alpha", SerializableDataTypes.FLOAT, 1f);

    public static SerializableData paintingData = new SerializableData()
            .add("type", SerializableDataTypes.STRING)
            .add("identifier", SerializableDataTypes.IDENTIFIER)
            .add("width", SerializableDataTypes.INT, 1)
            .add("height", SerializableDataTypes.INT, 1);

    public static SerializableData mooshroomEntityData = new SerializableData()
            .add("type", SerializableDataTypes.STRING)
            .add("identifier", SerializableDataTypes.IDENTIFIER)
            .add("texture", SerializableDataTypes.IDENTIFIER)
            .add("back_item", SerializableDataTypes.IDENTIFIER);

    public static SerializableData genericEntityData = new SerializableData()
            .add("type", SerializableDataTypes.STRING)
            .add("identifier", SerializableDataTypes.IDENTIFIER)
            .add("texture", SerializableDataTypes.IDENTIFIER);

    public static SerializableData statusEffectData = new SerializableData()
            .add("type", SerializableDataTypes.STRING)
            .add("identifier", SerializableDataTypes.IDENTIFIER)
            .add("red", SerializableDataTypes.INT, 255)
            .add("green", SerializableDataTypes.INT, 255)
            .add("blue", SerializableDataTypes.INT, 255);

    public static SerializableData enchantmentData = new SerializableData()
            .add("type", SerializableDataTypes.STRING)
            .add("identifier", SerializableDataTypes.IDENTIFIER)
            .add("curse", SerializableDataTypes.BOOLEAN)
            .add("max_level", SerializableDataTypes.INT, 1)
            .add("rarity", SerializableDataType.enumValue(Enchantment.Rarity.class), Enchantment.Rarity.VERY_RARE);

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
            .add("subtype", SerializableDataTypes.STRING)
            .add("identifier", SerializableDataTypes.IDENTIFIER)
            .add("lore", CCPackDataTypes.STRINGS, null)
            .add("durability", SerializableDataTypes.INT, null)
            .add("fuel_tick", SerializableDataTypes.INT, 0)
            .add("start_color", CCPackDataTypes.COLOR, null)
            .add("end_color", CCPackDataTypes.COLOR, null)
            .add("max_count", SerializableDataTypes.INT, 64);

    public static SerializableData shieldData = new SerializableData()
            .add("type",SerializableDataTypes.STRING)
            .add("identifier", SerializableDataTypes.IDENTIFIER)
            .add("durability", SerializableDataTypes.INT, 100)
            .add("cooldown", SerializableDataTypes.INT, 60)
            .add("enchantability",SerializableDataTypes.INT, 0)
            .add("repair_item", SerializableDataTypes.ITEM, null);

    public static SerializableData toolData = new SerializableData()
            .add("type",SerializableDataTypes.STRING)
            .add("subtype", SerializableDataTypes.STRING)
            .add("identifier", SerializableDataTypes.IDENTIFIER)
            .add("durability", SerializableDataTypes.INT, 10)
            .add("mining_speed_multiplier",SerializableDataTypes.FLOAT, 0f)
            .add("attack_speed",SerializableDataTypes.INT, 0)
            .add("mining_level",SerializableDataTypes.INT, 0)
            .add("enchantability",SerializableDataTypes.INT, 0)
            .add("attack_damage",SerializableDataTypes.INT, 0)
            .add("repair_item",SerializableDataTypes.ITEM_STACK,null)
            .add("lore", CCPackDataTypes.STRINGS, null);

    public static SerializableData blockData = new SerializableData()
            .add("type", SerializableDataTypes.STRING)
            .add("subtype", SerializableDataTypes.STRING)
            .add("identifier", SerializableDataTypes.IDENTIFIER)
            .add("material", CCPackDataTypes.MATERIAL)
            .add("effective_tool", SerializableDataTypes.STRING)
            .add("block_sound_group", CCPackDataTypes.BLOCK_SOUND_GROUP)
            .add("collidable", SerializableDataTypes.BOOLEAN, true)
            .add("transparent", SerializableDataTypes.BOOLEAN, false)
            .add("hardness", SerializableDataTypes.INT, 3)
            .add("slipperiness", SerializableDataTypes.FLOAT, 0.6f)
            .add("resistance", SerializableDataTypes.INT, 3)
            .add("luminance", SerializableDataTypes.INT, 0)
            .add("mining_level", SerializableDataTypes.INT, 1)
            .add("loot_table", SerializableDataTypes.IDENTIFIER)
            .add("make_block_item", SerializableDataTypes.BOOLEAN, true);

    public static SerializableData stairsData = new SerializableData()
            .add("type", SerializableDataTypes.STRING)
            .add("subtype", SerializableDataTypes.STRING)
            .add("identifier", SerializableDataTypes.IDENTIFIER)
            .add("material", SerializableDataTypes.STRING)
            .add("effective_tool", SerializableDataTypes.STRING)
            .add("block_sound_group", CCPackDataTypes.BLOCK_SOUND_GROUP)
            .add("collidable", SerializableDataTypes.BOOLEAN, true)
            .add("transparent", SerializableDataTypes.BOOLEAN, false)
            .add("hardness", SerializableDataTypes.INT, 3)
            .add("slipperiness", SerializableDataTypes.FLOAT, 0.6f)
            .add("resistance", SerializableDataTypes.INT, 3)
            .add("luminance", SerializableDataTypes.INT, 0)
            .add("mining_level", SerializableDataTypes.INT, 1)
            .add("loot_table", SerializableDataTypes.IDENTIFIER)
            .add("base_block", SerializableDataTypes.IDENTIFIER)
            .add("make_block_item", SerializableDataTypes.BOOLEAN, true);

    public static SerializableData armorData = new SerializableData()
            .add("type",SerializableDataTypes.STRING)
            .add("subtype",SerializableDataTypes.STRING)
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
            .add("subtype", SerializableDataTypes.STRING)
            .add("identifier", SerializableDataTypes.IDENTIFIER)
            .add("max_count", SerializableDataTypes.INT, 64)
            .add("hunger",SerializableDataTypes.INT, 4)
            .add("saturation",SerializableDataTypes.FLOAT, 8f)
            .add("meat",SerializableDataTypes.BOOLEAN, false)
            .add("always_edible",SerializableDataTypes.BOOLEAN, false)
            .add("lore", CCPackDataTypes.STRINGS, null)
            .add("drinkable", SerializableDataTypes.BOOLEAN, false)
            .add("sound", SerializableDataTypes.SOUND_EVENT, SoundEvents.ENTITY_GENERIC_EAT)
            .add("returns", SerializableDataTypes.ITEM, null)
            .add("eating_time", SerializableDataTypes.INT, 30)
            .add("eat_action", ApoliDataTypes.ENTITY_ACTION, null);

    public static SerializableData musicDiscData = new SerializableData()
            .add("type",SerializableDataTypes.STRING)
            .add("subtype", SerializableDataTypes.STRING)
            .add("identifier",SerializableDataTypes.IDENTIFIER)
            .add("comparator_output",SerializableDataTypes.INT,1)
            .add("sound",SerializableDataTypes.SOUND_EVENT);

    public static SerializableData soundEventData = new SerializableData()
            .add("type",SerializableDataTypes.STRING)
            .add("identifier",SerializableDataTypes.IDENTIFIER);

    public static SerializableData projectileData = new SerializableData()
            .add("type", SerializableDataTypes.STRING)
            .add("identifier", SerializableDataTypes.IDENTIFIER)
            .add("damage", SerializableDataTypes.INT, 0)
            .add("height", SerializableDataTypes.FLOAT, 0.25f)
            .add("width", SerializableDataTypes.FLOAT, 0.25f)
            .add("base_item", SerializableDataTypes.ITEM)
            .add("damage_source", SerializableDataTypes.DAMAGE_SOURCE)
            .add("hit_action", ApoliDataTypes.ENTITY_ACTION, null)
            .add("collision_action", ApoliDataTypes.ENTITY_ACTION, null);

}
