package io.github.ThatRobin.ccpacks.dataDrivenTypes.Items;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import java.util.List;

public class DDFoodItem extends Item {
    private SoundEvent sound;
    private boolean drinkable;
    private ItemConvertible returns;
    private int eating_time;
    private List<String> lore;
    private StatusEffect remove_effect;
    private StatusEffectInstance add_effect;

    public DDFoodItem(Settings settings, boolean drinkable, SoundEvent sound, ItemConvertible returns, int eating_time, List<String> lore, StatusEffect remove_effect, StatusEffectInstance add_effect) {
        super(settings);
        this.drinkable = drinkable;
        this.sound = sound;
        this.returns = returns;
        this.eating_time = eating_time;
        this.lore = lore;
        this.remove_effect = remove_effect;
        this.add_effect = add_effect;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        if(this.drinkable) {
            return UseAction.DRINK;
        } else
            return UseAction.EAT;
    }

    @Override
    public SoundEvent getDrinkSound() {
        return this.sound;
    }

    @Override
    public SoundEvent getEatSound() {
        return this.sound;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        super.finishUsing(stack, world, user);
        if (user instanceof ServerPlayerEntity serverPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
            serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        }

        if (!world.isClient) {
            user.removeStatusEffect(remove_effect);
            user.addStatusEffect(add_effect);
        }

        if (stack.isEmpty()) {
            return new ItemStack(returns);
        } else {
            if (user instanceof PlayerEntity && !((PlayerEntity)user).getAbilities().creativeMode) {
                ItemStack itemStack = new ItemStack(returns);
                PlayerEntity playerEntity = (PlayerEntity)user;
                if (!playerEntity.getInventory().insertStack(itemStack)) {
                    playerEntity.dropItem(itemStack, false);
                }
            }

            return stack;
        }
    }

    public int getMaxUseTime(ItemStack stack) {
        return eating_time;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        if (lore != null) {
            if (lore.size() > 0) {
                for (int i = 0; i < lore.size(); i++) {
                    tooltip.add(new LiteralText(lore.get(i)).formatted(Formatting.GRAY));
                }
            }
        }
    }

}
