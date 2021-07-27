package io.github.ThatRobin.ccpacks.registries;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.ThatRobin.ccpacks.serializableData.SerializableObjects;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.*;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.Blocks.*;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.Entities.DDChickenEntity;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.Entities.DDCowEntity;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.Entities.DDMushroomCowEntity;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.Entities.DDPigEntity;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.EntityRenderer.DDChickenEntityRenderer;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.EntityRenderer.DDCowEntityRenderer;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.EntityRenderer.DDMushroomCowEntityRenderer;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.EntityRenderer.DDPigEntityRenderer;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.Items.*;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.Particles.DDGlowParticle;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.Particles.DDParticle;
import io.github.apace100.apoli.ApoliClient;
import io.github.apace100.apoli.power.PowerTypeReference;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.fabricmc.loader.api.FabricLoader;
import net.kyrptonaught.customportalapi.CustomPortalApiRegistry;
import net.kyrptonaught.customportalapi.portal.PortalIgnitionSource;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.item.*;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;
import org.lwjgl.glfw.GLFW;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class CCPackServerRegistry {

    private List<Pair<SerializableData.Instance, JsonObject>> list = new ArrayList<>();
    public static final Path DATAPACKS_PATH = FabricLoader.getInstance().getGameDirectory().toPath().resolve("resourcepacks");

    public CCPackServerRegistry() {
        try {
            CCPacksMain.LOGGER.info(DATAPACKS_PATH);
            File[] fileArray = DATAPACKS_PATH.toFile().listFiles();
            CCPacksMain.LOGGER.info("Types to Register:");
            if(fileArray != null) {
                for (int i = 0; i < fileArray.length; i++) {
                    if (fileArray[i].isDirectory()) {
                        readFromDir(fileArray[i], null);
                    } else if (fileArray[i].getName().endsWith(".zip")) {
                        readFromZip(fileArray[i], new ZipFile(fileArray[i]));
                    } else {
                        continue;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        register(list);
    }

    private void register(List<Pair<SerializableData.Instance, JsonObject>> list){
        // Pre-Item and Block Registration
        CCPacksMain.LOGGER.info("Pre-Item/Block Registration:");

        for(int i = 0; i < list.size(); i++) {
            SerializableData.Instance instance = list.get(i).getLeft();
            String type = instance.getString("type");
            JsonObject jsonObject = list.get(i).getRight();
            SerializableData.Instance instance2;
            CCPacksMain.LOGGER.info(type);
            if (type.equals("ccpacks:sound")) {
                instance2 = SerializableObjects.soundEventData.read(jsonObject);

                SoundEvent CUSTOM_SOUND = new DDSound(instance2.getId("identifier"));
                Registry.register(Registry.SOUND_EVENT, instance2.getId("identifier"), CUSTOM_SOUND);
            }
        }

        // Item and Block Registration

        CCPacksMain.LOGGER.info("Item/Block Registration:");
        for(int i = 0; i < list.size(); i++){
            SerializableData.Instance instance = list.get(i).getLeft();
            String type = list.get(i).getLeft().getString("type");
            JsonObject jsonObject = list.get(i).getRight();
            SerializableData.Instance instance2;
            CCPacksMain.LOGGER.info(type);
            if(type.equals("ccpacks:item")) {
                SerializableData.Instance subtype = SerializableObjects.getItemType.read(jsonObject);
                String itemType = subtype.getString("subtype");

                if(itemType.equals("generic")) {
                    instance2 = SerializableObjects.itemData.read(jsonObject);

                    DDItem EXAMPLE_ITEM = new DDItem(new FabricItemSettings().maxCount(instance2.getInt("max_count")).group(ItemGroup.MISC), (List<String>)instance2.get("lore"));
                    Registry.register(Registry.ITEM, instance2.getId("identifier"), EXAMPLE_ITEM);

                } else if (itemType.equals("trinket")) {
                    FabricLoader.getInstance().getModContainer("trinkets").ifPresent(modContainer -> {
                        SerializableData.Instance instance3 = SerializableObjects.itemData.read(jsonObject);

                        DDTrinketItem EXAMPLE_ITEM = new DDTrinketItem(new FabricItemSettings().maxCount(1).group(ItemGroup.MISC), (List<String>) instance3.get("lore"));
                        Registry.register(Registry.ITEM, instance3.getId("identifier"), EXAMPLE_ITEM);
                    });
                } else if (itemType.equals("durable")) {

                    instance2 = SerializableObjects.itemData.read(jsonObject);

                    DDItem EXAMPLE_ITEM = new DDItem(new FabricItemSettings().maxDamage(instance2.getInt("durability")).group(ItemGroup.MISC), (List<String>) instance2.get("lore"));
                    Registry.register(Registry.ITEM, instance2.getId("identifier"), EXAMPLE_ITEM);

                } else if (itemType.equals("sword")) {
                    instance2 = SerializableObjects.toolData.read(jsonObject);

                    DDSwordItem EXAMPLE_ITEM = new DDSwordItem(new DDToolMaterial(instance2.getInt( "durability"), instance2.getFloat( "mining_speed_multiplier"), instance2.getInt("attack_damage"), instance2.getInt("mining_level"), instance2.getInt("enchantability")), instance2.getInt("attack_damage") - 4, instance2.getInt("attack_speed") - 3.3f, new FabricItemSettings().maxCount(1).group(ItemGroup.TOOLS), (List<String>)instance2.get("lore"));
                    Registry.register(Registry.ITEM, instance2.getId("identifier"), EXAMPLE_ITEM);

                } else if (itemType.equals("pickaxe")) {
                    instance2 = SerializableObjects.toolData.read(jsonObject);

                    DDPickaxeItem EXAMPLE_ITEM = new DDPickaxeItem(new DDToolMaterial(instance2.getInt( "durability"), instance2.getFloat( "mining_speed_multiplier"), instance2.getInt("attack_damage"), instance2.getInt("mining_level"), instance2.getInt("enchantability")), instance2.getInt("attack_damage") - 4, instance2.getInt("attack_speed") - 3.3f, new FabricItemSettings().maxCount(1).group(ItemGroup.TOOLS), (List<String>)instance2.get("lore"));
                    Registry.register(Registry.ITEM, instance2.getId("identifier"), EXAMPLE_ITEM);

                } else if (itemType.equals("axe")) {
                    instance2 = SerializableObjects.toolData.read(jsonObject);

                    DDAxeItem EXAMPLE_ITEM = new DDAxeItem(new DDToolMaterial(instance2.getInt("durability"), instance2.getFloat("mining_speed_multiplier"), instance2.getInt("attack_damage"), instance2.getInt("mining_level"), instance2.getInt("enchantability")), instance2.getInt("attack_damage") - 4, instance2.getInt("attack_speed") - 3.3f, new FabricItemSettings().maxCount(1).group(ItemGroup.TOOLS), (List<String>) instance2.get("lore"));
                    Registry.register(Registry.ITEM, instance2.getId("identifier"), EXAMPLE_ITEM);

                } else if (itemType.equals("shovel")) {
                    instance2 = SerializableObjects.toolData.read(jsonObject);

                    DDShovelItem EXAMPLE_ITEM = new DDShovelItem(new DDToolMaterial(instance2.getInt( "durability"), instance2.getFloat( "mining_speed_multiplier"), instance2.getInt("attack_damage"), instance2.getInt("mining_level"), instance2.getInt("enchantability")), instance2.getInt("attack_damage") - 4, instance2.getInt("attack_speed") - 3.3f, new FabricItemSettings().maxCount(1).group(ItemGroup.TOOLS), (List<String>)instance2.get("lore"));
                    Registry.register(Registry.ITEM, instance2.getId("identifier"), EXAMPLE_ITEM);

                } else if (itemType.equals("hoe")) {
                    instance2 = SerializableObjects.toolData.read(jsonObject);

                    DDHoeItem EXAMPLE_ITEM = new DDHoeItem(new DDToolMaterial(instance2.getInt( "durability"), instance2.getFloat( "mining_speed_multiplier"), instance2.getInt("attack_damage"), instance2.getInt("mining_level"), instance2.getInt("enchantability")), instance2.getInt("attack_damage") - 4, instance2.getInt("attack_speed") - 3.3f, new FabricItemSettings().maxCount(1).group(ItemGroup.TOOLS), (List<String>)instance2.get("lore"));
                    Registry.register(Registry.ITEM, instance2.getId("identifier"), EXAMPLE_ITEM);

                } else if (itemType.equals("food")) {
                    instance2 = SerializableObjects.foodData.read(jsonObject);

                    FoodComponent.Builder food = new FoodComponent.Builder().hunger(instance2.getInt("hunger")).saturationModifier(instance2.getFloat("saturation"));
                    if (instance2.getBoolean("meat")) {
                        food.meat();
                    }
                    if (instance2.getBoolean("always_edible")) {
                        food.alwaysEdible();
                    }
                    FoodComponent foodComp = food.build();
                    DDFoodItem EXAMPLE_ITEM = new DDFoodItem(new FabricItemSettings().group(ItemGroup.FOOD).food(foodComp).maxCount(instance2.getInt("max_count")), instance2.getBoolean("drinkable"), (SoundEvent) instance2.get("sound"), (ItemConvertible) instance2.get("returns"), instance2.getInt("eating_time"), (List<String>) instance2.get("lore"));
                    Registry.register(Registry.ITEM, instance2.getId("identifier"), EXAMPLE_ITEM);
                } else if (itemType.equals("helmet")) {
                    instance2 = SerializableObjects.armorData.read(jsonObject);

                    DDArmorMaterial CUSTOM_MATERIAL = new DDArmorMaterial(instance2.getInt("durability"), instance2.getInt("protection"), instance2.getInt("enchantability"), instance2.getInt("toughness"), instance2.getInt("knockback_resistance"), instance2.getString("name"), (Item)instance2.get("repair_item"));
                    DDArmorItem EXAMPLE_ITEM = new DDArmorItem(CUSTOM_MATERIAL, EquipmentSlot.HEAD, new Item.Settings().group(ItemGroup.COMBAT), (List<String>)instance2.get("lore"));
                    Registry.register(Registry.ITEM, instance2.getId("identifier"), EXAMPLE_ITEM);

                } else if (itemType.equals("chestplate")) {
                    instance2 = SerializableObjects.armorData.read(jsonObject);

                    DDArmorMaterial CUSTOM_MATERIAL = new DDArmorMaterial(instance2.getInt("durability"), instance2.getInt("protection"), instance2.getInt("enchantability"), instance2.getInt("toughness"), instance2.getInt("knockback_resistance"), instance2.getString("name"), (Item) instance2.get("repair_item"));
                    DDArmorItem EXAMPLE_ITEM = new DDArmorItem(CUSTOM_MATERIAL, EquipmentSlot.CHEST, new Item.Settings().group(ItemGroup.COMBAT), (List<String>) instance2.get("lore"));
                    Registry.register(Registry.ITEM, instance2.getId("identifier"), EXAMPLE_ITEM);
                } else if (itemType.equals("leggings")) {
                    instance2 = SerializableObjects.armorData.read(jsonObject);

                    DDArmorMaterial CUSTOM_MATERIAL = new DDArmorMaterial(instance2.getInt("durability"), instance2.getInt("protection"), instance2.getInt("enchantability"), instance2.getInt("toughness"), instance2.getInt("knockback_resistance"), instance2.getString("name"), (Item)instance2.get("repair_item"));
                    DDArmorItem EXAMPLE_ITEM = new DDArmorItem(CUSTOM_MATERIAL, EquipmentSlot.LEGS, new Item.Settings().group(ItemGroup.COMBAT), (List<String>)instance2.get("lore"));
                    Registry.register(Registry.ITEM, instance2.getId("identifier"), EXAMPLE_ITEM);

                } else if (itemType.equals("boots")) {
                    instance2 = SerializableObjects.armorData.read(jsonObject);

                    DDArmorMaterial CUSTOM_MATERIAL = new DDArmorMaterial(instance2.getInt("durability"), instance2.getInt("protection"), instance2.getInt("enchantability"), instance2.getInt("toughness"), instance2.getInt("knockback_resistance"), instance2.getString("name"), (Item)instance2.get("repair_item"));
                    DDArmorItem EXAMPLE_ITEM = new DDArmorItem(CUSTOM_MATERIAL, EquipmentSlot.FEET, new Item.Settings().group(ItemGroup.COMBAT), (List<String>)instance2.get("lore"));
                    Registry.register(Registry.ITEM, instance2.getId("identifier"), EXAMPLE_ITEM);

                }else if(itemType.equals("music_disc")) {
                    instance2 = SerializableObjects.musicDiscData.read(jsonObject);

                    Item CUSTOM_MUSIC_DISC = new DDMusicDiscItem(instance2.getInt("comparator_output"), (SoundEvent) instance2.get("sound"), (new Item.Settings().maxCount(1)));
                    CUSTOM_MUSIC_DISC = Registry.register(Registry.ITEM, instance2.getId("identifier"), CUSTOM_MUSIC_DISC);

                }

            } else if (type.equals("ccpacks:block")) {

                SerializableData.Instance subtype = SerializableObjects.getItemType.read(jsonObject);
                String itemType = subtype.getString("subtype");

                if(itemType.equals("generic")) {
                    instance2 = SerializableObjects.blockData.read(jsonObject);

                    Material mat = getMat(instance2.getString("material"));
                    BlockSoundGroup sounds = getSound(instance2.getString("sound"));
                    Tag<Item> tools = getTool(instance2.getString("effective_tool"));
                    FabricBlockSettings blockSettings = FabricBlockSettings.of(mat).breakByTool(tools, instance2.getInt("mining_level")).collidable(instance2.getBoolean("collidable")).strength(instance2.getInt("hardness"), instance2.getInt("resistance")).slipperiness(instance2.getFloat("slipperiness")).luminance(instance2.getInt("luminance")).sounds(sounds).requiresTool().drops(instance2.getId("loot_table"));
                    if(instance2.getBoolean("transparent")){
                        blockSettings.nonOpaque();
                    }
                    DDBlock EXAMPLE_BLOCK = new DDBlock(blockSettings);
                    Registry.register(Registry.BLOCK, instance2.getId("identifier"), EXAMPLE_BLOCK);
                    if (instance2.getBoolean("make_block_item")) {
                        Registry.register(Registry.ITEM, instance2.getId("identifier"), new BlockItem(EXAMPLE_BLOCK, new FabricItemSettings().group(ItemGroup.DECORATIONS)));
                    }
                } else if(itemType.equals("falling")) {
                    instance2 = SerializableObjects.blockData.read(jsonObject);

                    Material mat = getMat(instance2.getString("material"));
                    BlockSoundGroup sounds = getSound(instance2.getString("sound"));
                    Tag<Item> tools = getTool(instance2.getString("effective_tool"));
                    FabricBlockSettings blockSettings = FabricBlockSettings.of(mat).breakByTool(tools, instance2.getInt("mining_level")).collidable(instance2.getBoolean("collidable")).strength(instance2.getInt("hardness"), instance2.getInt("resistance")).slipperiness(instance2.getFloat("slipperiness")).luminance(instance2.getInt("luminance")).sounds(sounds).requiresTool().drops(instance2.getId("loot_table"));
                    if(instance2.getBoolean("transparent")){
                        blockSettings.nonOpaque();
                    }
                    DDFallingBlock EXAMPLE_BLOCK = new DDFallingBlock(blockSettings);
                    Registry.register(Registry.BLOCK, instance2.getId("identifier"), EXAMPLE_BLOCK);
                    if (instance2.getBoolean("make_block_item")) {
                        Registry.register(Registry.ITEM, instance2.getId("identifier"), new BlockItem(EXAMPLE_BLOCK, new FabricItemSettings().group(ItemGroup.DECORATIONS)));
                    }
                } else if(itemType.equals("horizontal_slab")) {
                    instance2 = SerializableObjects.blockData.read(jsonObject);

                    Material mat = getMat(instance2.getString("material"));
                    BlockSoundGroup sounds = getSound(instance2.getString("sound"));
                    Tag<Item> tools = getTool(instance2.getString("effective_tool"));
                    FabricBlockSettings blockSettings = FabricBlockSettings.of(mat).breakByTool(tools, instance2.getInt("mining_level")).collidable(instance2.getBoolean("collidable")).strength(instance2.getInt("hardness"), instance2.getInt("resistance")).slipperiness(instance2.getFloat("slipperiness")).luminance(instance2.getInt("luminance")).sounds(sounds).requiresTool().drops(instance2.getId("loot_table"));
                    if(instance2.getBoolean("transparent")){
                        blockSettings.nonOpaque();
                    }
                    DDHSlabBlock EXAMPLE_BLOCK = new DDHSlabBlock(blockSettings);
                    Registry.register(Registry.BLOCK, instance2.getId("identifier"), EXAMPLE_BLOCK);
                    if (instance2.getBoolean("make_block_item")) {
                        Registry.register(Registry.ITEM, instance2.getId("identifier"), new BlockItem(EXAMPLE_BLOCK, new FabricItemSettings().group(ItemGroup.DECORATIONS)));
                    }
                } else if(itemType.equals("vertical_slab")) {
                    instance2 = SerializableObjects.blockData.read(jsonObject);

                    Material mat = getMat(instance2.getString("material"));
                    BlockSoundGroup sounds = getSound(instance2.getString("sound"));
                    Tag<Item> tools = getTool(instance2.getString("effective_tool"));
                    FabricBlockSettings blockSettings = FabricBlockSettings.of(mat).breakByTool(tools, instance2.getInt("mining_level")).collidable(instance2.getBoolean("collidable")).strength(instance2.getInt("hardness"), instance2.getInt("resistance")).slipperiness(instance2.getFloat("slipperiness")).luminance(instance2.getInt("luminance")).sounds(sounds).requiresTool().drops(instance2.getId("loot_table"));
                    if(instance2.getBoolean("transparent")){
                        blockSettings.nonOpaque();
                    }
                    DDVSlabBlock EXAMPLE_BLOCK = new DDVSlabBlock(blockSettings);
                    Registry.register(Registry.BLOCK, instance2.getId("identifier"), EXAMPLE_BLOCK);
                    if (instance2.getBoolean("make_block_item")) {
                        Registry.register(Registry.ITEM, instance2.getId("identifier"), new BlockItem(EXAMPLE_BLOCK, new FabricItemSettings().group(ItemGroup.DECORATIONS)));
                    }
                } else if(itemType.equals("stairs")) {
                    instance2 = SerializableObjects.stairsData.read(jsonObject);

                    Material mat = getMat(instance2.getString("material"));
                    BlockSoundGroup sounds = getSound(instance2.getString("sound"));
                    BlockState state = (Registry.BLOCK.get(instance2.getId("base_block"))).getDefaultState();
                    Tag<Item> tools = getTool(instance2.getString("effective_tool"));
                    FabricBlockSettings blockSettings = FabricBlockSettings.of(mat).breakByTool(tools, instance2.getInt("mining_level")).collidable(instance2.getBoolean("collidable")).strength(instance2.getInt("hardness"), instance2.getInt("resistance")).slipperiness(instance2.getFloat("slipperiness")).luminance(instance2.getInt("luminance")).sounds(sounds).requiresTool().drops(instance2.getId("loot_table"));
                    if(instance2.getBoolean("transparent")){
                        blockSettings.nonOpaque();
                    }
                    DDStairBlock EXAMPLE_BLOCK = new DDStairBlock(state, blockSettings);
                    Registry.register(Registry.BLOCK, instance2.getId("identifier"), EXAMPLE_BLOCK);
                    if (instance2.getBoolean("make_block_item")) {
                        Registry.register(Registry.ITEM, instance2.getId("identifier"), new BlockItem(EXAMPLE_BLOCK, new FabricItemSettings().group(ItemGroup.DECORATIONS)));
                    }
                }else if (itemType.equals("fence")) {
                    instance2 = SerializableObjects.blockData.read(jsonObject);

                    Material mat = getMat(instance2.getString("material"));
                    BlockSoundGroup sounds = getSound(instance2.getString("sound"));
                    Tag<Item> tools = getTool(instance2.getString("effective_tool"));
                    FabricBlockSettings blockSettings = FabricBlockSettings.of(mat).breakByTool(tools, instance2.getInt("mining_level")).collidable(instance2.getBoolean("collidable")).strength(instance2.getInt("hardness"), instance2.getInt("resistance")).slipperiness(instance2.getFloat("slipperiness")).luminance(instance2.getInt("luminance")).sounds(sounds).requiresTool().drops(instance2.getId("loot_table"));
                    if (instance2.getBoolean("transparent")) {
                        blockSettings.nonOpaque();
                    }
                    DDFenceBlock EXAMPLE_BLOCK = new DDFenceBlock(blockSettings);
                    Registry.register(Registry.BLOCK, instance2.getId("identifier"), EXAMPLE_BLOCK);
                    if (instance2.getBoolean("make_block_item")) {
                        Registry.register(Registry.ITEM, instance2.getId("identifier"), new BlockItem(EXAMPLE_BLOCK, new FabricItemSettings().group(ItemGroup.DECORATIONS)));
                    }
                } else if (itemType.equals("fence_gate")) {
                    instance2 = SerializableObjects.blockData.read(jsonObject);

                    Material mat = getMat(instance2.getString("material"));
                    BlockSoundGroup sounds = getSound(instance2.getString("sound"));
                    Tag<Item> tools = getTool(instance2.getString("effective_tool"));
                    FabricBlockSettings blockSettings = FabricBlockSettings.of(mat).breakByTool(tools, instance2.getInt("mining_level")).collidable(instance2.getBoolean("collidable")).strength(instance2.getInt("hardness"), instance2.getInt("resistance")).slipperiness(instance2.getFloat("slipperiness")).luminance(instance2.getInt("luminance")).sounds(sounds).requiresTool().drops(instance2.getId("loot_table"));
                    if (instance2.getBoolean("transparent")) {
                        blockSettings.nonOpaque();
                    }
                    DDFenceGateBlock EXAMPLE_BLOCK = new DDFenceGateBlock(blockSettings);
                    Registry.register(Registry.BLOCK, instance2.getId("identifier"), EXAMPLE_BLOCK);
                    if (instance2.getBoolean("make_block_item")) {
                        Registry.register(Registry.ITEM, instance2.getId("identifier"), new BlockItem(EXAMPLE_BLOCK, new FabricItemSettings().group(ItemGroup.DECORATIONS)));
                    }
                } else if (itemType.equals("wall")) {
                    instance2 = SerializableObjects.blockData.read(jsonObject);

                    Material mat = getMat(instance2.getString("material"));
                    BlockSoundGroup sounds = getSound(instance2.getString("sound"));
                    Tag<Item> tools = getTool(instance2.getString("effective_tool"));
                    FabricBlockSettings blockSettings = FabricBlockSettings.of(mat).breakByTool(tools, instance2.getInt("mining_level")).collidable(instance2.getBoolean("collidable")).strength(instance2.getInt("hardness"), instance2.getInt("resistance")).slipperiness(instance2.getFloat("slipperiness")).luminance(instance2.getInt("luminance")).sounds(sounds).requiresTool().drops(instance2.getId("loot_table"));
                    if (instance2.getBoolean("transparent")) {
                        blockSettings.nonOpaque();
                    }
                    DDWallBlock EXAMPLE_BLOCK = new DDWallBlock(blockSettings);
                    Registry.register(Registry.BLOCK, instance2.getId("identifier"), EXAMPLE_BLOCK);
                    if (instance2.getBoolean("make_block_item")) {
                        Registry.register(Registry.ITEM, instance2.getId("identifier"), new BlockItem(EXAMPLE_BLOCK, new FabricItemSettings().group(ItemGroup.DECORATIONS)));
                    }
                }
            } else if (type.equals("ccpacks:keybind")) {

                instance2 = SerializableObjects.keybindData.read(jsonObject);

                KeyBinding key = new KeyBinding("key.ccpacks."+instance2.getString("name"), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category." + instance2.getString("category"));
                ApoliClient.registerPowerKeybinding("key.ccpacks."+instance2.getString("name"), key);
                KeyBindingHelper.registerKeyBinding(key);

            } else if (type.equals("ccpacks:status_effect")) {

                instance2 = SerializableObjects.statusEffectData.read(jsonObject);
                StatusEffect effect = new DDStatusEffect(StatusEffectType.NEUTRAL, Integer.parseInt(instance2.getString("color")));
                Registry.register(Registry.STATUS_EFFECT, instance2.getId("identifier"), effect);

            } else if (type.equals("ccpacks:enchantment")) {

                instance2 = SerializableObjects.statusEffectData.read(jsonObject);

                DDEnchantment EXAMPLE_ENCHANTMENT = new DDEnchantment(Enchantment.Rarity.VERY_RARE, EnchantmentTarget.BREAKABLE, null);
                Registry.register(Registry.ENCHANTMENT, instance2.getId("identifier"), EXAMPLE_ENCHANTMENT);
            }
        }

        // Post Item and Block Registration

        CCPacksMain.LOGGER.info("Post Item/Block Registration:");
        for(int i = 0; i < list.size(); i++) {
            String type = list.get(i).getLeft().getString("type");
            CCPacksMain.LOGGER.info(type);
            JsonObject jsonObject = list.get(i).getRight();
            SerializableData.Instance instance2;
            if (type.equals("ccpacks:portal")) {
                instance2 = SerializableObjects.portalData.read(jsonObject);
                CustomPortalApiRegistry.addPortal((Block) instance2.get("block"), PortalIgnitionSource.ItemUseSource((Item) instance2.get("ignition_item")), instance2.getId("dimension"), instance2.getInt("red"), instance2.getInt("green"), instance2.getInt("blue"));
            } else if (type.equals("ccpacks:animal_entity")) {

                SerializableData.Instance entityTypeGot = SerializableObjects.getEntityType.read(jsonObject);
                String entityType = entityTypeGot.getString("subtype");

                if(entityType.equals("mooshroom")) {
                    instance2 = SerializableObjects.mooshroomEntityData.read(jsonObject);

                    EntityType<DDMushroomCowEntity> entity = FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, DDMushroomCowEntity::new).dimensions(EntityDimensions.fixed(0.6F, 1.8F)).build();
                    Registry.register(Registry.ENTITY_TYPE, instance2.getId("identifier"), entity);
                    FabricDefaultAttributeRegistry.register(entity, MooshroomEntity.createMobAttributes());

                    EntityRendererRegistry.INSTANCE.register((EntityType<DDMushroomCowEntity>)(Registry.ENTITY_TYPE.get(instance2.getId("identifier"))), (context) -> new DDMushroomCowEntityRenderer(context, instance2.getId("texture"), instance2.getId("back_item")));
                } else if(entityType.equals("cow")) {
                    instance2 = SerializableObjects.genericEntityData.read(jsonObject);

                    EntityType<DDCowEntity> entity = FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, DDCowEntity::new).dimensions(EntityDimensions.fixed(0.6F, 1.8F)).build();
                    Registry.register(Registry.ENTITY_TYPE, instance2.getId("identifier"), entity);
                    FabricDefaultAttributeRegistry.register(entity, CowEntity.createMobAttributes());

                    EntityRendererRegistry.INSTANCE.register((EntityType<DDCowEntity>)(Registry.ENTITY_TYPE.get(instance2.getId("identifier"))), (context) -> new DDCowEntityRenderer(context, instance2.getId("texture")));
                } else if(entityType.equals("pig")) {
                    instance2 = SerializableObjects.genericEntityData.read(jsonObject);

                    EntityType<DDPigEntity> entity = FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, DDPigEntity::new).dimensions(EntityDimensions.fixed(0.6F, 1.8F)).build();
                    Registry.register(Registry.ENTITY_TYPE, instance2.getId("identifier"), entity);
                    FabricDefaultAttributeRegistry.register(entity, PigEntity.createMobAttributes());

                    EntityRendererRegistry.INSTANCE.register((EntityType<DDPigEntity>)(Registry.ENTITY_TYPE.get(instance2.getId("identifier"))), (context) -> new DDPigEntityRenderer(context, instance2.getId("texture")));
                } else if(entityType.equals("chicken")) {
                    instance2 = SerializableObjects.genericEntityData.read(jsonObject);

                    EntityType<DDChickenEntity> entity = FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, DDChickenEntity::new).dimensions(EntityDimensions.fixed(0.6F, 1.8F)).build();
                    Registry.register(Registry.ENTITY_TYPE, instance2.getId("identifier"), entity);
                    FabricDefaultAttributeRegistry.register(entity, ChickenEntity.createMobAttributes());

                    EntityRendererRegistry.INSTANCE.register((EntityType<DDChickenEntity>)(Registry.ENTITY_TYPE.get(instance2.getId("identifier"))), (context) -> new DDChickenEntityRenderer(context, instance2.getId("texture")));
                }

            } else if(type.equals("ccpacks:particle")) {

                instance2 = SerializableObjects.particleData.read(jsonObject);


                DefaultParticleType TEST = Registry.register(Registry.PARTICLE_TYPE, instance2.getId("identifier"), FabricParticleTypes.simple(true));

                if (instance2.getBoolean("glowing")) {
                    ParticleFactoryRegistry.getInstance().register(TEST, DDGlowParticle.Factory::new);
                } else {
                    ParticleFactoryRegistry.getInstance().register(TEST, DDParticle.Factory::new);
                }
            }

        }
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
        CCPacksMain.LOGGER.info(instance.getString("type"));
        Pair<SerializableData.Instance, JsonObject> pair = new Pair<>(instance, jsonObject);
        list.add(pair);
    }

    private Tag<Item> getTool(String name){
        Tag<Item> tools;
        if(name.equalsIgnoreCase("pickaxe")){
            tools = FabricToolTags.PICKAXES;
        } else if(name.equalsIgnoreCase("axe")){
            tools = FabricToolTags.AXES;
        } else if(name.equalsIgnoreCase("shovel")){
            tools = FabricToolTags.SHOVELS;
        } else if(name.equalsIgnoreCase("hoe")){
            tools = FabricToolTags.HOES;
        } else if(name.equalsIgnoreCase("shears")){
            tools = FabricToolTags.SHEARS;
        } else {
            tools = FabricToolTags.AXES;
        }
        return tools;
    }

    private BlockSoundGroup getSound(String name){
        BlockSoundGroup sound;
        if(name.equalsIgnoreCase("bone")){
            sound = BlockSoundGroup.BONE;
        } else if(name.equalsIgnoreCase("glass")){
            sound = BlockSoundGroup.GLASS;
        } else if(name.equalsIgnoreCase("stone")){
            sound = BlockSoundGroup.STONE;
        } else if(name.equalsIgnoreCase("amethyst")){
            sound = BlockSoundGroup.AMETHYST_BLOCK;
        } else if(name.equalsIgnoreCase("leaves")){
            sound = BlockSoundGroup.WET_GRASS;
        } else if(name.equalsIgnoreCase("wood")){
            sound = BlockSoundGroup.WOOD;
        } else if(name.equalsIgnoreCase("snow_block")){
            sound = BlockSoundGroup.SNOW;
        } else if(name.equalsIgnoreCase("anvil")){
            sound = BlockSoundGroup.ANVIL;
        } else {
            sound = BlockSoundGroup.BONE;
        }
        return sound;
    }

    private Material getMat(String name){
        Material mat;
        if(name.equalsIgnoreCase("air")){
            mat = Material.AIR;
        } else if(name.equalsIgnoreCase("ice")){
            mat = Material.ICE;
        } else if(name.equalsIgnoreCase("stone")){
            mat = Material.STONE;
        } else if(name.equalsIgnoreCase("amethyst")){
            mat = Material.AMETHYST;
        } else if(name.equalsIgnoreCase("leaves")){
            mat = Material.LEAVES;
        } else if(name.equalsIgnoreCase("wood")){
            mat = Material.WOOD;
        } else if(name.equalsIgnoreCase("snow_block")){
            mat = Material.SNOW_BLOCK;
        } else if(name.equalsIgnoreCase("tnt")){
            mat = Material.TNT;
        } else {
            mat = Material.STONE;
        }
        return mat;
    }
}
