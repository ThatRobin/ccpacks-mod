package io.github.connor3001.ccpacks.CustomObjects;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.sun.jna.platform.win32.DdemlUtil;
import io.github.apace100.apoli.power.Active;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.connor3001.ccpacks.customTypes.CustomItemItem;
import net.fabricmc.fabric.api.client.networking.v1.C2SPlayChannelEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.function.Consumer;

public class CustomItem {

    public CustomItem(String namespace, String id, Integer max_count, Boolean consume, Consumer<Entity> entityAction, ConditionFactory<LivingEntity>.Instance condition) {
        Item EXAMPLE_ITEM = new CustomItemItem(new FabricItemSettings().maxCount(max_count).group(ItemGroup.MISC), consume, entityAction,condition);
        Registry.register(Registry.ITEM, new Identifier(namespace, id), EXAMPLE_ITEM);
    }
}