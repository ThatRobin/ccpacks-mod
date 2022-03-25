package io.github.thatrobin.ccpacks.util;

import io.github.apace100.apoli.util.StackPowerUtil;
import net.minecraft.item.ItemStack;

import java.util.Collection;

public interface PowerGrantingTrinket {
    Collection<StackPowerUtil.StackPower> getTrinketPowers(ItemStack stack);
}
