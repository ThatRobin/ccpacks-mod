package io.github.thatrobin.ccpacks.data_driven_classes.items;

import com.google.common.collect.Lists;
import io.github.apace100.apoli.util.PowerGrantingItem;
import io.github.apace100.apoli.util.StackPowerUtil;
import io.github.thatrobin.ccpacks.util.StackPowerExpansion;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.List;

public class DDMusicDiscItem extends MusicDiscItem implements PowerGrantingItem {

    private final List<StackPowerExpansion> item_powers;
    private LiteralText name;
    private final List<String> lore;

    public DDMusicDiscItem(int comparatorOutput, SoundEvent sound, Settings settings, List<StackPowerExpansion> item_powers, LiteralText name, List<String> lore) {
        super(comparatorOutput, sound, settings);
        this.name = name;
        this.lore = lore;
        this.item_powers = item_powers;
    }

    @Override
    public Text getName() {
        if(name != null) {
            return name;
        }
        return new TranslatableText(this.getTranslationKey());
    }

    @Override
    public Text getName(ItemStack stack) {
        if(name != null) {
            return name;
        }
        return new TranslatableText(this.getTranslationKey(stack));
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        if (lore != null) {
            if (lore.size() > 0) {
                for (String s : lore) {
                    tooltip.add(new LiteralText(s).formatted(Formatting.GRAY));
                }
            }
        }
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
