package io.github.connor3001.ccpacks.customTypes;

import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolMaterial;

public class CustomShovelItem extends ShovelItem {
    public CustomShovelItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }
}
