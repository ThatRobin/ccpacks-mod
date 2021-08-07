package io.github.ThatRobin.ccpacks.Power;

import io.github.ThatRobin.ccpacks.Screen.ChoiceScreen;
import io.github.ThatRobin.ccpacks.Screen.ContainerScreenHandler;
import io.github.ThatRobin.ccpacks.util.CustomAnvil;
import io.github.ThatRobin.ccpacks.util.CustomCraftingTable;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.screen.*;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

import java.util.function.Predicate;

public class ChoiceScreenPower extends Power {

    private final ChoiceScreen screen;

    public ChoiceScreenPower(PowerType<?> type, LivingEntity entity, ChoiceScreen screen) {
        super(type, entity);
        this.screen = screen;
    }

    public ChoiceScreen getScreen() {
        return screen;
    }
}
