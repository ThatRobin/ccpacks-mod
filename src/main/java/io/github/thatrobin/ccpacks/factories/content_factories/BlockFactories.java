package io.github.thatrobin.ccpacks.factories.content_factories;

import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.thatrobin.ccpacks.CCPackDataTypes;
import io.github.thatrobin.ccpacks.CCPacksMain;
import io.github.thatrobin.ccpacks.data_driven_classes.blocks.*;
import io.github.thatrobin.ccpacks.data_driven_classes.items.DDBlockItem;
import io.github.thatrobin.ccpacks.factories.mechanic_factories.MechanicTypeReference;
import io.github.thatrobin.ccpacks.registries.CCPacksRegistries;
import io.github.thatrobin.ccpacks.util.BlockItemHolder;
import io.github.thatrobin.ccpacks.util.RenderLayerTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
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
                        .add("block_sound_group", CCPackDataTypes.BLOCK_SOUND_GROUP)
                        .add("collidable", SerializableDataTypes.BOOLEAN, true)
                        .add("transparent", SerializableDataTypes.BOOLEAN, false)
                        .add("hardness", SerializableDataTypes.INT, 3)
                        .add("slipperiness", SerializableDataTypes.FLOAT, 0.6f)
                        .add("resistance", SerializableDataTypes.INT, 3)
                        .add("luminance", SerializableDataTypes.INT, 0)
                        .add("requires_tool", SerializableDataTypes.BOOLEAN, false)
                        .add("loot_table", SerializableDataTypes.IDENTIFIER, null)
                        .add("block_item", CCPackDataTypes.ITEM, null),
                data ->
                        (contentType, content) -> {
                            Material mat = data.get("material");
                            BlockSoundGroup sounds = data.get("block_sound_group");
                            Identifier loot = contentType.getIdentifier();
                            if(data.isPresent("loot_table"))
                                loot = data.getId("loot_table");
                            FabricBlockSettings blockSettings = FabricBlockSettings.of(mat).collidable(data.getBoolean("collidable")).strength(data.getInt("hardness"), data.getInt("resistance")).slipperiness(data.getFloat("slipperiness")).luminance(data.getInt("luminance")).sounds(sounds).drops(loot);
                            if(data.isPresent("transparent") && data.getBoolean("transparent"))
                                blockSettings.nonOpaque();
                            if(data.isPresent("requires_tool") && data.getBoolean("requires_tool"))
                                blockSettings.requiresTool();
                            DDBlock block;
                            List<MechanicTypeReference> mechanicTypeReferences = data.get("mechanics");
                            block = new DDBlock(blockSettings, mechanicTypeReferences);
                            if(data.isPresent("block_item")) {
                                BlockItemHolder itemHolder = data.get("block_item");
                                DDBlockItem item = new DDBlockItem(block, itemHolder.settings, itemHolder.name, itemHolder.lore, itemHolder.item_powers);
                                FuelRegistry.INSTANCE.add(item,  itemHolder.fuel_tick);
                                Registry.register(Registry.ITEM, contentType.getIdentifier(), item);
                            }
                            Pair<Block,RenderLayerTypes> pair = new Pair<>(block, data.get("render_layer"));
                            return (Supplier<Pair<Block,RenderLayerTypes>>) () -> pair;
                        }));

        register(new ContentFactory<>(identifier("falling"), Types.BLOCK,
                new SerializableData()
                        .add("material", CCPackDataTypes.MATERIAL)
                        .add("mechanics", CCPackDataTypes.MECHANIC_TYPES, null)
                        .add("render_layer", CCPackDataTypes.RENDER_LAYER, RenderLayerTypes.SOLID)
                        .add("block_sound_group", CCPackDataTypes.BLOCK_SOUND_GROUP)
                        .add("collidable", SerializableDataTypes.BOOLEAN, true)
                        .add("transparent", SerializableDataTypes.BOOLEAN, false)
                        .add("hardness", SerializableDataTypes.INT, 3)
                        .add("slipperiness", SerializableDataTypes.FLOAT, 0.6f)
                        .add("resistance", SerializableDataTypes.INT, 3)
                        .add("luminance", SerializableDataTypes.INT, 0)
                        .add("requires_tool", SerializableDataTypes.BOOLEAN, false)
                        .add("loot_table", SerializableDataTypes.IDENTIFIER, null)
                        .add("block_item", CCPackDataTypes.ITEM, null),
                data ->
                        (contentType, content) -> {
                            Material mat = data.get("material");
                            BlockSoundGroup sounds = data.get("block_sound_group");
                            Identifier loot = contentType.getIdentifier();
                            if(data.isPresent("loot_table"))
                                loot = data.getId("loot_table");
                            FabricBlockSettings blockSettings = FabricBlockSettings.of(mat).collidable(data.getBoolean("collidable")).strength(data.getInt("hardness"), data.getInt("resistance")).slipperiness(data.getFloat("slipperiness")).luminance(data.getInt("luminance")).sounds(sounds).drops(loot);
                            if(data.isPresent("transparent") && data.getBoolean("transparent"))
                                blockSettings.nonOpaque();
                            if(data.isPresent("requires_tool") && data.getBoolean("requires_tool"))
                                blockSettings.requiresTool();
                            DDFallingBlock block;
                            List<MechanicTypeReference> mechanicTypeReferences = data.get("mechanics");
                            block = new DDFallingBlock(blockSettings, mechanicTypeReferences);
                            if(data.isPresent("block_item")) {
                                BlockItemHolder itemHolder = data.get("block_item");
                                DDBlockItem item = new DDBlockItem(block, itemHolder.settings, itemHolder.name, itemHolder.lore, itemHolder.item_powers);
                                FuelRegistry.INSTANCE.add(item,  itemHolder.fuel_tick);
                                Registry.register(Registry.ITEM, contentType.getIdentifier(), item);
                            }
                            Pair<Block,RenderLayerTypes> pair = new Pair<>(block, data.get("render_layer"));
                            return (Supplier<Pair<Block,RenderLayerTypes>>) () -> pair;
                        }));

        register(new ContentFactory<>(identifier("slab"), Types.BLOCK,
                new SerializableData()
                        .add("material", CCPackDataTypes.MATERIAL)
                        .add("mechanics", CCPackDataTypes.MECHANIC_TYPES, null)
                        .add("render_layer", CCPackDataTypes.RENDER_LAYER, RenderLayerTypes.SOLID)
                        .add("block_sound_group", CCPackDataTypes.BLOCK_SOUND_GROUP)
                        .add("collidable", SerializableDataTypes.BOOLEAN, true)
                        .add("transparent", SerializableDataTypes.BOOLEAN, false)
                        .add("hardness", SerializableDataTypes.INT, 3)
                        .add("slipperiness", SerializableDataTypes.FLOAT, 0.6f)
                        .add("resistance", SerializableDataTypes.INT, 3)
                        .add("luminance", SerializableDataTypes.INT, 0)
                        .add("requires_tool", SerializableDataTypes.BOOLEAN, false)
                        .add("loot_table", SerializableDataTypes.IDENTIFIER, null)
                        .add("block_item", CCPackDataTypes.ITEM, null),
                data ->
                        (contentType, content) -> {
                            Material mat = data.get("material");
                            BlockSoundGroup sounds = data.get("block_sound_group");
                            Identifier loot = contentType.getIdentifier();
                            if(data.isPresent("loot_table"))
                                loot = data.getId("loot_table");
                            FabricBlockSettings blockSettings = FabricBlockSettings.of(mat).collidable(data.getBoolean("collidable")).strength(data.getInt("hardness"), data.getInt("resistance")).slipperiness(data.getFloat("slipperiness")).luminance(data.getInt("luminance")).sounds(sounds).drops(loot);
                            if(data.isPresent("transparent") && data.getBoolean("transparent"))
                                blockSettings.nonOpaque();
                            if(data.isPresent("requires_tool") && data.getBoolean("requires_tool"))
                                blockSettings.requiresTool();
                            DDSlabBlock block;
                            List<MechanicTypeReference> mechanicTypeReferences = data.get("mechanics");
                            block = new DDSlabBlock(blockSettings, mechanicTypeReferences);
                            if(data.isPresent("block_item")) {
                                BlockItemHolder itemHolder = data.get("block_item");
                                DDBlockItem item = new DDBlockItem(block, itemHolder.settings, itemHolder.name, itemHolder.lore, itemHolder.item_powers);
                                FuelRegistry.INSTANCE.add(item,  itemHolder.fuel_tick);
                                Registry.register(Registry.ITEM, contentType.getIdentifier(), item);
                            }
                            Pair<Block,RenderLayerTypes> pair = new Pair<>(block, data.get("render_layer"));
                            return (Supplier<Pair<Block,RenderLayerTypes>>) () -> pair;
                        }));

        register(new ContentFactory<>(identifier("stairs"), Types.BLOCK,
                new SerializableData()
                        .add("material", CCPackDataTypes.MATERIAL)
                        .add("mechanics", CCPackDataTypes.MECHANIC_TYPES, null)
                        .add("render_layer", CCPackDataTypes.RENDER_LAYER, RenderLayerTypes.SOLID)
                        .add("block_sound_group", CCPackDataTypes.BLOCK_SOUND_GROUP)
                        .add("collidable", SerializableDataTypes.BOOLEAN, true)
                        .add("transparent", SerializableDataTypes.BOOLEAN, false)
                        .add("hardness", SerializableDataTypes.INT, 3)
                        .add("slipperiness", SerializableDataTypes.FLOAT, 0.6f)
                        .add("resistance", SerializableDataTypes.INT, 3)
                        .add("luminance", SerializableDataTypes.INT, 0)
                        .add("requires_tool", SerializableDataTypes.BOOLEAN, false)
                        .add("loot_table", SerializableDataTypes.IDENTIFIER, null)
                        .add("block_item", CCPackDataTypes.ITEM, null),
                data ->
                        (contentType, content) -> {
                            Material mat = data.get("material");
                            BlockSoundGroup sounds = data.get("block_sound_group");
                            Identifier loot = contentType.getIdentifier();
                            if(data.isPresent("loot_table"))
                                loot = data.getId("loot_table");
                            FabricBlockSettings blockSettings = FabricBlockSettings.of(mat).collidable(data.getBoolean("collidable")).strength(data.getInt("hardness"), data.getInt("resistance")).slipperiness(data.getFloat("slipperiness")).luminance(data.getInt("luminance")).sounds(sounds).drops(loot);
                            if(data.isPresent("transparent") && data.getBoolean("transparent"))
                                blockSettings.nonOpaque();
                            if(data.isPresent("requires_tool") && data.getBoolean("requires_tool"))
                                blockSettings.requiresTool();
                            DDStairBlock block;
                            List<MechanicTypeReference> mechanicTypeReferences = data.get("mechanics");
                            block = new DDStairBlock(Blocks.OAK_PLANKS.getDefaultState(), blockSettings, mechanicTypeReferences);
                            if(data.isPresent("block_item")) {
                                BlockItemHolder itemHolder = data.get("block_item");
                                DDBlockItem item = new DDBlockItem(block, itemHolder.settings, itemHolder.name, itemHolder.lore, itemHolder.item_powers);
                                FuelRegistry.INSTANCE.add(item,  itemHolder.fuel_tick);
                                Registry.register(Registry.ITEM, contentType.getIdentifier(), item);
                            }
                            Pair<Block,RenderLayerTypes> pair = new Pair<>(block, data.get("render_layer"));
                            return (Supplier<Pair<Block,RenderLayerTypes>>) () -> pair;
                        }));

        register(new ContentFactory<>(identifier("fence"), Types.BLOCK,
                new SerializableData()
                        .add("material", CCPackDataTypes.MATERIAL)
                        .add("mechanics", CCPackDataTypes.MECHANIC_TYPES, null)
                        .add("render_layer", CCPackDataTypes.RENDER_LAYER, RenderLayerTypes.SOLID)
                        .add("block_sound_group", CCPackDataTypes.BLOCK_SOUND_GROUP)
                        .add("collidable", SerializableDataTypes.BOOLEAN, true)
                        .add("transparent", SerializableDataTypes.BOOLEAN, false)
                        .add("hardness", SerializableDataTypes.INT, 3)
                        .add("slipperiness", SerializableDataTypes.FLOAT, 0.6f)
                        .add("resistance", SerializableDataTypes.INT, 3)
                        .add("luminance", SerializableDataTypes.INT, 0)
                        .add("requires_tool", SerializableDataTypes.BOOLEAN, false)
                        .add("loot_table", SerializableDataTypes.IDENTIFIER, null)
                        .add("block_item", CCPackDataTypes.ITEM, null),
                data ->
                        (contentType, content) -> {
                            Material mat = data.get("material");
                            BlockSoundGroup sounds = data.get("block_sound_group");
                            Identifier loot = contentType.getIdentifier();
                            if(data.isPresent("loot_table"))
                                loot = data.getId("loot_table");
                            FabricBlockSettings blockSettings = FabricBlockSettings.of(mat).collidable(data.getBoolean("collidable")).strength(data.getInt("hardness"), data.getInt("resistance")).slipperiness(data.getFloat("slipperiness")).luminance(data.getInt("luminance")).sounds(sounds).drops(loot);
                            if(data.isPresent("transparent") && data.getBoolean("transparent"))
                                blockSettings.nonOpaque();
                            if(data.isPresent("requires_tool") && data.getBoolean("requires_tool"))
                                blockSettings.requiresTool();
                            DDFenceBlock block;
                            List<MechanicTypeReference> mechanicTypeReferences = data.get("mechanics");
                            block = new DDFenceBlock(blockSettings, mechanicTypeReferences);
                            if(data.isPresent("block_item")) {
                                BlockItemHolder itemHolder = data.get("block_item");
                                DDBlockItem item = new DDBlockItem(block, itemHolder.settings, itemHolder.name, itemHolder.lore, itemHolder.item_powers);
                                FuelRegistry.INSTANCE.add(item,  itemHolder.fuel_tick);
                                Registry.register(Registry.ITEM, contentType.getIdentifier(), item);
                            }
                            Pair<Block,RenderLayerTypes> pair = new Pair<>(block, data.get("render_layer"));
                            return (Supplier<Pair<Block,RenderLayerTypes>>) () -> pair;
                        }));

        register(new ContentFactory<>(identifier("fence_gate"), Types.BLOCK,
                new SerializableData()
                        .add("material", CCPackDataTypes.MATERIAL)
                        .add("mechanics", CCPackDataTypes.MECHANIC_TYPES, null)
                        .add("render_layer", CCPackDataTypes.RENDER_LAYER, RenderLayerTypes.SOLID)
                        .add("block_sound_group", CCPackDataTypes.BLOCK_SOUND_GROUP)
                        .add("collidable", SerializableDataTypes.BOOLEAN, true)
                        .add("transparent", SerializableDataTypes.BOOLEAN, false)
                        .add("hardness", SerializableDataTypes.INT, 3)
                        .add("slipperiness", SerializableDataTypes.FLOAT, 0.6f)
                        .add("resistance", SerializableDataTypes.INT, 3)
                        .add("luminance", SerializableDataTypes.INT, 0)
                        .add("requires_tool", SerializableDataTypes.BOOLEAN, false)
                        .add("loot_table", SerializableDataTypes.IDENTIFIER, null)
                        .add("block_item", CCPackDataTypes.ITEM, null),
                data ->
                        (contentType, content) -> {
                            Material mat = data.get("material");
                            BlockSoundGroup sounds = data.get("block_sound_group");
                            Identifier loot = contentType.getIdentifier();
                            if(data.isPresent("loot_table"))
                                loot = data.getId("loot_table");
                            FabricBlockSettings blockSettings = FabricBlockSettings.of(mat).collidable(data.getBoolean("collidable")).strength(data.getInt("hardness"), data.getInt("resistance")).slipperiness(data.getFloat("slipperiness")).luminance(data.getInt("luminance")).sounds(sounds).drops(loot);
                            if(data.isPresent("transparent") && data.getBoolean("transparent"))
                                blockSettings.nonOpaque();
                            if(data.isPresent("requires_tool") && data.getBoolean("requires_tool"))
                                blockSettings.requiresTool();
                            DDFenceGateBlock block;
                            List<MechanicTypeReference> mechanicTypeReferences = data.get("mechanics");
                            block = new DDFenceGateBlock(blockSettings, mechanicTypeReferences);
                            if(data.isPresent("block_item")) {
                                BlockItemHolder itemHolder = data.get("block_item");
                                DDBlockItem item = new DDBlockItem(block, itemHolder.settings, itemHolder.name, itemHolder.lore, itemHolder.item_powers);
                                FuelRegistry.INSTANCE.add(item,  itemHolder.fuel_tick);
                                Registry.register(Registry.ITEM, contentType.getIdentifier(), item);
                            }
                            Pair<Block,RenderLayerTypes> pair = new Pair<>(block, data.get("render_layer"));
                            return (Supplier<Pair<Block,RenderLayerTypes>>) () -> pair;
                        }));

        register(new ContentFactory<>(identifier("wall"), Types.BLOCK,
                new SerializableData()
                        .add("material", CCPackDataTypes.MATERIAL)
                        .add("mechanics", CCPackDataTypes.MECHANIC_TYPES, null)
                        .add("render_layer", CCPackDataTypes.RENDER_LAYER, RenderLayerTypes.SOLID)
                        .add("block_sound_group", CCPackDataTypes.BLOCK_SOUND_GROUP)
                        .add("collidable", SerializableDataTypes.BOOLEAN, true)
                        .add("transparent", SerializableDataTypes.BOOLEAN, false)
                        .add("hardness", SerializableDataTypes.INT, 3)
                        .add("slipperiness", SerializableDataTypes.FLOAT, 0.6f)
                        .add("resistance", SerializableDataTypes.INT, 3)
                        .add("luminance", SerializableDataTypes.INT, 0)
                        .add("requires_tool", SerializableDataTypes.BOOLEAN, false)
                        .add("loot_table", SerializableDataTypes.IDENTIFIER, null)
                        .add("block_item", CCPackDataTypes.ITEM, null),
                data ->
                        (contentType, content) -> {
                            Material mat = data.get("material");
                            BlockSoundGroup sounds = data.get("block_sound_group");
                            Identifier loot = contentType.getIdentifier();
                            if(data.isPresent("loot_table"))
                                loot = data.getId("loot_table");
                            FabricBlockSettings blockSettings = FabricBlockSettings.of(mat).collidable(data.getBoolean("collidable")).strength(data.getInt("hardness"), data.getInt("resistance")).slipperiness(data.getFloat("slipperiness")).luminance(data.getInt("luminance")).sounds(sounds).drops(loot);
                            if(data.isPresent("transparent") && data.getBoolean("transparent"))
                                blockSettings.nonOpaque();
                            if(data.isPresent("requires_tool") && data.getBoolean("requires_tool"))
                                blockSettings.requiresTool();
                            DDWallBlock block;
                            List<MechanicTypeReference> mechanicTypeReferences = data.get("mechanics");
                            block = new DDWallBlock(blockSettings, mechanicTypeReferences);
                            if(data.isPresent("block_item")) {
                                BlockItemHolder itemHolder = data.get("block_item");
                                DDBlockItem item = new DDBlockItem(block, itemHolder.settings, itemHolder.name, itemHolder.lore, itemHolder.item_powers);
                                FuelRegistry.INSTANCE.add(item,  itemHolder.fuel_tick);
                                Registry.register(Registry.ITEM, contentType.getIdentifier(), item);
                            }
                            Pair<Block,RenderLayerTypes> pair = new Pair<>(block, data.get("render_layer"));
                            return (Supplier<Pair<Block,RenderLayerTypes>>) () -> pair;
                        }));

    }

    private static void register(ContentFactory<Supplier<?>> serializer) {
        Registry.register(CCPacksRegistries.CONTENT_FACTORY, serializer.getSerializerId(), serializer);
    }
}
