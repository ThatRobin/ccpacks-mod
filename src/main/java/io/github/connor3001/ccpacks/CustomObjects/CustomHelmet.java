package io.github.connor3001.ccpacks.CustomObjects;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import io.github.connor3001.ccpacks.customTypes.CustomArmorMaterial;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class CustomHelmet {

    public CustomHelmet(String namespace, String id, Integer durability, String name, Integer protection, Integer toughness, Integer enchantability, Integer knockbackResistance) {
        CustomArmorMaterial CUSTOM_MATERIAL = new CustomArmorMaterial(durability, protection, enchantability, toughness, knockbackResistance, name);
        Item EXAMPLE_ITEM = new ArmorItem(CUSTOM_MATERIAL, EquipmentSlot.HEAD, new Item.Settings().group(ItemGroup.COMBAT));
        Registry.register(Registry.ITEM, new Identifier(namespace, id), EXAMPLE_ITEM);
    }
}