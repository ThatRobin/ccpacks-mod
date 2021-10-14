package io.github.ThatRobin.ccpacks.Factories.ContentFactories;

import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.ThatRobin.ccpacks.Registries.CCPacksRegistries;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.Blocks.*;
import io.github.ThatRobin.ccpacks.serializableData.CCPackDataTypes;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.List;
import java.util.function.Supplier;

public class BlockFactories {

    public static Identifier identifier(String string) {
        return new Identifier("block", string);
    }

    @SuppressWarnings("unchecked")
    public static void register() {
        register(new ContentFactory<>(identifier("generic"), Types.BLOCK,
                new SerializableData()
                        .add("material", CCPackDataTypes.MATERIAL)
                        .add("effective_tool", SerializableDataTypes.INGREDIENT_ENTRY)
                        .add("block_sound_group", CCPackDataTypes.BLOCK_SOUND_GROUP)
                        .add("collidable", SerializableDataTypes.BOOLEAN, true)
                        .add("transparent", SerializableDataTypes.BOOLEAN, false)
                        .add("hardness", SerializableDataTypes.INT, 3)
                        .add("slipperiness", SerializableDataTypes.FLOAT, 0.6f)
                        .add("resistance", SerializableDataTypes.INT, 3)
                        .add("luminance", SerializableDataTypes.INT, 0)
                        .add("mining_level", SerializableDataTypes.INT, 1)
                        .add("loot_table", SerializableDataTypes.IDENTIFIER)
                        .add("make_block_item", SerializableDataTypes.BOOLEAN, true)
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroup.FOOD)
                        .add("fuel_tick", SerializableDataTypes.INT, 0),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            data.ifPresent("item_group", settings::group);
                            Material mat = (Material) data.get("material");
                            BlockSoundGroup sounds = (BlockSoundGroup) data.get("block_sound_group");
                            List<Item> tools = (List<Item>) data.get("effective_tool");
                            Tag<Item> tag = ItemTags.BEDS;
                            FabricBlockSettings blockSettings = FabricBlockSettings.of(mat).breakByTool(tag, data.getInt("mining_level")).collidable(data.getBoolean("collidable")).strength(data.getInt("hardness"), data.getInt("resistance")).slipperiness(data.getFloat("slipperiness")).luminance(data.getInt("luminance")).sounds(sounds).requiresTool().drops(data.getId("loot_table"));
                            if (data.getBoolean("transparent")) {
                                blockSettings.nonOpaque();
                            }
                            Supplier<Block> EXAMPLE_BLOCK = () -> new DDBlock(blockSettings, data.getBoolean("make_block_item"));
                            return EXAMPLE_BLOCK;
                        }));

        register(new ContentFactory<>(CCPacksMain.identifier("falling"), Types.BLOCK,
                new SerializableData()
                        .add("material", CCPackDataTypes.MATERIAL)
                        .add("effective_tool", SerializableDataTypes.INGREDIENT_ENTRY)
                        .add("block_sound_group", CCPackDataTypes.BLOCK_SOUND_GROUP)
                        .add("collidable", SerializableDataTypes.BOOLEAN, true)
                        .add("transparent", SerializableDataTypes.BOOLEAN, false)
                        .add("hardness", SerializableDataTypes.INT, 3)
                        .add("slipperiness", SerializableDataTypes.FLOAT, 0.6f)
                        .add("resistance", SerializableDataTypes.INT, 3)
                        .add("luminance", SerializableDataTypes.INT, 0)
                        .add("mining_level", SerializableDataTypes.INT, 1)
                        .add("loot_table", SerializableDataTypes.IDENTIFIER)
                        .add("make_block_item", SerializableDataTypes.BOOLEAN, true)
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroup.FOOD)
                        .add("fuel_tick", SerializableDataTypes.INT, 0),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            data.ifPresent("item_group", settings::group);
                            Material mat = (Material) data.get("material");
                            BlockSoundGroup sounds = (BlockSoundGroup) data.get("block_sound_group");
                            List<Item> tools = (List<Item>) data.get("effective_tool");
                            Tag<Item> tag = ItemTags.BEDS;
                            FabricBlockSettings blockSettings = FabricBlockSettings.of(mat).breakByTool(tag, data.getInt("mining_level")).collidable(data.getBoolean("collidable")).strength(data.getInt("hardness"), data.getInt("resistance")).slipperiness(data.getFloat("slipperiness")).luminance(data.getInt("luminance")).sounds(sounds).requiresTool().drops(data.getId("loot_table"));
                            if (data.getBoolean("transparent")) {
                                blockSettings.nonOpaque();
                            }
                            Supplier<Block> EXAMPLE_BLOCK = () -> new DDFallingBlock(blockSettings, data.getBoolean("make_block_item"));
                            return EXAMPLE_BLOCK;
                        }));

        register(new ContentFactory<>(CCPacksMain.identifier("slab"), Types.BLOCK,
                new SerializableData()
                        .add("material", CCPackDataTypes.MATERIAL)
                        .add("effective_tool", SerializableDataTypes.INGREDIENT_ENTRY)
                        .add("block_sound_group", CCPackDataTypes.BLOCK_SOUND_GROUP)
                        .add("collidable", SerializableDataTypes.BOOLEAN, true)
                        .add("transparent", SerializableDataTypes.BOOLEAN, false)
                        .add("hardness", SerializableDataTypes.INT, 3)
                        .add("slipperiness", SerializableDataTypes.FLOAT, 0.6f)
                        .add("resistance", SerializableDataTypes.INT, 3)
                        .add("luminance", SerializableDataTypes.INT, 0)
                        .add("mining_level", SerializableDataTypes.INT, 1)
                        .add("loot_table", SerializableDataTypes.IDENTIFIER)
                        .add("make_block_item", SerializableDataTypes.BOOLEAN, true)
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroup.FOOD)
                        .add("fuel_tick", SerializableDataTypes.INT, 0),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            data.ifPresent("item_group", settings::group);
                            Material mat = (Material) data.get("material");
                            BlockSoundGroup sounds = (BlockSoundGroup) data.get("block_sound_group");
                            List<Item> tools = (List<Item>) data.get("effective_tool");
                            Tag<Item> tag = ItemTags.BEDS;
                            FabricBlockSettings blockSettings = FabricBlockSettings.of(mat).breakByTool(tag, data.getInt("mining_level")).collidable(data.getBoolean("collidable")).strength(data.getInt("hardness"), data.getInt("resistance")).slipperiness(data.getFloat("slipperiness")).luminance(data.getInt("luminance")).sounds(sounds).requiresTool().drops(data.getId("loot_table"));
                            if (data.getBoolean("transparent")) {
                                blockSettings.nonOpaque();
                            }
                            Supplier<Block> EXAMPLE_BLOCK = () -> new DDSlabBlock(blockSettings, data.getBoolean("make_block_item"));
                            return EXAMPLE_BLOCK;
                        }));

        register(new ContentFactory<>(CCPacksMain.identifier("stairs"), Types.BLOCK,
                new SerializableData()
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
                        .add("make_block_item", SerializableDataTypes.BOOLEAN, true)
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroup.FOOD)
                        .add("fuel_tick", SerializableDataTypes.INT, 0),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            data.ifPresent("item_group", settings::group);
                            Material mat = (Material) data.get("material");
                            BlockSoundGroup sounds = (BlockSoundGroup) data.get("block_sound_group");
                            BlockState state = (Registry.BLOCK.get(data.getId("base_block"))).getDefaultState();
                            List<Item> tools = (List<Item>) data.get("effective_tool");
                            Tag<Item> tag = ItemTags.BEDS;
                            FabricBlockSettings blockSettings = FabricBlockSettings.of(mat).breakByTool(tag, data.getInt("mining_level")).collidable(data.getBoolean("collidable")).strength(data.getInt("hardness"), data.getInt("resistance")).slipperiness(data.getFloat("slipperiness")).luminance(data.getInt("luminance")).sounds(sounds).requiresTool().drops(data.getId("loot_table"));
                            if (data.getBoolean("transparent")) {
                                blockSettings.nonOpaque();
                            }
                            Supplier<Block> EXAMPLE_BLOCK = () -> new DDStairBlock(state, blockSettings, data.getBoolean("make_block_item"));
                            return EXAMPLE_BLOCK;
                        }));

        register(new ContentFactory<>(CCPacksMain.identifier("fence"), Types.BLOCK,
                new SerializableData()
                        .add("material", CCPackDataTypes.MATERIAL)
                        .add("effective_tool", SerializableDataTypes.INGREDIENT_ENTRY)
                        .add("block_sound_group", CCPackDataTypes.BLOCK_SOUND_GROUP)
                        .add("collidable", SerializableDataTypes.BOOLEAN, true)
                        .add("transparent", SerializableDataTypes.BOOLEAN, false)
                        .add("hardness", SerializableDataTypes.INT, 3)
                        .add("slipperiness", SerializableDataTypes.FLOAT, 0.6f)
                        .add("resistance", SerializableDataTypes.INT, 3)
                        .add("luminance", SerializableDataTypes.INT, 0)
                        .add("mining_level", SerializableDataTypes.INT, 1)
                        .add("loot_table", SerializableDataTypes.IDENTIFIER)
                        .add("make_block_item", SerializableDataTypes.BOOLEAN, true)
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroup.FOOD)
                        .add("fuel_tick", SerializableDataTypes.INT, 0),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            data.ifPresent("item_group", settings::group);
                            Material mat = (Material) data.get("material");
                            BlockSoundGroup sounds = (BlockSoundGroup) data.get("block_sound_group");
                            List<Item> tools = (List<Item>) data.get("effective_tool");
                            Tag<Item> tag = ItemTags.BEDS;
                            FabricBlockSettings blockSettings = FabricBlockSettings.of(mat).breakByTool(tag, data.getInt("mining_level")).collidable(data.getBoolean("collidable")).strength(data.getInt("hardness"), data.getInt("resistance")).slipperiness(data.getFloat("slipperiness")).luminance(data.getInt("luminance")).sounds(sounds).requiresTool().drops(data.getId("loot_table"));
                            if (data.getBoolean("transparent")) {
                                blockSettings.nonOpaque();
                            }
                            Supplier<Block> EXAMPLE_BLOCK = () -> new DDFenceBlock(blockSettings, data.getBoolean("make_block_item"));
                            return EXAMPLE_BLOCK;
                        }));

        register(new ContentFactory<>(CCPacksMain.identifier("fence_gate"), Types.BLOCK,
                new SerializableData()
                        .add("material", CCPackDataTypes.MATERIAL)
                        .add("effective_tool", SerializableDataTypes.INGREDIENT_ENTRY)
                        .add("block_sound_group", CCPackDataTypes.BLOCK_SOUND_GROUP)
                        .add("collidable", SerializableDataTypes.BOOLEAN, true)
                        .add("transparent", SerializableDataTypes.BOOLEAN, false)
                        .add("hardness", SerializableDataTypes.INT, 3)
                        .add("slipperiness", SerializableDataTypes.FLOAT, 0.6f)
                        .add("resistance", SerializableDataTypes.INT, 3)
                        .add("luminance", SerializableDataTypes.INT, 0)
                        .add("mining_level", SerializableDataTypes.INT, 1)
                        .add("loot_table", SerializableDataTypes.IDENTIFIER)
                        .add("make_block_item", SerializableDataTypes.BOOLEAN, true)
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroup.FOOD)
                        .add("fuel_tick", SerializableDataTypes.INT, 0),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            data.ifPresent("item_group", settings::group);
                            Material mat = (Material) data.get("material");
                            BlockSoundGroup sounds = (BlockSoundGroup) data.get("block_sound_group");
                            List<Item> tools = (List<Item>) data.get("effective_tool");
                            Tag<Item> tag = ItemTags.BEDS;
                            FabricBlockSettings blockSettings = FabricBlockSettings.of(mat).breakByTool(tag, data.getInt("mining_level")).collidable(data.getBoolean("collidable")).strength(data.getInt("hardness"), data.getInt("resistance")).slipperiness(data.getFloat("slipperiness")).luminance(data.getInt("luminance")).sounds(sounds).requiresTool().drops(data.getId("loot_table"));
                            if (data.getBoolean("transparent")) {
                                blockSettings.nonOpaque();
                            }
                            Supplier<Block> EXAMPLE_BLOCK = () -> new DDFenceGateBlock(blockSettings);
                            return EXAMPLE_BLOCK;
                        }));

        register(new ContentFactory<>(CCPacksMain.identifier("wall"), Types.BLOCK,
                new SerializableData()
                        .add("material", CCPackDataTypes.MATERIAL)
                        .add("effective_tool", SerializableDataTypes.INGREDIENT_ENTRY)
                        .add("block_sound_group", CCPackDataTypes.BLOCK_SOUND_GROUP)
                        .add("collidable", SerializableDataTypes.BOOLEAN, true)
                        .add("transparent", SerializableDataTypes.BOOLEAN, false)
                        .add("hardness", SerializableDataTypes.INT, 3)
                        .add("slipperiness", SerializableDataTypes.FLOAT, 0.6f)
                        .add("resistance", SerializableDataTypes.INT, 3)
                        .add("luminance", SerializableDataTypes.INT, 0)
                        .add("mining_level", SerializableDataTypes.INT, 1)
                        .add("loot_table", SerializableDataTypes.IDENTIFIER)
                        .add("make_block_item", SerializableDataTypes.BOOLEAN, true)
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroup.FOOD)
                        .add("fuel_tick", SerializableDataTypes.INT, 0),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            data.ifPresent("item_group", settings::group);
                            Material mat = (Material) data.get("material");
                            BlockSoundGroup sounds = (BlockSoundGroup) data.get("block_sound_group");
                            List<Item> tools = (List<Item>) data.get("effective_tool");
                            Tag<Item> tag = ItemTags.BEDS;
                            FabricBlockSettings blockSettings = FabricBlockSettings.of(mat).breakByTool(tag, data.getInt("mining_level")).collidable(data.getBoolean("collidable")).strength(data.getInt("hardness"), data.getInt("resistance")).slipperiness(data.getFloat("slipperiness")).luminance(data.getInt("luminance")).sounds(sounds).requiresTool().drops(data.getId("loot_table"));
                            if (data.getBoolean("transparent")) {
                                blockSettings.nonOpaque();
                            }
                            Supplier<Block> EXAMPLE_BLOCK = () -> new DDWallBlock(blockSettings, data.getBoolean("make_block_item"));
                            return EXAMPLE_BLOCK;
                        }));

    }

    private static void register(ContentFactory<Supplier<?>> serializer) {
        Registry.register(CCPacksRegistries.CONTENT_FACTORY, serializer.getSerializerId(), serializer);
    }
}
