package io.github.thatrobin.ccpacks.data_driven_classes;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

import java.util.List;

//public class DDEnchantment extends Enchantment implements VirtualObject {
public class DDEnchantment extends Enchantment {

    private Rarity rarity;
    private int maxLevel;
    private boolean curse;
    private boolean treasure;
    private List<Enchantment> enchantments;

    public DDEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes, int maxLevel, boolean curse, boolean treasure, List<Enchantment> blacklist) {
        super(weight, type, slotTypes);
        this.rarity = weight;
        this.curse = curse;
        this.maxLevel = maxLevel;
        this.treasure = treasure;
        this.enchantments = blacklist;
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

    @Override
    public boolean isTreasure() {
        return this.treasure;
    }

    @Override
    protected boolean canAccept(Enchantment other) {

        boolean acceptable = false;

        for (int i = enchantments.size(); i > 0; i--) {
            if (other != enchantments.get(i - 1))
                acceptable = true;
            else {
                acceptable = false;
                break;
            }
        }
        return super.canAccept(other) && acceptable;
    }
}
