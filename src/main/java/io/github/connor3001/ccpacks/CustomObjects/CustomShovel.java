package io.github.connor3001.ccpacks.CustomObjects;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import io.github.connor3001.ccpacks.customTypes.CustomAxeItem;
import io.github.connor3001.ccpacks.customTypes.CustomShovelItem;
import io.github.connor3001.ccpacks.customTypes.CustomToolMaterial;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class CustomShovel {

    public CustomShovel(String namespace, String id, Integer durability, Float mining_speed_multiplier, Integer attack_speed, Integer mining_level, Integer enchantability, Integer attack_damage) {
        Item EXAMPLE_ITEM = new CustomShovelItem(new CustomToolMaterial(durability, mining_speed_multiplier, attack_damage, mining_level, enchantability), 0, attack_speed - 4, new FabricItemSettings().maxCount(1).group(ItemGroup.TOOLS));
        Registry.register(Registry.ITEM, new Identifier(namespace, id), EXAMPLE_ITEM);
    }
}