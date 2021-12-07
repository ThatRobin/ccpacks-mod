package io.github.thatrobin.ccpacks.data_driven_classes.items;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class DDToolMaterial implements ToolMaterial{

    private int durability;
    private float miningMult;
    private float attackDamage;
    private int miningLevel;
    private int enchantability;
    private Identifier repairItem;

    public DDToolMaterial(int durability, float miningMult, float attackDamage, int miningLevel, int enchantability, Identifier repairItem){
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
        return Ingredient.ofStacks(Registry.ITEM.get(repairItem).getDefaultStack());
    }
}
