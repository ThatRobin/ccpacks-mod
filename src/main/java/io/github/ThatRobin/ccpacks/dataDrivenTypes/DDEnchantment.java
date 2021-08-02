package io.github.ThatRobin.ccpacks.dataDrivenTypes;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class DDEnchantment extends Enchantment {

    private Rarity rarity;
    private int maxLevel;
    private boolean curse;

    public DDEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes, int maxLevel, boolean curse) {
        super(weight, type, slotTypes);
        this.rarity = weight;
        this.curse = curse;
        this.maxLevel = maxLevel;
    }

    @Override
    public boolean isCursed() {
        return this.curse;
    }

    @Override
    public Enchantment.Rarity getRarity() {
        return this.rarity;
    }

    @Override
    public int getMaxLevel() {
        return this.maxLevel;
    }
}
