package io.github.ThatRobin.ccpacks.middleManTypes;

import io.github.ThatRobin.ccpacks.dataDrivenTypes.DDEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MMEnchantment {

    public MMEnchantment(Identifier identifier) {
        DDEnchantment EXAMPLE_ENCHANTMENT = new DDEnchantment(Enchantment.Rarity.VERY_RARE, EnchantmentTarget.BREAKABLE, null);
        Registry.register(Registry.ENCHANTMENT, identifier, EXAMPLE_ENCHANTMENT);
    }

}
