package io.github.thatrobin.ccpacks.data_driven_classes.items;

import com.google.common.collect.Lists;
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
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DDArmorItem extends ArmorItem implements PowerGrantingItem, IAnimatable {

    private final AnimationFactory factory = new AnimationFactory(this);
    private final List<String> lore;
    private final List<StackPowerExpansion> item_powers;

    public DDArmorItem(ArmorMaterial material, EquipmentSlot slot, Settings settings, List<String> lore, List<StackPowerExpansion> item_powers) {
        super(material, slot, settings);
        this.lore = lore;
        this.item_powers = item_powers;
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