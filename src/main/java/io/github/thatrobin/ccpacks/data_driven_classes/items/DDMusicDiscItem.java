package io.github.thatrobin.ccpacks.data_driven_classes.items;

import com.google.common.collect.Lists;
import io.github.apace100.apoli.util.PowerGrantingItem;
import io.github.apace100.apoli.util.StackPowerUtil;
import io.github.thatrobin.ccpacks.util.StackPowerExpansion;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.sound.SoundEvent;

import java.util.Collection;
import java.util.List;

public class DDMusicDiscItem extends MusicDiscItem implements PowerGrantingItem {

    private final List<StackPowerExpansion> item_powers;

    public DDMusicDiscItem(int comparatorOutput, SoundEvent sound, Settings settings, List<StackPowerExpansion> item_powers) {
        super(comparatorOutput, sound, settings);
        this.item_powers = item_powers;
    }

    @Override
    public Collection<StackPowerUtil.StackPower> getPowers(ItemStack stack, EquipmentSlot slot) {
        List<StackPowerUtil.StackPower> stackPowerList = Lists.newArrayList();
        if(this.item_powers != null) {
            this.item_powers.forEach(item_power -> {
                if (item_power.slot == slot) {
                    stackPowerList.add(item_power);
                }
            });
        }
        return stackPowerList;
    }

}
