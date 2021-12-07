package io.github.thatrobin.ccpacks.util;

import io.github.apace100.apoli.util.StackPowerUtil;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;

public class StackPowerExpansion extends StackPowerUtil.StackPower {

    public StackPowerExpansion(Identifier powerId, boolean isHidden, boolean isNegative, EquipmentSlot slot) {
        this.powerId = powerId;
        this.slot = slot;
        this.isHidden = isHidden;
        this.isNegative = isNegative;
    }
}