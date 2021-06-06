package io.github.connor3001.ccpacks.CustomObjects;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.connor3001.ccpacks.CCPacksMain;
import io.github.connor3001.ccpacks.customTypes.CustomBlockBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;

import java.io.*;
import java.net.URI;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class CustomBlock {

    public CustomBlock(String namespace, String id, Integer hardness, Integer resistance, Float slipperiness, Consumer<Entity> action, ConditionFactory<LivingEntity>.Instance condition){
        CustomBlockBlock EXAMPLE_BLOCK = new CustomBlockBlock(FabricBlockSettings.of(Material.STONE).strength(hardness, resistance).slipperiness(slipperiness), action, condition);
        Registry.register(Registry.BLOCK, new Identifier(namespace, id), EXAMPLE_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(namespace, id), new BlockItem(EXAMPLE_BLOCK, new FabricItemSettings().group(ItemGroup.DECORATIONS)));
    }

}

