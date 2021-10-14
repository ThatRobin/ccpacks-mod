package io.github.ThatRobin.ccpacks.mixin;

import io.github.ThatRobin.ccpacks.CCPacksMain;
import net.minecraft.client.render.entity.ParrotEntityRenderer;
import net.minecraft.entity.passive.ParrotEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParrotEntityRenderer.class)
public class ParrotEntityRendererMixin {

    @Inject(method = "getTexture", at = @At(value = "HEAD"), cancellable = true)
    public void getTexture(ParrotEntity parrotEntity, CallbackInfoReturnable ci) {
        if(parrotEntity.hasCustomName() && "robin".equals(parrotEntity.getName().asString())) {
            ci.setReturnValue(CCPacksMain.identifier("textures/entity/parrot/robin.png"));
        }
        return;
    }
}
