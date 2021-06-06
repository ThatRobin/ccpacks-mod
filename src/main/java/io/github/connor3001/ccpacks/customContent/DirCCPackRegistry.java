package io.github.connor3001.ccpacks.customContent;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.apace100.apoli.power.Active;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.connor3001.ccpacks.CCPacksMain;
import io.github.connor3001.ccpacks.CustomObjects.*;
import io.github.connor3001.ccpacks.SerializableData.SerializableObjects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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
        File f = new File(MinecraftClient.getInstance().runDirectory, "resourcepacks");
        String[] pathnames;
        pathnames = f.list();
        if (pathnames.length > 0) {
            for (int i = 0; i < pathnames.length; i++) {
                File dir = new File(f, pathnames[i]);
                File pack = new File(dir, "ccdata");
                try (Stream<Path> paths = Files.walk(Paths.get(pack.getPath()))) {
                    paths.forEach((file) -> {
                        String string3 = file.toString();
                        if (string3.endsWith(".json")) {
                            InputStream stream = null;
                            try {
                                stream = new FileInputStream(String.valueOf(file));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            JsonParser jsonParser = new JsonParser();
                            JsonObject jsonObject = null;
                            try {
                                jsonObject = (JsonObject)jsonParser.parse(new InputStreamReader(stream, "UTF-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            Identifier factoryId = Identifier.tryParse(JsonHelper.getString(jsonObject, "type"));

                            SerializableData.Instance instance = SerializableObjects.getItemType.read(jsonObject);
                            CCPacksMain.LOGGER.info(instance.get("type"));
                            if(instance.getString("type").equals("ccpacks:item")) {
                                SerializableData.Instance instance2 = SerializableObjects.itemData.read(jsonObject);
                                CustomItem item = new CustomItem(instance2.getString("namespace"), instance2.getString("id"), instance2.getInt("max_count"), instance2.getBoolean("consume"),(ActionFactory<Entity>.Instance)instance2.get("action"),(ConditionFactory<LivingEntity>.Instance)instance2.get("condition"));
                            }
                            if(instance.getString("type").equals("ccpacks:item_axe")) {
                                SerializableData.Instance instance2 = SerializableObjects.toolData.read(jsonObject);
                                CustomAxe item = new CustomAxe(instance2.getString("namespace"),instance2.getString( "id"),instance2.getInt( "durability"),instance2.getFloat( "mining_speed_multiplier"),instance2.getInt("attack_speed"),instance2.getInt("mining_level"),instance2.getInt("enchantability"),instance2.getInt("attack_damage"));
                            }
                            if(instance.getString("type").equals("ccpacks:block")) {
                                SerializableData.Instance instance2 = SerializableObjects.blockData.read(jsonObject);
                                 CustomBlock item = new CustomBlock(instance2.getString("namespace"),instance2.getString("id"),instance2.getInt("hardness"), instance2.getInt("resistance"),instance2.getFloat("slipperiness"),(ActionFactory<Entity>.Instance)instance2.get("action"),(ConditionFactory<LivingEntity>.Instance)instance2.get("condition"));
                            }
                            if(instance.getString("type").equals("ccpacks:item_boots")) {
                                SerializableData.Instance instance2 = SerializableObjects.armorData.read(jsonObject);
                                CustomBoots item = new CustomBoots(instance2.getString("namespace"),instance2.getString("id"),instance2.getInt("durability"),instance2.getString("name"),instance2.getInt("protection"),instance2.getInt("toughness"),instance2.getInt("enchantability"),instance2.getInt("knockback_resistance"));
                            }
                            if(instance.getString("type").equals("ccpacks:item_chestplate")) {
                                SerializableData.Instance instance2 = SerializableObjects.armorData.read(jsonObject);
                                CustomChestplate item = new CustomChestplate(instance2.getString("namespace"),instance2.getString("id"),instance2.getInt("durability"),instance2.getString("name"),instance2.getInt("protection"),instance2.getInt("toughness"),instance2.getInt("enchantability"),instance2.getInt("knockback_resistance"));
                            }
                            if(instance.getString("type").equals("ccpacks:item_food")) {
                                SerializableData.Instance instance2 = SerializableObjects.foodData.read(jsonObject);
                                CustomFood item = new CustomFood(instance2.getString("namespace"),instance2.getString("id"),instance2.getInt("max_count"),instance2.getInt("hunger"),instance2.getFloat("saturation"),instance2.getBoolean("meat"),instance2.getBoolean("snack"),instance2.getBoolean("always_edible"));
                            }
                            if(instance.getString("type").equals("ccpacks:item_helmet")) {
                                SerializableData.Instance instance2 = SerializableObjects.armorData.read(jsonObject);
                                CustomHelmet item = new CustomHelmet(instance2.getString("namespace"),instance2.getString("id"),instance2.getInt("durability"),instance2.getString("name"),instance2.getInt("protection"),instance2.getInt("toughness"),instance2.getInt("enchantability"),instance2.getInt("knockback_resistance"));
                            }
                            if(instance.getString("type").equals("ccpacks:item_hoe")) {
                                SerializableData.Instance instance2 = SerializableObjects.toolData.read(jsonObject);
                                CustomHoe item = new CustomHoe(instance2.getString("namespace"),instance2.getString( "id"),instance2.getInt( "durability"),instance2.getFloat( "mining_speed_multiplier"),instance2.getInt("attack_speed"),instance2.getInt("mining_level"),instance2.getInt("enchantability"),instance2.getInt("attack_damage"));
                            }
                            if(instance.getString("type").equals("ccpacks:item_leggings")) {
                                SerializableData.Instance instance2 = SerializableObjects.armorData.read(jsonObject);
                                CustomLegging item = new CustomLegging(instance2.getString("namespace"),instance2.getString("id"),instance2.getInt("durability"),instance2.getString("name"),instance2.getInt("protection"),instance2.getInt("toughness"),instance2.getInt("enchantability"),instance2.getInt("knockback_resistance"));
                            }
                            if(instance.getString("type").equals("ccpacks:item_pickaxe")) {
                                SerializableData.Instance instance2 = SerializableObjects.toolData.read(jsonObject);
                                CustomPickaxe item = new CustomPickaxe(instance2.getString("namespace"),instance2.getString( "id"),instance2.getInt( "durability"),instance2.getFloat( "mining_speed_multiplier"),instance2.getInt("attack_speed"),instance2.getInt("mining_level"),instance2.getInt("enchantability"),instance2.getInt("attack_damage"));
                            }
                            if(instance.getString("type").equals("ccpacks:item_shovels")) {
                                SerializableData.Instance instance2 = SerializableObjects.toolData.read(jsonObject);
                                CustomShovel item = new CustomShovel(instance2.getString("namespace"),instance2.getString( "id"),instance2.getInt( "durability"),instance2.getFloat( "mining_speed_multiplier"),instance2.getInt("attack_speed"),instance2.getInt("mining_level"),instance2.getInt("enchantability"),instance2.getInt("attack_damage"));
                            }
                            if(instance.getString("type").equals("ccpacks:item_sword")) {
                                SerializableData.Instance instance2 = SerializableObjects.toolData.read(jsonObject);
                                CustomSword item = new CustomSword(instance2.getString("namespace"),instance2.getString( "id"),instance2.getInt( "durability"),instance2.getFloat( "mining_speed_multiplier"),instance2.getInt("attack_speed"),instance2.getInt("mining_level"),instance2.getInt("enchantability"),instance2.getInt("attack_damage"));
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
