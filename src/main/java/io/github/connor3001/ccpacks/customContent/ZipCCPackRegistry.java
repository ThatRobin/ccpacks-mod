package io.github.connor3001.ccpacks.customContent;

import com.google.common.base.Splitter;
import com.google.gson.*;
import io.github.connor3001.ccpacks.CCPacksMain;
import io.github.connor3001.ccpacks.CustomObjects.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipCCPackRegistry {

    private static final Identifier ITEM = CCPacksMain.identifier("item");
    private static final Identifier AXE = CCPacksMain.identifier("item_axe");
    private static final Identifier PICKAXE = CCPacksMain.identifier("item_pickaxe");
    private static final Identifier SWORD = CCPacksMain.identifier("item_sword");
    private static final Identifier SHOVEL = CCPacksMain.identifier("item_shovel");
    private static final Identifier HOE = CCPacksMain.identifier("item_hoe");
    private static final Identifier LEGGINGS = CCPacksMain.identifier("item_leggings");
    private static final Identifier BOOTS = CCPacksMain.identifier("item_boots");
    private static final Identifier CHESTPLATE = CCPacksMain.identifier("item_chestplate");
    private static final Identifier HELMET = CCPacksMain.identifier("item_helmet");
    private static final Identifier FOOD = CCPacksMain.identifier("item_food");
    private static final Identifier BLOCK = CCPacksMain.identifier("block");

    private ZipFile file;
    private final File base;

    public ZipCCPackRegistry(File base, ZipFile file) throws IOException {
        this.base = base;
        this.file = file;
        CCPacksMain.LOGGER.info("Registering Custom Items from Zips");
        registerFromZip();
        CCPacksMain.LOGGER.info("Zip Custom Items Registered");
    }

    private ZipFile getZipFile() throws IOException {
        if (this.file == null) {
            this.file = new ZipFile(this.base);
        }
        return this.file;
    }

    public void registerFromZip() throws IOException {
        ZipFile zipFile2 = this.getZipFile();

        Enumeration<? extends ZipEntry> enumeration = zipFile2.entries();

        String var10000 = "ccdata/";
        String string = var10000;
        String string2 = string;

        while(enumeration.hasMoreElements()) {
            ZipEntry zipEntry = (ZipEntry)enumeration.nextElement();
            if (!zipEntry.isDirectory()) {
                String string3 = zipEntry.getName();
                if (string3.endsWith(".json") && string3.startsWith(string2)) {
                    InputStream stream = zipFile2.getInputStream(zipEntry);
                    JsonParser jsonParser = new JsonParser();
                    JsonObject jsonObject = (JsonObject)jsonParser.parse(new InputStreamReader(stream, "UTF-8"));
                    Identifier factoryId = Identifier.tryParse(JsonHelper.getString(jsonObject, "type"));
                    CCPacksMain.LOGGER.info(ITEM +"/"+ factoryId);
                    CCPacksMain.LOGGER.info(AXE +"/"+ factoryId);
                    CCPacksMain.LOGGER.info(PICKAXE +"/"+ factoryId);
                    CCPacksMain.LOGGER.info(SHOVEL +"/"+ factoryId);
                    CCPacksMain.LOGGER.info(SWORD +"/"+ factoryId);
                    CCPacksMain.LOGGER.info(HELMET +"/"+ factoryId);

                    if(ITEM.equals(factoryId)) {
                        try {
                            String command = JsonHelper.getString(jsonObject, "command");
                            //CustomItem item = new CustomItem(JsonHelper.getString(jsonObject, "namespace"), JsonHelper.getString(jsonObject, "id"), JsonHelper.getInt(jsonObject, "max_count"), JsonHelper.getBoolean(jsonObject, "useable"), JsonHelper.getString(jsonObject, "command"));
                        } catch(Exception e) {
                            //CustomItem item = new CustomItem(JsonHelper.getString(jsonObject, "namespace"), JsonHelper.getString(jsonObject, "id"), JsonHelper.getInt(jsonObject, "max_count"), JsonHelper.getBoolean(jsonObject, "useable"), "tellraw @s \"you shouldnt see this\"");
                        }
                    }
                    if(AXE.equals(factoryId)) {
                        CustomAxe item = new CustomAxe(JsonHelper.getString(jsonObject, "namespace"),JsonHelper.getString(jsonObject, "id"),JsonHelper.getInt(jsonObject, "durability"),JsonHelper.getFloat(jsonObject, "mining_speed_multiplier"),JsonHelper.getInt(jsonObject, "attack_speed"),JsonHelper.getInt(jsonObject, "mining_level"),JsonHelper.getInt(jsonObject, "enchantability"),JsonHelper.getInt(jsonObject, "attack_damage"));
                    }
                    if(BLOCK.equals(factoryId)) {
                        //CustomBlock item = new CustomBlock(JsonHelper.getString(jsonObject, "namespace"),JsonHelper.getString(jsonObject, "id"),JsonHelper.getInt(jsonObject, "hardness"), JsonHelper.getInt(jsonObject, "resistance"),JsonHelper.getFloat(jsonObject, "slipperiness"));
                    }
                    if(BOOTS.equals(factoryId)) {
                        CustomBoots item = new CustomBoots(JsonHelper.getString(jsonObject, "namespace"),JsonHelper.getString(jsonObject, "id"),JsonHelper.getInt(jsonObject, "durability"), JsonHelper.getString(jsonObject, "name"),JsonHelper.getInt(jsonObject, "protection"),JsonHelper.getInt(jsonObject, "toughness"),JsonHelper.getInt(jsonObject, "enchantability"),JsonHelper.getInt(jsonObject, "knockback_resistance"));
                    }
                    if(CHESTPLATE.equals(factoryId)) {
                        CustomChestplate item = new CustomChestplate(JsonHelper.getString(jsonObject, "namespace"),JsonHelper.getString(jsonObject, "id"),JsonHelper.getInt(jsonObject, "durability"), JsonHelper.getString(jsonObject, "name"),JsonHelper.getInt(jsonObject, "protection"),JsonHelper.getInt(jsonObject, "toughness"),JsonHelper.getInt(jsonObject, "enchantability"),JsonHelper.getInt(jsonObject, "knockback_resistance"));
                    }
                    if(FOOD.equals(factoryId)) {
                        CustomFood item = new CustomFood(JsonHelper.getString(jsonObject, "namespace"),JsonHelper.getString(jsonObject, "id"),JsonHelper.getInt(jsonObject, "max_count"),JsonHelper.getInt(jsonObject, "hunger"),JsonHelper.getFloat(jsonObject, "saturation"),JsonHelper.getBoolean(jsonObject, "meat"),JsonHelper.getBoolean(jsonObject, "snack"),JsonHelper.getBoolean(jsonObject, "always_edible"));
                    }
                    if(HELMET.equals(factoryId)) {
                        CustomHelmet item = new CustomHelmet(JsonHelper.getString(jsonObject, "namespace"),JsonHelper.getString(jsonObject, "id"),JsonHelper.getInt(jsonObject, "durability"), JsonHelper.getString(jsonObject, "name"),JsonHelper.getInt(jsonObject, "protection"),JsonHelper.getInt(jsonObject, "toughness"),JsonHelper.getInt(jsonObject, "enchantability"),JsonHelper.getInt(jsonObject, "knockback_resistance"));
                    }
                    if(HOE.equals(factoryId)) {
                        CustomHoe item = new CustomHoe(JsonHelper.getString(jsonObject, "namespace"),JsonHelper.getString(jsonObject, "id"),JsonHelper.getInt(jsonObject, "durability"),JsonHelper.getFloat(jsonObject, "mining_speed_multiplier"),JsonHelper.getInt(jsonObject, "attack_speed"),JsonHelper.getInt(jsonObject, "mining_level"),JsonHelper.getInt(jsonObject, "enchantability"),JsonHelper.getInt(jsonObject, "attack_damage"));
                    }
                    if(LEGGINGS.equals(factoryId)) {
                        CustomLegging item = new CustomLegging(JsonHelper.getString(jsonObject, "namespace"),JsonHelper.getString(jsonObject, "id"),JsonHelper.getInt(jsonObject, "durability"), JsonHelper.getString(jsonObject, "name"),JsonHelper.getInt(jsonObject, "protection"),JsonHelper.getInt(jsonObject, "toughness"),JsonHelper.getInt(jsonObject, "enchantability"),JsonHelper.getInt(jsonObject, "knockback_resistance"));
                    }
                    if(PICKAXE.equals(factoryId)) {
                        CustomPickaxe item = new CustomPickaxe(JsonHelper.getString(jsonObject, "namespace"),JsonHelper.getString(jsonObject, "id"),JsonHelper.getInt(jsonObject, "durability"),JsonHelper.getFloat(jsonObject, "mining_speed_multiplier"),JsonHelper.getInt(jsonObject, "attack_speed"),JsonHelper.getInt(jsonObject, "mining_level"),JsonHelper.getInt(jsonObject, "enchantability"),JsonHelper.getInt(jsonObject, "attack_damage"));
                    }
                    if(SHOVEL.equals(factoryId)) {
                        CustomShovel item = new CustomShovel(JsonHelper.getString(jsonObject, "namespace"),JsonHelper.getString(jsonObject, "id"),JsonHelper.getInt(jsonObject, "durability"),JsonHelper.getFloat(jsonObject, "mining_speed_multiplier"),JsonHelper.getInt(jsonObject, "attack_speed"),JsonHelper.getInt(jsonObject, "mining_level"),JsonHelper.getInt(jsonObject, "enchantability"),JsonHelper.getInt(jsonObject, "attack_damage"));
                    }
                    if(SWORD.equals(factoryId)) {
                        CustomSword item = new CustomSword(JsonHelper.getString(jsonObject, "namespace"),JsonHelper.getString(jsonObject, "id"),JsonHelper.getInt(jsonObject, "durability"),JsonHelper.getFloat(jsonObject, "mining_speed_multiplier"),JsonHelper.getInt(jsonObject, "attack_speed"),JsonHelper.getInt(jsonObject, "mining_level"),JsonHelper.getInt(jsonObject, "enchantability"),JsonHelper.getInt(jsonObject, "attack_damage"));
                    }
                }
            }
        }
    }
}
