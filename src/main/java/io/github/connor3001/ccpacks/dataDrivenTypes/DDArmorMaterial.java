package io.github.connor3001.ccpacks.dataDrivenTypes;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;

public class DDArmorMaterial implements ArmorMaterial {

    private Ingredient repIng;
    private int durability;
    private int protection;
    private int enchantability;
    private int toughness;
    private int knockbackRes;
    private String name;

    public DDArmorMaterial(int durability, int protection, int enchantability, int toughness, int knockbackRes, String name, Item repItem){
        this.durability = durability;
        this.protection = protection;
        this.enchantability = enchantability;
        this.toughness = toughness;
        this.knockbackRes = knockbackRes;
        this.name = name;
        this.repIng = Ingredient.ofItems(repItem);
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
        return this.repIng;
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