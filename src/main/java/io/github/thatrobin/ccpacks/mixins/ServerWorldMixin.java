package io.github.thatrobin.ccpacks.mixins;

import io.github.thatrobin.ccpacks.CCPacksMain;
import io.github.thatrobin.ccpacks.registries.UniversalPowerRegistry;
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ServerWorld.class)
public class ServerWorldMixin {

    @Inject(method = "spawnEntity", at = @At(value = "HEAD"))
    private void spawnEntity(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if(entity instanceof LivingEntity) {
            PowerHolderComponent component = PowerHolderComponent.KEY.get(entity);
            UniversalPowerRegistry.entries().forEach((up -> {
                if(up.getValue().entityTypes.contains(entity.getType())) {
                    up.getValue().powerTypes.forEach(powerType -> component.addPower(powerType, CCPacksMain.identifier("universal_powers")));
                }
            }));
        }
    }
}
