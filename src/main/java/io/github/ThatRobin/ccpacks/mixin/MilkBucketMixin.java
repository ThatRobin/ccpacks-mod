package io.github.ThatRobin.ccpacks.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MilkBucketItem;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(MilkBucketItem.class)
public class MilkBucketMixin {

    @Inject(method = "finishUsing", at = @At(value = "TAIL", target = "Lnet/minecraft/entity/LivingEntity;clearStatusEffects()Z"))
    public void injectEffects(ItemStack stack, World world, LivingEntity entity, CallbackInfoReturnable<ItemStack> cir) {
        Map<StatusEffect, StatusEffectInstance> map = entity.getActiveStatusEffects();

        StatusEffect effect = Registry.STATUS_EFFECT.get(new Identifier("ccpacks","universal_powers"));

        map.put(effect, new StatusEffectInstance(Registry.STATUS_EFFECT.get(new Identifier("ccpacks","universal_powers")),
                        99999,
                        0,
                        true,
                        false,
                        false));
        if (entity instanceof ServerPlayerEntity player) {
            player.networkHandler.sendPacket(new EntityStatusEffectS2CPacket(player.getId(), map.get(effect)));
        }

    }
}
