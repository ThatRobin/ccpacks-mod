package io.github.ThatRobin.ccpacks.mixin;

import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.item.ArmorItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorFeatureRenderer.class)
public class ArmorRendererMixin {

    //@Inject(method = "getArmorTexture", at = @At("HEAD"), cancellable = true)
    private void getArmorTexture(ArmorItem item, boolean legs, String overlay, CallbackInfoReturnable<Identifier> cir) {
        String var10000 = item.getMaterial().getName();
        String string = "textures/models/armor/" + var10000 + "_layer_" + (legs ? 2 : 1) + (overlay == null ? "" : "_" + overlay) + ".png";;
        if (var10000 != "leather" || var10000 != "chainmail" || var10000 != "iron" || var10000 != "gold" || var10000 != "diamond" || var10000 != "turtle" || var10000 != "netherite") {
            cir.setReturnValue(new Identifier(string));
        } else {
            cir.setReturnValue(new Identifier("example_pack",string));
        }
        cir.getReturnValue();
    }
}
