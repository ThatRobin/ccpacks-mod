package io.github.ThatRobin.ccpacks.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.server.command.EffectCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

@Mixin(EffectCommand.class)
public class EffectCommandMixin {

    @Inject(method = "executeClear(Lnet/minecraft/server/command/ServerCommandSource;Ljava/util/Collection;)I", at = @At(value = "TAIL"))
    private static void executeClear(ServerCommandSource source, Collection<? extends Entity> targets, CallbackInfoReturnable<Integer> cir) throws CommandSyntaxException {
        Iterator var3 = targets.iterator();

        while (var3.hasNext()) {
            Entity entity = (Entity) var3.next();
            if (entity instanceof LivingEntity) {
                if (entity instanceof ServerPlayerEntity player) {
                    Map<StatusEffect, StatusEffectInstance> map = player.getActiveStatusEffects();
                    StatusEffect effect = Registry.STATUS_EFFECT.get(new Identifier("ccpacks", "universal_powers"));
                    map.put(effect, new StatusEffectInstance(Registry.STATUS_EFFECT.get(new Identifier("ccpacks", "universal_powers")),
                            99999,
                            0,
                            true,
                            false,
                            false));
                    player.networkHandler.sendPacket(new EntityStatusEffectS2CPacket(player.getId(), map.get(effect)));
                }
            }
        }
    }
}
