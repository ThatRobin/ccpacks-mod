package io.github.thatrobin.ccpacks.data_driven_classes.items;

import com.google.common.collect.Lists;
import dev.emi.trinkets.TrinketSlot;
import dev.emi.trinkets.api.*;
import io.github.apace100.apoli.power.PowerType;
import io.github.thatrobin.ccpacks.util.ColourHolder;
import io.github.thatrobin.ccpacks.util.PowerGrantingTrinket;
import io.github.thatrobin.ccpacks.util.StackPowerExpansion;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import io.github.apace100.apoli.util.StackPowerUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.awt.*;
import java.util.Collection;
import java.util.List;

public class DDTrinketItem extends TrinketItem implements PowerGrantingTrinket {

    private List<StackPowerExpansion> item_powers;
    private final List<String> lore;
    private final ColourHolder startColours;
    private final ColourHolder endColours;
    private LiteralText name;

    public DDTrinketItem(Settings settings, LiteralText name, List<String> lore, ColourHolder startColours, ColourHolder endColours, List<StackPowerExpansion> item_powers) {
        super(settings);
        this.lore = lore;
        this.name = name;
        this.startColours = startColours;
        this.endColours = endColours;
        this.item_powers = item_powers;
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        if (lore != null) {
            if (lore.size() > 0) {
                for (String s : lore) {
                    tooltip.add(new LiteralText(s).formatted(Formatting.GRAY));
                }
            }
        }
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
    public int getItemBarColor(ItemStack stack) {
        if(startColours != null && endColours != null) {
            int[] scolor = new int[]{(int)(startColours.getRed() * 255), (int)(startColours.getGreen() * 255), (int)(startColours.getBlue() * 255)};
            int[] ecolor = new int[]{(int)(endColours.getRed() * 255), (int)(endColours.getGreen() * 255), (int)(endColours.getBlue() * 255)};

            float dp = ((float) this.getMaxDamage() - (float) stack.getDamage()) / (float) this.getMaxDamage();

            float[] hsvs = Color.RGBtoHSB(scolor[0],scolor[1],scolor[2], null);
            float[] hsve = Color.RGBtoHSB(ecolor[0],ecolor[1],ecolor[2], null);

            float lerpr = lerp(hsve[0], hsvs[0], dp);
            float lerpg = lerp(hsve[1], hsvs[1], dp);
            float lerpb = lerp(hsve[2], hsvs[2], dp);

            return MathHelper.hsvToRgb(lerpr, lerpg, lerpb);
        } else {
            float f = Math.max(0.0F, ((float)this.getMaxDamage() - (float)stack.getDamage()) / (float)this.getMaxDamage());
            return MathHelper.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
        }
    }

    private float lerp(float a, float b, float f)
    {
        return (float)(a * (1.0 - f)) + (b * f);
    }

    @Override
    public Collection<StackPowerUtil.StackPower> getTrinketPowers(ItemStack stack) {
        List<StackPowerUtil.StackPower> stackPowerList = Lists.newArrayList();
        if(this.item_powers != null) {
            stackPowerList.addAll(this.item_powers);
        }
        return stackPowerList;
    }

    @Override
    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        List<StackPowerUtil.StackPower> powers = getTrinketPowers(stack).stream().toList();
        if(powers.size() > 0) {
            Identifier source = new Identifier(Identifier.tryParse(this.getDefaultStack().getNbt().getString("id")).getNamespace(), slot.inventory().getSlotType().getName());
            PowerHolderComponent powerHolder = PowerHolderComponent.KEY.get(entity);
            powers.forEach(sp -> {
                if(PowerTypeRegistry.contains(sp.powerId)) {
                    powerHolder.addPower(PowerTypeRegistry.get(sp.powerId), source);
                }
            });
            powerHolder.sync();
        } else if(getTrinketPowers(stack).size() > 0) {
            PowerHolderComponent.KEY.get(entity).sync();
        }

    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        List<StackPowerUtil.StackPower> powers = getTrinketPowers(stack).stream().toList();
        if(powers.size() > 0) {
            Identifier source = new Identifier(Identifier.tryParse(this.getDefaultStack().getNbt().getString("id")).getNamespace(), slot.inventory().getSlotType().getName());
            PowerHolderComponent powerHolder = PowerHolderComponent.KEY.get(entity);
            powers.forEach(sp -> {
                if(PowerTypeRegistry.contains(sp.powerId)) {
                    powerHolder.removePower(PowerTypeRegistry.get(sp.powerId), source);
                }
            });
            powerHolder.sync();
        }
    }
}