package io.github.ThatRobin.ccpacks;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.ThatRobin.ccpacks.SerializableData.SerializableObjects;
import io.github.ThatRobin.ccpacks.Util.PriorityList;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.*;
import io.github.apace100.apoli.ApoliClient;
import io.github.apace100.apoli.power.PowerTypeReference;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.kyrptonaught.customportalapi.CustomPortalApiRegistry;
import net.kyrptonaught.customportalapi.portal.PortalIgnitionSource;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.potion.PotionUtil;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;
import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.core.config.plugins.convert.HexConverter;
import org.lwjgl.glfw.GLFW;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class CCPackRegistry {

    private List<Pair<String, JsonObject>> list = new ArrayList<>();

    public CCPackRegistry() {
        try {
            File[] fileArray = CCPacksMain.DATAPACKS_PATH.toFile().listFiles();
            for(int i = 0; i < fileArray.length; i++){
                if(fileArray[i].isDirectory()) {
                    readFromDir(fileArray[i], null);
                } else if(fileArray[i].getName().endsWith(".zip")) {
                    readFromZip(fileArray[i], new ZipFile(fileArray[i]));
                } else {
                    continue;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        CCPacksMain.LOGGER.info(list);
        register(list);
    }

    private void register(List<Pair<String, JsonObject>> list){

        // Item and Block Registration

        for(int i = 0; i < list.size(); i++){
            String type = list.get(i).getLeft();
            JsonObject jsonObject = list.get(i).getRight();
            SerializableData.Instance instance2;
            if(type.equals("ccpacks:item")) {

                instance2 = SerializableObjects.itemData.read(jsonObject);

                DDItem EXAMPLE_ITEM = new DDItem(new FabricItemSettings().maxCount(instance2.getInt("max_count")).group(ItemGroup.MISC), (List<String>)instance2.get("lore"));
                Registry.register(Registry.ITEM, (Identifier) instance2.get("identifier"), EXAMPLE_ITEM);

            } else if (type.equals("ccpacks:trinket")) {

                instance2 = SerializableObjects.itemData.read(jsonObject);

                DDTrinketItem EXAMPLE_ITEM = new DDTrinketItem(new FabricItemSettings().maxCount(1).group(ItemGroup.MISC), (List<String>)instance2.get("lore"));
                Registry.register(Registry.ITEM, (Identifier) instance2.get("identifier"), EXAMPLE_ITEM);

            } else if (type.equals("ccpacks:durable_item")) {

                instance2 = SerializableObjects.itemData.read(jsonObject);

                DDItem EXAMPLE_ITEM = new DDItem(new FabricItemSettings().maxDamage(instance2.getInt("durability")).group(ItemGroup.MISC), (List<String>) instance2.get("lore"));
                Registry.register(Registry.ITEM, (Identifier) instance2.get("identifier"), EXAMPLE_ITEM);

            } else if (type.equals("ccpacks:axe")) {

                instance2 = SerializableObjects.toolData.read(jsonObject);

                DDAxeItem EXAMPLE_ITEM = new DDAxeItem(new DDToolMaterial(instance2.getInt( "durability"), instance2.getFloat( "mining_speed_multiplier"), instance2.getInt("attack_damage"), instance2.getInt("mining_level"), instance2.getInt("enchantability")), instance2.getInt("attack_damage") - 4, instance2.getInt("attack_speed") - 3.3f, new FabricItemSettings().maxCount(1).group(ItemGroup.TOOLS), (List<String>)instance2.get("lore"));
                Registry.register(Registry.ITEM, (Identifier) instance2.get("identifier"), EXAMPLE_ITEM);

            } else if (type.equals("ccpacks:block")) {

                instance2 = SerializableObjects.blockData.read(jsonObject);

                Material mat = getMat(instance2.getString("material"));
                BlockSoundGroup sounds = getSound(instance2.getString("sound"));
                Tag<Item> tools = getTool(instance2.getString("effective_tool"));
                DDBlock EXAMPLE_BLOCK = new DDBlock(FabricBlockSettings.of(mat).breakByTool(tools, instance2.getInt("mining_level")).collidable(instance2.getBoolean("collidable")).strength(instance2.getInt("hardness"), instance2.getInt("resistance")).slipperiness(instance2.getFloat("slipperiness")).luminance(instance2.getInt("luminance")).sounds(sounds).requiresTool().drops((Identifier) instance2.get("loot_table")), (ActionFactory<Entity>.Instance)instance2.get("action"),(ConditionFactory<LivingEntity>.Instance)instance2.get("condition"));
                Registry.register(Registry.BLOCK, (Identifier) instance2.get("identifier"), EXAMPLE_BLOCK);
                Registry.register(Registry.ITEM, (Identifier) instance2.get("identifier"), new BlockItem(EXAMPLE_BLOCK, new FabricItemSettings().group(ItemGroup.DECORATIONS)));

            } else if (type.equals("ccpacks:falling_block")) {

                instance2 = SerializableObjects.blockData.read(jsonObject);

                Material mat = getMat(instance2.getString("material"));
                BlockSoundGroup sounds = getSound(instance2.getString("sound"));
                Tag<Item> tools = getTool(instance2.getString("effective_tool"));
                DDFallingBlock EXAMPLE_BLOCK = new DDFallingBlock(FabricBlockSettings.of(mat).breakByTool(tools, instance2.getInt("mining_level")).collidable(instance2.getBoolean("collidable")).strength(instance2.getInt("hardness"), instance2.getInt("resistance")).slipperiness(instance2.getFloat("slipperiness")).luminance(instance2.getInt("luminance")).sounds(sounds).requiresTool().drops((Identifier) instance2.get("loot_table")), (ActionFactory<Entity>.Instance)instance2.get("action"),(ConditionFactory<LivingEntity>.Instance)instance2.get("condition"));
                Registry.register(Registry.BLOCK, (Identifier) instance2.get("identifier"), EXAMPLE_BLOCK);
                Registry.register(Registry.ITEM, (Identifier) instance2.get("identifier"), new BlockItem(EXAMPLE_BLOCK, new FabricItemSettings().group(ItemGroup.DECORATIONS)));

            } else if (type.equals("ccpacks:vertical_slab")) {

                instance2 = SerializableObjects.blockData.read(jsonObject);

                Material mat = getMat(instance2.getString("material"));
                BlockSoundGroup sounds = getSound(instance2.getString("sound"));
                Tag<Item> tools = getTool(instance2.getString("effective_tool"));
                DDVSlabBlock EXAMPLE_BLOCK = new DDVSlabBlock(FabricBlockSettings.of(mat).breakByTool(tools, instance2.getInt("mining_level")).collidable(instance2.getBoolean("collidable")).strength(instance2.getInt("hardness"), instance2.getInt("resistance")).slipperiness(instance2.getFloat("slipperiness")).luminance(instance2.getInt("luminance")).sounds(sounds).requiresTool().drops((Identifier) instance2.get("loot_table")), (ActionFactory<Entity>.Instance)instance2.get("action"),(ConditionFactory<LivingEntity>.Instance)instance2.get("condition"));
                Registry.register(Registry.BLOCK, (Identifier) instance2.get("identifier"), EXAMPLE_BLOCK);
                Registry.register(Registry.ITEM, (Identifier) instance2.get("identifier"), new BlockItem(EXAMPLE_BLOCK, new FabricItemSettings().group(ItemGroup.DECORATIONS)));

            } else if (type.equals("ccpacks:horizontal_slab")) {

                instance2 = SerializableObjects.blockData.read(jsonObject);

                Material mat = getMat(instance2.getString("material"));
                BlockSoundGroup sounds = getSound(instance2.getString("sound"));
                Tag<Item> tools = getTool(instance2.getString("effective_tool"));
                DDHSlabBlock EXAMPLE_BLOCK = new DDHSlabBlock(FabricBlockSettings.of(mat).breakByTool(tools, instance2.getInt("mining_level")).collidable(instance2.getBoolean("collidable")).strength(instance2.getInt("hardness"), instance2.getInt("resistance")).slipperiness(instance2.getFloat("slipperiness")).luminance(instance2.getInt("luminance")).sounds(sounds).requiresTool().drops((Identifier) instance2.get("loot_table")), (ActionFactory<Entity>.Instance)instance2.get("action"),(ConditionFactory<LivingEntity>.Instance)instance2.get("condition"));
                Registry.register(Registry.BLOCK, (Identifier) instance2.get("identifier"), EXAMPLE_BLOCK);
                Registry.register(Registry.ITEM, (Identifier) instance2.get("identifier"), new BlockItem(EXAMPLE_BLOCK, new FabricItemSettings().group(ItemGroup.DECORATIONS)));

            } else if (type.equals("ccpacks:stairs")) {

                instance2 = SerializableObjects.stairsData.read(jsonObject);

                Material mat = getMat(instance2.getString("material"));
                BlockSoundGroup sounds = getSound(instance2.getString("sound"));
                BlockState state = (Registry.BLOCK.get((Identifier) instance2.get("base_block"))).getDefaultState();
                Tag<Item> tools = getTool(instance2.getString("effective_tool"));
                DDStairBlock EXAMPLE_BLOCK = new DDStairBlock(state, FabricBlockSettings.of(mat).breakByTool(tools, instance2.getInt("mining_level")).collidable(instance2.getBoolean("collidable")).strength(instance2.getInt("hardness"), instance2.getInt("resistance")).slipperiness(instance2.getFloat("slipperiness")).luminance(instance2.getInt("luminance")).sounds(sounds).requiresTool().drops((Identifier) instance2.get("loot_table")), (ActionFactory<Entity>.Instance)instance2.get("action"),(ConditionFactory<LivingEntity>.Instance)instance2.get("condition"));
                Registry.register(Registry.BLOCK, (Identifier) instance2.get("identifier"), EXAMPLE_BLOCK);
                Registry.register(Registry.ITEM, (Identifier) instance2.get("identifier"), new BlockItem(EXAMPLE_BLOCK, new FabricItemSettings().group(ItemGroup.DECORATIONS)));

            } else if (type.equals("ccpacks:boots")) {

                instance2 = SerializableObjects.armorData.read(jsonObject);

                DDArmorMaterial CUSTOM_MATERIAL = new DDArmorMaterial(instance2.getInt("durability"), instance2.getInt("protection"), instance2.getInt("enchantability"), instance2.getInt("toughness"), instance2.getInt("knockback_resistance"), instance2.getString("name"), (Item)instance2.get("repair_item"));
                DDArmorItem EXAMPLE_ITEM = new DDArmorItem(CUSTOM_MATERIAL, EquipmentSlot.FEET, new Item.Settings().group(ItemGroup.COMBAT), (List<String>)instance2.get("lore"));
                Registry.register(Registry.ITEM, (Identifier) instance2.get("identifier"), EXAMPLE_ITEM);

            } else if (type.equals("ccpacks:chestplate")) {

                instance2 = SerializableObjects.armorData.read(jsonObject);

                DDArmorMaterial CUSTOM_MATERIAL = new DDArmorMaterial(instance2.getInt("durability"), instance2.getInt("protection"), instance2.getInt("enchantability"), instance2.getInt("toughness"), instance2.getInt("knockback_resistance"), instance2.getString("name"), (Item)instance2.get("repair_item"));
                DDArmorItem EXAMPLE_ITEM = new DDArmorItem(CUSTOM_MATERIAL, EquipmentSlot.CHEST, new Item.Settings().group(ItemGroup.COMBAT), (List<String>)instance2.get("lore"));
                Registry.register(Registry.ITEM, (Identifier) instance2.get("identifier"), EXAMPLE_ITEM);

            } else if (type.equals("ccpacks:food")) {

                instance2 = SerializableObjects.foodData.read(jsonObject);

                FoodComponent.Builder food = new FoodComponent.Builder().hunger(instance2.getInt("hunger")).saturationModifier(instance2.getFloat("saturation"));
                if(instance2.getBoolean("meat")) {
                    food.meat();
                }
                if(instance2.getBoolean("snack")){
                    food.snack();
                }
                if(instance2.getBoolean("always_edible")){
                    food.alwaysEdible();
                }
                FoodComponent foodComp = food.build();
                Item EXAMPLE_ITEM = new Item(new FabricItemSettings().group(ItemGroup.FOOD).maxCount(instance2.getInt("max_count")).food(foodComp));
                Registry.register(Registry.ITEM, (Identifier) instance2.get("identifier"), EXAMPLE_ITEM);

            } else if (type.equals("ccpacks:helmet")) {

                instance2 = SerializableObjects.armorData.read(jsonObject);

                DDArmorMaterial CUSTOM_MATERIAL = new DDArmorMaterial(instance2.getInt("durability"), instance2.getInt("protection"), instance2.getInt("enchantability"), instance2.getInt("toughness"), instance2.getInt("knockback_resistance"), instance2.getString("name"), (Item)instance2.get("repair_item"));
                DDArmorItem EXAMPLE_ITEM = new DDArmorItem(CUSTOM_MATERIAL, EquipmentSlot.HEAD, new Item.Settings().group(ItemGroup.COMBAT), (List<String>)instance2.get("lore"));
                Registry.register(Registry.ITEM, (Identifier) instance2.get("identifier"), EXAMPLE_ITEM);

            } else if (type.equals("ccpacks:hoe")) {

                instance2 = SerializableObjects.toolData.read(jsonObject);

                DDHoeItem EXAMPLE_ITEM = new DDHoeItem(new DDToolMaterial(instance2.getInt( "durability"), instance2.getFloat( "mining_speed_multiplier"), instance2.getInt("attack_damage"), instance2.getInt("mining_level"), instance2.getInt("enchantability")), instance2.getInt("attack_damage") - 4, instance2.getInt("attack_speed") - 3.3f, new FabricItemSettings().maxCount(1).group(ItemGroup.TOOLS), (List<String>)instance2.get("lore"));
                Registry.register(Registry.ITEM, (Identifier) instance2.get("identifier"), EXAMPLE_ITEM);

            } else if (type.equals("ccpacks:leggings")) {

                instance2 = SerializableObjects.armorData.read(jsonObject);

                DDArmorMaterial CUSTOM_MATERIAL = new DDArmorMaterial(instance2.getInt("durability"), instance2.getInt("protection"), instance2.getInt("enchantability"), instance2.getInt("toughness"), instance2.getInt("knockback_resistance"), instance2.getString("name"), (Item)instance2.get("repair_item"));
                DDArmorItem EXAMPLE_ITEM = new DDArmorItem(CUSTOM_MATERIAL, EquipmentSlot.LEGS, new Item.Settings().group(ItemGroup.COMBAT), (List<String>)instance2.get("lore"));
                Registry.register(Registry.ITEM, (Identifier) instance2.get("identifier"), EXAMPLE_ITEM);

            } else if (type.equals("ccpacks:pickaxe")) {

                instance2 = SerializableObjects.toolData.read(jsonObject);

                DDPickaxeItem EXAMPLE_ITEM = new DDPickaxeItem(new DDToolMaterial(instance2.getInt( "durability"), instance2.getFloat( "mining_speed_multiplier"), instance2.getInt("attack_damage"), instance2.getInt("mining_level"), instance2.getInt("enchantability")), instance2.getInt("attack_damage") - 4, instance2.getInt("attack_speed") - 3.3f, new FabricItemSettings().maxCount(1).group(ItemGroup.TOOLS), (List<String>)instance2.get("lore"));
                Registry.register(Registry.ITEM, (Identifier) instance2.get("identifier"), EXAMPLE_ITEM);

            } else if (type.equals("ccpacks:shovels")) {

                instance2 = SerializableObjects.toolData.read(jsonObject);

                DDShovelItem EXAMPLE_ITEM = new DDShovelItem(new DDToolMaterial(instance2.getInt( "durability"), instance2.getFloat( "mining_speed_multiplier"), instance2.getInt("attack_damage"), instance2.getInt("mining_level"), instance2.getInt("enchantability")), instance2.getInt("attack_damage") - 4, instance2.getInt("attack_speed") - 3.3f, new FabricItemSettings().maxCount(1).group(ItemGroup.TOOLS), (List<String>)instance2.get("lore"));
                Registry.register(Registry.ITEM, (Identifier) instance2.get("identifier"), EXAMPLE_ITEM);

            } else if (type.equals("ccpacks:sword")) {
                instance2 = SerializableObjects.toolData.read(jsonObject);

                DDSwordItem EXAMPLE_ITEM = new DDSwordItem(new DDToolMaterial(instance2.getInt( "durability"), instance2.getFloat( "mining_speed_multiplier"), instance2.getInt("attack_damage"), instance2.getInt("mining_level"), instance2.getInt("enchantability")), instance2.getInt("attack_damage") - 4, instance2.getInt("attack_speed") - 3.3f, new FabricItemSettings().maxCount(1).group(ItemGroup.TOOLS), (List<String>)instance2.get("lore"));
                Registry.register(Registry.ITEM, (Identifier) instance2.get("identifier"), EXAMPLE_ITEM);

            } else if (type.equals("ccpacks:keybind")) {

                instance2 = SerializableObjects.keybindData.read(jsonObject);

                KeyBinding key = new KeyBinding("key.ccpacks."+instance2.getString("name"), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category." + instance2.getString("category"));
                ApoliClient.registerPowerKeybinding("key.ccpacks."+instance2.getString("name"), key);
                KeyBindingHelper.registerKeyBinding(key);

            } else if (type.equals("ccpacks:status_effect")) {

                instance2 = SerializableObjects.statusEffectData.read(jsonObject);
                StatusEffect effect = new DDStatusEffect(StatusEffectType.NEUTRAL, Integer.parseInt(instance2.getString("color")));
                Registry.register(Registry.STATUS_EFFECT, (Identifier) instance2.get("identifier"), effect);

            } else if (type.equals("ccpacks:enchantment")) {

                instance2 = SerializableObjects.statusEffectData.read(jsonObject);

                DDEnchantment EXAMPLE_ENCHANTMENT = new DDEnchantment(Enchantment.Rarity.VERY_RARE, EnchantmentTarget.BREAKABLE, null);
                Registry.register(Registry.ENCHANTMENT, (Identifier) instance2.get("identifier"), EXAMPLE_ENCHANTMENT);
            }
        }

        //Post Item and Block Registration
        List<PowerTypeReference> powers = new ArrayList<>();
        for(int i = 0; i < list.size(); i++) {
            String type = list.get(i).getLeft();
            JsonObject jsonObject = list.get(i).getRight();
            SerializableData.Instance instance2;
            if (type.equals("ccpacks:portal")) {
                instance2 = SerializableObjects.portalData.read(jsonObject);
                CustomPortalApiRegistry.addPortal((Block) instance2.get("block"), PortalIgnitionSource.ItemUseSource((Item) instance2.get("ignition_item")), (Identifier) instance2.get("dimension"), instance2.getInt("red"), instance2.getInt("green"), instance2.getInt("blue"));
            } else if (type.equals("ccpacks:universal_powers")) {

                instance2 = SerializableObjects.universalPowerData.read(jsonObject);

                for(int e = 0; e < ((List<PowerTypeReference>)instance2.get("powers")).size(); e++){
                    powers.add(((List<PowerTypeReference>)instance2.get("powers")).get(e));
                }
            }
        }


        StatusEffect effect = new DDUniversalPowers(StatusEffectType.NEUTRAL,0x000000, powers, new Identifier("ccpacks", "universal_powers"));
        Registry.register(Registry.STATUS_EFFECT, new Identifier("ccpacks", "universal_powers"), effect);
    }

    public void readFromDir(File base, ZipFile zipFile) throws IOException {
        String string2 = "ccdata/";
        File pack = new File(base, "ccdata");
        try (Stream<Path> paths = Files.walk(Paths.get(pack.getPath()))) {
            paths.forEach((file) -> {
                String string3 = file.toString();
                if (string3.endsWith(".json")) {
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
                    registerElements(jsonObject, instance);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ZipFile getZipFile(File base, ZipFile zipFile) throws IOException {
        if (zipFile == null) {
            zipFile = new ZipFile(base);
        }
        return zipFile;
    }

    public void readFromZip(File base, ZipFile zipFile) throws IOException {
        ZipFile zipFile2 = this.getZipFile(base, zipFile);
        Enumeration<? extends ZipEntry> enumeration = zipFile2.entries();
        String string2 = "ccdata/";
        while(enumeration.hasMoreElements()) {
            ZipEntry zipEntry = enumeration.nextElement();
            if (!zipEntry.isDirectory()) {
                String string3 = zipEntry.getName();
                if (string3.endsWith(".json") && string3.startsWith(string2)) {
                    InputStream stream = zipFile2.getInputStream(zipEntry);
                    JsonParser jsonParser = new JsonParser();
                    JsonObject jsonObject = null;
                    try {
                        jsonObject = (JsonObject)jsonParser.parse(new InputStreamReader(stream, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    SerializableData.Instance instance = SerializableObjects.getItemType.read(jsonObject);
                    registerElements(jsonObject, instance);
                }
            }
        }
    }

    public void registerElements(JsonObject jsonObject, SerializableData.Instance instance){
        Pair<String, JsonObject> pair = new Pair<>(instance.getString("type"), jsonObject);
        list.add(pair);
    }

    private Tag<Item> getTool(String name){
        Tag<Item> tools = null;
        if(name.toLowerCase().equals("pickaxe")){
            tools = FabricToolTags.PICKAXES;
        } else if(name.toLowerCase().equals("axe")){
            tools = FabricToolTags.AXES;
        } else if(name.toLowerCase().equals("shovel")){
            tools = FabricToolTags.SHOVELS;
        } else if(name.toLowerCase().equals("hoe")){
            tools = FabricToolTags.HOES;
        } else if(name.toLowerCase().equals("shears")){
            tools = FabricToolTags.SHEARS;
        } else {
            tools = FabricToolTags.AXES;
        }
        return tools;
    }

    private BlockSoundGroup getSound(String name){
        BlockSoundGroup sound = null;
        if(name.toLowerCase().equals("bone")){
            sound = BlockSoundGroup.BONE;
        } else if(name.toLowerCase().equals("glass")){
            sound = BlockSoundGroup.GLASS;
        } else if(name.toLowerCase().equals("stone")){
            sound = BlockSoundGroup.STONE;
        } else if(name.toLowerCase().equals("amethyst")){
            sound = BlockSoundGroup.AMETHYST_BLOCK;
        } else if(name.toLowerCase().equals("leaves")){
            sound = BlockSoundGroup.WET_GRASS;
        } else if(name.toLowerCase().equals("wood")){
            sound = BlockSoundGroup.WOOD;
        } else if(name.toLowerCase().equals("snow_block")){
            sound = BlockSoundGroup.SNOW;
        } else if(name.toLowerCase().equals("anvil")){
            sound = BlockSoundGroup.ANVIL;
        } else {
            sound = BlockSoundGroup.BONE;
        }
        return sound;
    }

    private Material getMat(String name){
        Material mat = null;
        if(name.toLowerCase().equals("air")){
            mat = Material.AIR;
        } else if(name.toLowerCase().equals("ice")){
            mat = Material.ICE;
        } else if(name.toLowerCase().equals("stone")){
            mat = Material.STONE;
        } else if(name.toLowerCase().equals("amethyst")){
            mat = Material.AMETHYST;
        } else if(name.toLowerCase().equals("leaves")){
            mat = Material.LEAVES;
        } else if(name.toLowerCase().equals("wood")){
            mat = Material.WOOD;
        } else if(name.toLowerCase().equals("snow_block")){
            mat = Material.SNOW_BLOCK;
        } else if(name.toLowerCase().equals("tnt")){
            mat = Material.TNT;
        } else {
            mat = Material.STONE;
        }
        return mat;
    }
}
