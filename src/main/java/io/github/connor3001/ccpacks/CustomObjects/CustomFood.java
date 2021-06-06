package io.github.connor3001.ccpacks.CustomObjects;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import io.github.connor3001.ccpacks.customTypes.CustomArmorMaterial;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.stat.Stat;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class CustomFood {

    public CustomFood(String namespace, String id, Integer maxCount, Integer hunger, Float saturation, Boolean meat, Boolean snack, Boolean alwaysEdible) {
        FoodComponent.Builder food = new FoodComponent.Builder().hunger(hunger).saturationModifier(saturation);
        if(meat) {
            food.meat();
        }
        if(snack){
            food.snack();
        }
        if(alwaysEdible){
            food.alwaysEdible();
        }
        FoodComponent foodComp = food.build();
        Item EXAMPLE_ITEM = new Item(new FabricItemSettings().group(ItemGroup.FOOD).maxCount(maxCount).food(foodComp));
        Registry.register(Registry.ITEM, new Identifier(namespace, id), EXAMPLE_ITEM);
    }

}