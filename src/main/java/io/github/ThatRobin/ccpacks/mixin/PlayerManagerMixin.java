package io.github.ThatRobin.ccpacks.Mixin;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerPlayerEntity.class, priority = 800)
public abstract class PlayerManagerMixin {

    @Shadow public abstract ServerWorld getServerWorld();

    @Inject(method = "onSpawn()V", at = @At(value = "TAIL"))
    private void createPlayer(CallbackInfo ci) {
        for(int i = 0; i < this.getServerWorld().getPlayers().size(); i++){
            if(!this.getServerWorld().getPlayers().get(i).hasStatusEffect(Registry.STATUS_EFFECT.get(new Identifier("ccpacks","universal_powers")))) {
                this.getServerWorld().getPlayers().get(i).addStatusEffect(new StatusEffectInstance(Registry.STATUS_EFFECT.get(new Identifier("ccpacks","universal_powers")), 20, 0, true, false, false));
            }
        }
    }
}
