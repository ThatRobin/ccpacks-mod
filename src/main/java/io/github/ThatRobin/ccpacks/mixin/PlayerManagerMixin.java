package io.github.ThatRobin.ccpacks.mixin;

import io.github.ThatRobin.ccpacks.util.UniversalPowerRegistry;
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

    @Shadow public abstract ServerWorld getServerWorld();

    @Inject(method = "onSpawn()V", at = @At(value = "TAIL"))
    private void createPlayer(CallbackInfo ci) {
        for(int i = 0; i < this.getServerWorld().getPlayers().size(); i++){
            PowerHolderComponent component = PowerHolderComponent.KEY.get(this.getServerWorld().getPlayers().get(i));
            UniversalPowerRegistry.entries().forEach((up -> {
                up.getValue().powerTypes.forEach(powerType -> {
                    component.addPower(powerType, new Identifier("ccpacks", "universal_powers"));
                });
            }));


        }
    }
}
