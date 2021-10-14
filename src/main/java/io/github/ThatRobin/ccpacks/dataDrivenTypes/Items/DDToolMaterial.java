package io.github.ThatRobin.ccpacks.dataDrivenTypes.Items;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class DDToolMaterial implements ToolMaterial{

    private int durability;
    private float miningMult;
    private float attackDamage;
    private int miningLevel;
    private int enchantability;
    private ItemStack repairItem;

    public DDToolMaterial(int durability, float miningMult, float attackDamage, int miningLevel, int enchantability, ItemStack repairItem){
        this.durability = durability;
        this.miningMult = miningMult;
        this.attackDamage = attackDamage;
        this.miningLevel = miningLevel;
        this.enchantability = enchantability;
        this.repairItem = repairItem;
    }

    @Override
    public int getDurability() {
        return this.durability;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return this.miningMult;
    }

    @Override
    public float getAttackDamage() {
        return this.attackDamage;
    }

    @Override
    public int getMiningLevel() {
        return this.miningLevel;
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofStacks(this.repairItem);
    }
}
