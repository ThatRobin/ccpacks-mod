package io.github.thatrobin.ccpacks.factories.content_factories;

import io.github.thatrobin.ccpacks.CCPackDataTypes;
import io.github.thatrobin.ccpacks.data_driven_classes.blocks.*;
import io.github.thatrobin.ccpacks.data_driven_classes.items.DDBlockItem;
import io.github.thatrobin.ccpacks.factories.mechanic_factories.MechanicTypeReference;
import io.github.thatrobin.ccpacks.registries.CCPacksRegistries;
import io.github.thatrobin.ccpacks.util.*;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.List;
import java.util.function.Supplier;

public class BlockFactories {

    public static Identifier identifier(String string) {
        return new Identifier("block", string);
    }

    public static void register() {
        register(new ContentFactory<>(identifier("generic"), Types.BLOCK,
                new SerializableData()
                        .add("material", CCPackDataTypes.MATERIAL)
                        .add("mechanics", CCPackDataTypes.MECHANIC_TYPES, null)
                        .add("render_layer", CCPackDataTypes.RENDER_LAYER, RenderLayerTypes.SOLID)
                        .add("effective_tool", CCPackDataTypes.TOOL_TYPES, ToolTypes.PICKAXES)
                        .add("block_sound_group", CCPackDataTypes.BLOCK_SOUND_GROUP)
                        .add("collidable", SerializableDataTypes.BOOLEAN, true)
                        .add("transparent", SerializableDataTypes.BOOLEAN, true)
                        .add("hardness", SerializableDataTypes.INT, 3)
                        .add("slipperiness", SerializableDataTypes.FLOAT, 0.6f)
                        .add("resistance", SerializableDataTypes.INT, 3)
                        .add("luminance", SerializableDataTypes.INT, 0)
                        .add("mining_level", SerializableDataTypes.INT, 1)
                        .add("loot_table", SerializableDataTypes.IDENTIFIER)
                        .add("block_item", CCPackDataTypes.ITEM, null)
                        .add("block_states", CCPackDataTypes.BLOCK_STATES, null),
                data ->
                        (contentType, content) -> {
                            Material mat = (Material) data.get("material");
                            BlockSoundGroup sounds = (BlockSoundGroup) data.get("block_sound_group");
                            ToolTypes tools = (ToolTypes) data.get("effective_tool");
                            FabricBlockSettings blockSettings;
                            if(data.getBoolean("transparent")) {
                                blockSettings = FabricBlockSettings.of(mat).collidable(data.getBoolean("collidable")).strength(data.getInt("hardness"), data.getInt("resistance")).slipperiness(data.getFloat("slipperiness")).luminance(data.getInt("luminance")).sounds(sounds).requiresTool().drops(data.getId("loot_table")).nonOpaque();
                            } else {
                                blockSettings = FabricBlockSettings.of(mat).collidable(data.getBoolean("collidable")).strength(data.getInt("hardness"), data.getInt("resistance")).slipperiness(data.getFloat("slipperiness")).luminance(data.getInt("luminance")).sounds(sounds).requiresTool().drops(data.getId("loot_table"));
                            }
                            DDBlock block;
                            List<VoxelInfo> voxelInfo = ((List<VoxelInfo>) data.get("block_states"));
                            List<MechanicTypeReference> mechanicTypeReferences = (List<MechanicTypeReference>) data.get("mechanics");
                            block = new DDBlock(blockSettings, mechanicTypeReferences, voxelInfo);
                            if(data.isPresent("block_item")) {
                                BlockItemHolder itemHolder = (BlockItemHolder)data.get("block_item");
                                DDBlockItem item = new DDBlockItem(block, itemHolder.settings, itemHolder.name, itemHolder.lore, itemHolder.item_modifiers);
                                FuelRegistry.INSTANCE.add(item,  itemHolder.fuel_tick);
                                Registry.register(Registry.ITEM, contentType.getIdentifier(), item);
                            }
                            BlockRenderLayerMap.INSTANCE.putBlock(block, ((RenderLayerTypes)data.get("render_layer")).renderLayer);
                            return (Supplier<Block>) () -> block;
                        }));

        register(new ContentFactory<>(identifier("cable"), Types.BLOCK,
                new SerializableData()
                        .add("connects_to", SerializableDataTypes.BLOCK_TAG, null)
                        .add("material", CCPackDataTypes.MATERIAL)
                        .add("mechanics", CCPackDataTypes.MECHANIC_TYPES, null)
                        .add("render_layer", CCPackDataTypes.RENDER_LAYER, RenderLayerTypes.SOLID)
                        .add("effective_tool", CCPackDataTypes.TOOL_TYPES, ToolTypes.PICKAXES)
                        .add("block_sound_group", CCPackDataTypes.BLOCK_SOUND_GROUP)
                        .add("collidable", SerializableDataTypes.BOOLEAN, true)
                        .add("transparent", SerializableDataTypes.BOOLEAN, true)
                        .add("hardness", SerializableDataTypes.INT, 3)
                        .add("slipperiness", SerializableDataTypes.FLOAT, 0.6f)
                        .add("resistance", SerializableDataTypes.INT, 3)
                        .add("luminance", SerializableDataTypes.INT, 0)
                        .add("mining_level", SerializableDataTypes.INT, 1)
                        .add("loot_table", SerializableDataTypes.IDENTIFIER, null)
                        .add("block_item", CCPackDataTypes.ITEM, null)
                        .add("block_states", CCPackDataTypes.BLOCK_STATES, null)
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroups.MISC)
                        .add("fuel_tick", SerializableDataTypes.INT, 0),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            ItemGroups group = (ItemGroups) data.get("item_group");
                            settings.group(group.group);
                            Material mat = (Material) data.get("material");
                            BlockSoundGroup sounds = (BlockSoundGroup) data.get("block_sound_group");
                            ToolTypes tools = (ToolTypes) data.get("effective_tool");
                            FabricBlockSettings blockSettings;
                            if(data.getBoolean("transparent")) {
                                blockSettings = FabricBlockSettings.of(mat).collidable(data.getBoolean("collidable")).strength(data.getInt("hardness"), data.getInt("resistance")).slipperiness(data.getFloat("slipperiness")).luminance(data.getInt("luminance")).sounds(sounds).requiresTool().drops(data.getId("loot_table")).nonOpaque();
                            } else {
                                blockSettings = FabricBlockSettings.of(mat).collidable(data.getBoolean("collidable")).strength(data.getInt("hardness"), data.getInt("resistance")).slipperiness(data.getFloat("slipperiness")).luminance(data.getInt("luminance")).sounds(sounds).requiresTool().drops(data.getId("loot_table"));
                            }
                            DDCableBlock block;
                            List<VoxelInfo> voxelInfo = ((List<VoxelInfo>) data.get("block_states"));
                            List<MechanicTypeReference> mechanicTypeReferences = (List<MechanicTypeReference>) data.get("mechanics");
                            block = new DDCableBlock(blockSettings, mechanicTypeReferences, voxelInfo, (Tag<Block>) data.get("connects_to"));
                            if(data.isPresent("block_item")) {
                                BlockItemHolder itemHolder = (BlockItemHolder)data.get("block_item");
                                DDBlockItem item = new DDBlockItem(block, itemHolder.settings, itemHolder.name, itemHolder.lore, itemHolder.item_modifiers);
                                FuelRegistry.INSTANCE.add(item,  itemHolder.fuel_tick);
                                Registry.register(Registry.ITEM, contentType.getIdentifier(), item);
                            }
                            BlockRenderLayerMap.INSTANCE.putBlock(block, ((RenderLayerTypes)data.get("render_layer")).renderLayer);
                            return (Supplier<Block>) () -> block;
                        }));

        register(new ContentFactory<>(identifier("falling"), Types.BLOCK,
                new SerializableData()
                        .add("material", CCPackDataTypes.MATERIAL)
                        .add("effective_tool", CCPackDataTypes.TOOL_TYPES, ToolTypes.PICKAXES)
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
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroups.MISC)
                        .add("fuel_tick", SerializableDataTypes.INT, 0),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            ItemGroups group = (ItemGroups) data.get("item_group");
                            settings.group(group.group);
                            Material mat = (Material) data.get("material");
                            BlockSoundGroup sounds = (BlockSoundGroup) data.get("block_sound_group");
                            ToolTypes tools = (ToolTypes) data.get("effective_tool");
                            FabricBlockSettings blockSettings = FabricBlockSettings.of(mat).collidable(data.getBoolean("collidable")).strength(data.getInt("hardness"), data.getInt("resistance")).slipperiness(data.getFloat("slipperiness")).luminance(data.getInt("luminance")).sounds(sounds).requiresTool().drops(data.getId("loot_table"));
                            if (data.getBoolean("transparent")) {
                                blockSettings.nonOpaque();
                            }
                            return (Supplier<Block>) () -> new DDFallingBlock(blockSettings, data.getBoolean("make_block_item"), data.getInt("fuel_tick"));
                        }));

        register(new ContentFactory<>(identifier("slab"), Types.BLOCK,
                new SerializableData()
                        .add("material", CCPackDataTypes.MATERIAL)
                        .add("effective_tool", CCPackDataTypes.TOOL_TYPES, ToolTypes.PICKAXES)
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
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroups.MISC)
                        .add("fuel_tick", SerializableDataTypes.INT, 0),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            ItemGroups group = (ItemGroups) data.get("item_group");
                            settings.group(group.group);
                            Material mat = (Material) data.get("material");
                            BlockSoundGroup sounds = (BlockSoundGroup) data.get("block_sound_group");
                            ToolTypes tools = (ToolTypes) data.get("effective_tool");
                            FabricBlockSettings blockSettings = FabricBlockSettings.of(mat).collidable(data.getBoolean("collidable")).strength(data.getInt("hardness"), data.getInt("resistance")).slipperiness(data.getFloat("slipperiness")).luminance(data.getInt("luminance")).sounds(sounds).requiresTool().drops(data.getId("loot_table"));
                            if (data.getBoolean("transparent")) {
                                blockSettings.nonOpaque();
                            }
                            return (Supplier<Block>) () -> new DDSlabBlock(blockSettings, data.getBoolean("make_block_item"), data.getInt("fuel_tick"));
                        }));

        register(new ContentFactory<>(identifier("stairs"), Types.BLOCK,
                new SerializableData()
                        .add("material", CCPackDataTypes.MATERIAL)
                        .add("effective_tool", CCPackDataTypes.TOOL_TYPES, ToolTypes.PICKAXES)
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
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroups.MISC)
                        .add("fuel_tick", SerializableDataTypes.INT, 0),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            ItemGroups group = (ItemGroups) data.get("item_group");
                            settings.group(group.group);
                            Material mat = (Material) data.get("material");
                            BlockSoundGroup sounds = (BlockSoundGroup) data.get("block_sound_group");
                            BlockState state = Blocks.OAK_PLANKS.getDefaultState();
                            ToolTypes tools = (ToolTypes) data.get("effective_tool");
                            FabricBlockSettings blockSettings = FabricBlockSettings.of(mat).collidable(data.getBoolean("collidable")).strength(data.getInt("hardness"), data.getInt("resistance")).slipperiness(data.getFloat("slipperiness")).luminance(data.getInt("luminance")).sounds(sounds).requiresTool().drops(data.getId("loot_table"));
                            if (data.getBoolean("transparent")) {
                                blockSettings.nonOpaque();
                            }
                            return (Supplier<Block>) () -> new DDStairBlock(state, blockSettings, data.getBoolean("make_block_item"), data.getInt("fuel_tick"));
                        }));

        register(new ContentFactory<>(identifier("fence"), Types.BLOCK,
                new SerializableData()
                        .add("material", CCPackDataTypes.MATERIAL)
                        .add("effective_tool", CCPackDataTypes.TOOL_TYPES, ToolTypes.PICKAXES)
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
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroups.MISC)
                        .add("fuel_tick", SerializableDataTypes.INT, 0),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            ItemGroups group = (ItemGroups) data.get("item_group");
                            settings.group(group.group);
                            Material mat = (Material) data.get("material");
                            BlockSoundGroup sounds = (BlockSoundGroup) data.get("block_sound_group");
                            ToolTypes tools = (ToolTypes) data.get("effective_tool");
                            FabricBlockSettings blockSettings = FabricBlockSettings.of(mat).collidable(data.getBoolean("collidable")).strength(data.getInt("hardness"), data.getInt("resistance")).slipperiness(data.getFloat("slipperiness")).luminance(data.getInt("luminance")).sounds(sounds).requiresTool().drops(data.getId("loot_table"));
                            if (data.getBoolean("transparent")) {
                                blockSettings.nonOpaque();
                            }
                            return (Supplier<Block>) () -> new DDFenceBlock(blockSettings, data.getBoolean("make_block_item"), data.getInt("fuel_tick"));
                        }));

        register(new ContentFactory<>(identifier("fence_gate"), Types.BLOCK,
                new SerializableData()
                        .add("material", CCPackDataTypes.MATERIAL)
                        .add("effective_tool", CCPackDataTypes.TOOL_TYPES, ToolTypes.PICKAXES)
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
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroups.MISC)
                        .add("fuel_tick", SerializableDataTypes.INT, 0),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            ItemGroups group = (ItemGroups) data.get("item_group");
                            settings.group(group.group);
                            Material mat = (Material) data.get("material");
                            BlockSoundGroup sounds = (BlockSoundGroup) data.get("block_sound_group");
                            ToolTypes tools = (ToolTypes) data.get("effective_tool");
                            FabricBlockSettings blockSettings = FabricBlockSettings.of(mat).collidable(data.getBoolean("collidable")).strength(data.getInt("hardness"), data.getInt("resistance")).slipperiness(data.getFloat("slipperiness")).luminance(data.getInt("luminance")).sounds(sounds).requiresTool().drops(data.getId("loot_table"));
                            if (data.getBoolean("transparent")) {
                                blockSettings.nonOpaque();
                            }
                            return (Supplier<Block>) () -> new DDFenceGateBlock(blockSettings, data.getBoolean("make_block_item"), data.getInt("fuel_tick"));
                        }));

        register(new ContentFactory<>(identifier("wall"), Types.BLOCK,
                new SerializableData()
                        .add("material", CCPackDataTypes.MATERIAL)
                        .add("effective_tool", CCPackDataTypes.TOOL_TYPES, ToolTypes.PICKAXES)
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
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroups.MISC)
                        .add("fuel_tick", SerializableDataTypes.INT, 0),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            ItemGroups group = (ItemGroups) data.get("item_group");
                            settings.group(group.group);
                            Material mat = (Material) data.get("material");
                            BlockSoundGroup sounds = (BlockSoundGroup) data.get("block_sound_group");
                            ToolTypes tools = (ToolTypes) data.get("effective_tool");
                            FabricBlockSettings blockSettings = FabricBlockSettings.of(mat).collidable(data.getBoolean("collidable")).strength(data.getInt("hardness"), data.getInt("resistance")).slipperiness(data.getFloat("slipperiness")).luminance(data.getInt("luminance")).sounds(sounds).requiresTool().drops(data.getId("loot_table"));
                            if (data.getBoolean("transparent")) {
                                blockSettings.nonOpaque();
                            }
                            return (Supplier<Block>) () -> new DDWallBlock(blockSettings, data.getBoolean("make_block_item"), data.getInt("fuel_tick"));
                        }));

    }

    private static void register(ContentFactory<Supplier<?>> serializer) {
        Registry.register(CCPacksRegistries.CONTENT_FACTORY, serializer.getSerializerId(), serializer);
    }
}
