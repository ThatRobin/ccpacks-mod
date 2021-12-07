package io.github.thatrobin.ccpacks.data_driven_classes.items;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class DDArmorMaterial implements ArmorMaterial {

    private Identifier repairItem;
    private int durability;
    private int protection;
    private int enchantability;
    private int toughness;
    private int knockbackRes;
    private String name;

    public DDArmorMaterial(int durability, int protection, int enchantability, int toughness, int knockbackRes, String name, Identifier repairItem){
        this.durability = durability;
        this.protection = protection;
        this.enchantability = enchantability;
        this.toughness = toughness;
        this.knockbackRes = knockbackRes;
        this.name = name;
        this.repairItem = repairItem;
    }

    @Override
    public int getDurability(EquipmentSlot slot) {
        return this.durability;
    }

    @Override
    public int getProtectionAmount(EquipmentSlot slot) {
        return this.protection;
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return null;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofStacks(Registry.ITEM.get(repairItem).getDefaultStack());
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackRes;
    }

}