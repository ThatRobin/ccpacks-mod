package io.github.connor3001.ccpacks.customTypes;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;

public class CustomArmorMaterial implements ArmorMaterial {

    private int durability;
    private int protection;
    private int enchantability;
    private int toughness;
    private int knockbackRes;
    private String name;

    public CustomArmorMaterial(int durability, int protection, int enchantability, int toughness, int knockbackRes, String name){
        this.durability = durability;
        this.protection = protection;
        this.enchantability = enchantability;
        this.toughness = toughness;
        this.knockbackRes = knockbackRes;
        this.name = name;
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
        return null;
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