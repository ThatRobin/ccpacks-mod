package io.github.ThatRobin.ccpacks.middleManTypes;

import io.github.ThatRobin.ccpacks.dataDrivenTypes.DDSwordItem;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.DDToolMaterial;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.List;

public class MMSword {

    public MMSword(Identifier identifier, Integer durability, Float mining_speed_multiplier, Integer attack_speed, Integer mining_level, Integer enchantability, Integer attack_damage, List<String> lore) {
        Item EXAMPLE_ITEM = new DDSwordItem(new DDToolMaterial(durability, mining_speed_multiplier, attack_damage, mining_level, enchantability), 0, attack_speed - 4, new FabricItemSettings().maxCount(1).group(ItemGroup.TOOLS), lore);
        Registry.register(Registry.ITEM, identifier, EXAMPLE_ITEM);
    }
}