package io.github.connor3001.ccpacks.customContent;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.apace100.apoli.power.PowerTypeReference;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.connor3001.ccpacks.CCPacksMain;
import io.github.connor3001.ccpacks.middleManTypes.*;
import io.github.connor3001.ccpacks.SerializableData.SerializableObjects;
import net.kyrptonaught.customportalapi.CustomPortalApiRegistry;
import net.kyrptonaught.customportalapi.portal.PortalIgnitionSource;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class DirCCPackRegistry {

    private final File base;

    public DirCCPackRegistry(File base) throws IOException {
        this.base = base;
        CCPacksMain.LOGGER.info("Registering Custom Items from Directories");
        registerFromDir();
        CCPacksMain.LOGGER.info("Directory Custom Items Registered");
    }

    public void registerFromDir() throws IOException {
        String string2 = "ccdata/";
        File pack = new File(this.base, "ccdata");
                try (Stream<Path> paths = Files.walk(Paths.get(pack.getPath()))) {
                    paths.forEach((file) -> {
                        String string3 = file.toString();
                        if (string3.endsWith(".json")) {
                            CCPacksMain.LOGGER.info(string3);
                            InputStream stream = null;
                            try {
                                stream = new FileInputStream(string3);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            JsonParser jsonParser = null;
                            jsonParser = new JsonParser();
                            JsonObject jsonObject = null;
                            try {
                                jsonObject = (JsonObject)jsonParser.parse(new InputStreamReader(stream, "UTF-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            SerializableData.Instance instance;
                            instance = SerializableObjects.getItemType.read(jsonObject);
                            SerializableData.Instance instance2;
                            CCPacksMain.LOGGER.info(instance.get("type"));
                            if(instance.getString("type").equals("ccpacks:item")) {
                                instance2 = SerializableObjects.itemData.read(jsonObject);
                                new MMItem((Identifier) instance2.get("identifier"), (List<String>)instance2.get("lore"), instance2.getInt("max_count"), (List<PowerTypeReference>) instance2.get("powers"));
                            } else if (instance.getString("type").equals("ccpacks:trinket")) {
                                instance2 = SerializableObjects.itemData.read(jsonObject);
                                new MMTrinket((Identifier) instance2.get("identifier"), (List<String>)instance2.get("lore"), instance2.getInt("durability"), (List<PowerTypeReference>) instance2.get("powers"));
                            } else if (instance.getString("type").equals("ccpacks:durable_item")) {
                                instance2 = SerializableObjects.itemData.read(jsonObject);
                                new MMDurableItem((Identifier) instance2.get("identifier"), (List<String>) instance2.get("lore"), instance2.getInt("durability"), (List<PowerTypeReference>) instance2.get("powers"));
                            } else if (instance.getString("type").equals("ccpacks:axe")) {
                                instance2 = SerializableObjects.toolData.read(jsonObject);
                                new MMAxeItem((Identifier) instance2.get("identifier"),instance2.getInt( "durability"),instance2.getFloat( "mining_speed_multiplier"),instance2.getInt("attack_speed"),instance2.getInt("mining_level"),instance2.getInt("enchantability"),instance2.getInt("attack_damage"), (List<PowerTypeReference>) instance2.get("powers"), (List<String>)instance2.get("lore"));
                            } else if (instance.getString("type").equals("ccpacks:block")) {
                                instance2 = SerializableObjects.blockData.read(jsonObject);
                                new MMBlock((Identifier) instance2.get("identifier"), instance2.getString("material"), instance2.getString("effective_tool"), instance2.getString("sound"),instance2.getBoolean("collidable"), instance2.getInt("hardness"), instance2.getInt("resistance"),instance2.getFloat("slipperiness"),instance2.getInt("luminance"),instance2.getInt("mining_level"),(ActionFactory<Entity>.Instance)instance2.get("action"),(ConditionFactory<LivingEntity>.Instance)instance2.get("condition"), (Identifier) instance2.get("loot_table"));
                            } else if (instance.getString("type").equals("ccpacks:falling_block")) {
                                instance2 = SerializableObjects.blockData.read(jsonObject);
                                new MMFallingBlock((Identifier) instance2.get("identifier"), instance2.getString("material"), instance2.getString("effective_tool"), instance2.getString("sound"),instance2.getBoolean("collidable"), instance2.getInt("hardness"), instance2.getInt("resistance"),instance2.getFloat("slipperiness"),instance2.getInt("luminance"),instance2.getInt("mining_level"),(ActionFactory<Entity>.Instance)instance2.get("action"),(ConditionFactory<LivingEntity>.Instance)instance2.get("condition"), (Identifier) instance2.get("loot_table"));
                            } else if (instance.getString("type").equals("ccpacks:vertical_slab")) {
                                instance2 = SerializableObjects.blockData.read(jsonObject);
                                new MMVSlabBlock((Identifier) instance2.get("identifier"), instance2.getString("material"), instance2.getString("effective_tool"), instance2.getString("sound"),instance2.getBoolean("collidable"), instance2.getInt("hardness"), instance2.getInt("resistance"),instance2.getFloat("slipperiness"),instance2.getInt("luminance"),instance2.getInt("mining_level"),(ActionFactory<Entity>.Instance)instance2.get("action"),(ConditionFactory<LivingEntity>.Instance)instance2.get("condition"), (Identifier) instance2.get("loot_table"));
                            } else if (instance.getString("type").equals("ccpacks:horizontal_slab")) {
                                instance2 = SerializableObjects.blockData.read(jsonObject);
                                new MMHSlabBlock((Identifier) instance2.get("identifier"), instance2.getString("material"), instance2.getString("effective_tool"), instance2.getString("sound"),instance2.getBoolean("collidable"), instance2.getInt("hardness"), instance2.getInt("resistance"),instance2.getFloat("slipperiness"),instance2.getInt("luminance"),instance2.getInt("mining_level"),(ActionFactory<Entity>.Instance)instance2.get("action"),(ConditionFactory<LivingEntity>.Instance)instance2.get("condition"), (Identifier) instance2.get("loot_table"));
                            } else if (instance.getString("type").equals("ccpacks:stairs")) {
                                instance2 = SerializableObjects.stairsData.read(jsonObject);
                                new MMStairBlock((Identifier) instance2.get("identifier"), instance2.getString("material"), instance2.getString("effective_tool"), instance2.getString("sound"),instance2.getBoolean("collidable"), instance2.getInt("hardness"), instance2.getInt("resistance"),instance2.getFloat("slipperiness"),instance2.getInt("luminance"),instance2.getInt("mining_level"),(ActionFactory<Entity>.Instance)instance2.get("action"),(ConditionFactory<LivingEntity>.Instance)instance2.get("condition"), (Identifier) instance2.get("loot_table"), Registry.BLOCK.get((Identifier) instance2.get("base_block")));
                            } else if (instance.getString("type").equals("ccpacks:boots")) {
                                instance2 = SerializableObjects.armorData.read(jsonObject);
                                new MMBoots((Identifier) instance2.get("identifier"),instance2.getInt("durability"),instance2.getString("name"),instance2.getInt("protection"),instance2.getInt("toughness"),instance2.getInt("enchantability"),instance2.getInt("knockback_resistance"), (List<PowerTypeReference>) instance2.get("powers"), (List<String>)instance2.get("lore"), (Item)instance2.get("repair_item"));
                            } else if (instance.getString("type").equals("ccpacks:chestplate")) {
                                instance2 = SerializableObjects.armorData.read(jsonObject);
                                new MMChestplate((Identifier) instance2.get("identifier"),instance2.getInt("durability"),instance2.getString("name"),instance2.getInt("protection"),instance2.getInt("toughness"),instance2.getInt("enchantability"),instance2.getInt("knockback_resistance"), (List<PowerTypeReference>) instance2.get("powers"), (List<String>)instance2.get("lore"), (Item)instance2.get("repair_item"));
                            } else if (instance.getString("type").equals("ccpacks:food")) {
                                instance2 = SerializableObjects.foodData.read(jsonObject);
                                new MMFoodItem((Identifier) instance2.get("identifier"),instance2.getInt("max_count"),instance2.getInt("hunger"),instance2.getFloat("saturation"),instance2.getBoolean("meat"),instance2.getBoolean("snack"),instance2.getBoolean("always_edible"));
                            } else if (instance.getString("type").equals("ccpacks:helmet")) {
                                instance2 = SerializableObjects.armorData.read(jsonObject);
                                new MMHelmet((Identifier) instance2.get("identifier"),instance2.getInt("durability"),instance2.getString("name"),instance2.getInt("protection"),instance2.getInt("toughness"),instance2.getInt("enchantability"),instance2.getInt("knockback_resistance"), (List<PowerTypeReference>) instance2.get("powers"), (List<String>)instance2.get("lore"), (Item)instance2.get("repair_item"));
                            } else if (instance.getString("type").equals("ccpacks:hoe")) {
                                instance2 = SerializableObjects.toolData.read(jsonObject);
                                new MMHoeItem((Identifier) instance2.get("identifier"),instance2.getInt( "durability"),instance2.getFloat( "mining_speed_multiplier"),instance2.getInt("attack_speed"),instance2.getInt("mining_level"),instance2.getInt("enchantability"),instance2.getInt("attack_damage"), (List<PowerTypeReference>) instance2.get("powers"), (List<String>)instance2.get("lore"));
                            } else if (instance.getString("type").equals("ccpacks:leggings")) {
                                instance2 = SerializableObjects.armorData.read(jsonObject);
                                new MMLegging((Identifier) instance2.get("identifier"),instance2.getInt("durability"),instance2.getString("name"),instance2.getInt("protection"),instance2.getInt("toughness"),instance2.getInt("enchantability"),instance2.getInt("knockback_resistance"), (List<PowerTypeReference>) instance2.get("powers"), (List<String>)instance2.get("lore"), (Item)instance2.get("repair_item"));
                            } else if (instance.getString("type").equals("ccpacks:pickaxe")) {
                                instance2 = SerializableObjects.toolData.read(jsonObject);
                                new MMPickaxeItem((Identifier) instance2.get("identifier"),instance2.getInt( "durability"),instance2.getFloat( "mining_speed_multiplier"),instance2.getInt("attack_speed"),instance2.getInt("mining_level"),instance2.getInt("enchantability"),instance2.getInt("attack_damage"), (List<PowerTypeReference>) instance2.get("powers"), (List<String>)instance2.get("lore"));
                            } else if (instance.getString("type").equals("ccpacks:shovels")) {
                                instance2 = SerializableObjects.toolData.read(jsonObject);
                                new MMShovelItem((Identifier) instance2.get("identifier"),instance2.getInt( "durability"),instance2.getFloat( "mining_speed_multiplier"),instance2.getInt("attack_speed"),instance2.getInt("mining_level"),instance2.getInt("enchantability"),instance2.getInt("attack_damage"), (List<PowerTypeReference>) instance2.get("powers"), (List<String>)instance2.get("lore"));
                            } else if (instance.getString("type").equals("ccpacks:sword")) {
                                instance2 = SerializableObjects.toolData.read(jsonObject);
                                new MMSword((Identifier) instance2.get("identifier"),instance2.getInt( "durability"),instance2.getFloat( "mining_speed_multiplier"),instance2.getInt("attack_speed"),instance2.getInt("mining_level"),instance2.getInt("enchantability"),instance2.getInt("attack_damage"), (List<PowerTypeReference>) instance2.get("powers"), (List<String>)instance2.get("lore"));
                            } else if (instance.getString("type").equals("ccpacks:keybind")) {
                                instance2 = SerializableObjects.keybindData.read(jsonObject);
                                new MMKeybind(instance2.getString("name"),instance2.getString("category"));
                            } else if (instance.getString("type").equals("ccpacks:status_effect")) {
                                instance2 = SerializableObjects.statusEffectData.read(jsonObject);
                                new MMStatusEffectType((Identifier) instance2.get("identifier"), instance2.getString("color"), (List<PowerTypeReference>)instance2.get("powers"));
                            } else if (instance.getString("type").equals("ccpacks:enchantment")) {
                                instance2 = SerializableObjects.statusEffectData.read(jsonObject);
                                new MMEnchantment((Identifier) instance2.get("identifier"));
                            } else if (instance.getString("type").equals("ccpacks:portal")) {
                                instance2 = SerializableObjects.portalData.read(jsonObject);
                                CustomPortalApiRegistry.addPortal((Block) instance2.get("block"), PortalIgnitionSource.ItemUseSource((Item)instance2.get("ignition_item")), (Identifier)instance2.get("dimension"), instance2.getInt("r"), instance2.getInt("g"), instance2.getInt("b"));
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
}
