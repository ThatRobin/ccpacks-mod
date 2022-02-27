package io.github.thatrobin.ccpacks.mixins;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.thatrobin.ccpacks.CCPacksMain;
import io.github.thatrobin.ccpacks.registries.UniversalPowerRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void spawnEntity(CallbackInfo ci) {
        LivingEntity entity = ((LivingEntity)(Object)this);
        PowerHolderComponent component = PowerHolderComponent.KEY.get(entity);
        UniversalPowerRegistry.entries().forEach((up -> {
            if(up.getValue().condition.test(entity)) {
                up.getValue().powerTypes.forEach(power -> {
                    if(!component.hasPower(power)) {
                        component.addPower(power, CCPacksMain.identifier("universal_powers"));
                        component.sync();
                    }
                });
            } else {
                up.getValue().powerTypes.forEach(power -> {
                    if(component.hasPower(power)) {
                        component.removePower(power, CCPacksMain.identifier("universal_powers"));
                        component.sync();
                    }
                });
            }
        }));
    }

}
