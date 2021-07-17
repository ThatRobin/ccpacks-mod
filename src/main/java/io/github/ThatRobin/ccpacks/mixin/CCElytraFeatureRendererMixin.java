package io.github.ThatRobin.ccpacks.mixin;

import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.ThatRobin.ccpacks.CustomElytraFlightPower;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.mixin.ElytraFeatureRendererMixin;
import io.github.apace100.apoli.power.ElytraFlightPower;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ElytraFeatureRenderer.class)
public class CCElytraFeatureRendererMixin {

    @Mutable
    @Shadow @Final private static Identifier SKIN;

    @Inject(method = "render", at = @At(value = "HEAD"))
    private void modifyEquippedStackToElytra(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, LivingEntity livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
        List<CustomElytraFlightPower> powers = PowerHolderComponent.getPowers(livingEntity, CustomElytraFlightPower.class);
        CCPacksMain.LOGGER.info(powers);
        powers.forEach((power) -> {
            CCPacksMain.LOGGER.info(power);
            if(power.shouldRenderElytra() && !livingEntity.isInvisible()) {
                CCPacksMain.LOGGER.info(power.getElytraTexture());
                this.SKIN = power.getElytraTexture();
            }
        });

    }

}
