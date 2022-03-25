package io.github.thatrobin.ccpacks.data_driven_classes.items;

import com.google.common.collect.Lists;
import io.github.thatrobin.ccpacks.util.ColourHolder;
import io.github.thatrobin.ccpacks.util.StackPowerExpansion;
import io.github.apace100.apoli.util.PowerGrantingItem;
import io.github.apace100.apoli.util.StackPowerUtil;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DDArmorItem extends ArmorItem implements PowerGrantingItem, IAnimatable {

    private final AnimationFactory factory = new AnimationFactory(this);
    private final List<String> lore;
    private final List<StackPowerExpansion> item_powers;
    private LiteralText name;
    private final ColourHolder startColours;
    private final ColourHolder endColours;

    public DDArmorItem(ArmorMaterial material, EquipmentSlot slot, Settings settings, List<String> lore, List<StackPowerExpansion> item_powers, LiteralText name, ColourHolder startColours, ColourHolder endColours) {
        super(material, slot, settings);
        this.name = name;
        this.lore = lore;
        this.item_powers = item_powers;
        this.startColours = startColours;
        this.endColours = endColours;
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 20, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    // Predicate runs every frame
    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        // This is all the extradata this event carries. The livingentity is the entity
        // that's wearing the armor. The itemstack and equipmentslottype are self
        // explanatory.
        LivingEntity livingEntity = event.getExtraDataOfType(LivingEntity.class).get(0);

        // Always loop the animation but later on in this method we'll decide whether or
        // not to actually play it
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.armor.new", true));

        // If the living entity is an armorstand just play the animation nonstop
        if (livingEntity instanceof ArmorStandEntity) {
            return PlayState.CONTINUE;
        }

        // The entity is a player, so we want to only play if the player is wearing the
        // full set of armor
        else if (livingEntity instanceof PlayerEntity player) {
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }
}