package io.github.thatrobin.ccpacks.power;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class BindPower extends Power {

    private final Predicate<ItemStack> item_condition;
    private final Set<Integer> slots;
    private final Predicate<ItemStack> prevent_use;

    public BindPower(PowerType<?> type, LivingEntity entity, Predicate<ItemStack> item_condition, Collection<Integer> slots, Predicate<ItemStack> prevent_use) {
        super(type, entity);
        this.item_condition = item_condition;
        this.prevent_use = prevent_use;
        if(slots == null) {
            this.slots = null;
        } else {
            this.slots = new HashSet<>(slots);
        }
    }

    public boolean checkSlot(int slot){
        if(slots == null) {
            return true;
        } else if(slots.contains(slot)) {
            return true;
        }
        return false;
    }

    public boolean doesPrevent(ItemStack stack) {
        return prevent_use == null || prevent_use.test(stack);
    }

    public boolean doesApply(ItemStack stack) {
        return item_condition == null || item_condition.test(stack);
    }

}

