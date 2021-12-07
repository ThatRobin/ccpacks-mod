package io.github.thatrobin.ccpacks.mixins;

import io.github.thatrobin.ccpacks.registries.UniversalPowerRegistry;
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerPlayerEntity.class, priority = 800)
public abstract class PlayerManagerMixin {

    @Shadow public abstract ServerWorld getWorld();

    @Inject(method = "onSpawn()V", at = @At(value = "TAIL"))
    private void createPlayer(CallbackInfo ci) {
        for(int i = 0; i < this.getWorld().getPlayers().size(); i++){
            PowerHolderComponent component = PowerHolderComponent.KEY.get(this.getWorld().getPlayers().get(i));
            int finalI = i;
            UniversalPowerRegistry.entries().forEach((up -> {
                if(up.getValue().entityTypes.contains(this.getWorld().getPlayers().get(finalI).getType())) {
                    up.getValue().powerTypes.forEach(powerType -> {
                        component.addPower(powerType, new Identifier("ccpacks", "universal_powers"));
                        component.sync();
                    });
                }
            }));



        }
    }
}
