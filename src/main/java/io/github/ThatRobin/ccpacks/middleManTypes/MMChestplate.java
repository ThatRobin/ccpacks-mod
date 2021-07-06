package io.github.ThatRobin.ccpacks.middleManTypes;

import io.github.ThatRobin.ccpacks.dataDrivenTypes.DDArmorItem;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.DDArmorMaterial;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.List;

public class MMChestplate {

    public MMChestplate(Identifier identifier, Integer durability, String name, Integer protection, Integer toughness, Integer enchantability, Integer knockbackResistance, List<String> lore, Item item) {
        DDArmorMaterial CUSTOM_MATERIAL = new DDArmorMaterial(durability, protection, enchantability, toughness, knockbackResistance, name, item);
        Item EXAMPLE_ITEM = new DDArmorItem(CUSTOM_MATERIAL, EquipmentSlot.CHEST, new Item.Settings().group(ItemGroup.COMBAT), lore);
        Registry.register(Registry.ITEM, identifier, EXAMPLE_ITEM);
    }
}