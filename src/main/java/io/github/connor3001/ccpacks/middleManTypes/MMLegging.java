package io.github.connor3001.ccpacks.middleManTypes;

import io.github.apace100.apoli.power.PowerTypeReference;
import io.github.connor3001.ccpacks.dataDrivenTypes.DDArmorItem;
import io.github.connor3001.ccpacks.dataDrivenTypes.DDArmorMaterial;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.List;

public class MMLegging {
    public MMLegging(Identifier identifier, Integer durability, String name, Integer protection, Integer toughness, Integer enchantability, Integer knockbackResistance, List<PowerTypeReference> power, List<String> lore, Item item) {
        DDArmorMaterial CUSTOM_MATERIAL = new DDArmorMaterial(durability, protection, enchantability, toughness, knockbackResistance, name, item);
        Item EXAMPLE_ITEM = new DDArmorItem(CUSTOM_MATERIAL, EquipmentSlot.LEGS, new Item.Settings().group(ItemGroup.COMBAT), identifier, power, lore);
        Registry.register(Registry.ITEM, identifier, EXAMPLE_ITEM);
    }
}