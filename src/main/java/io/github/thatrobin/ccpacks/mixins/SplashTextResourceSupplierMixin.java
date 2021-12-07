package io.github.thatrobin.ccpacks.mixins;

import net.minecraft.client.resource.SplashTextResourceSupplier;
import net.minecraft.client.util.Session;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(SplashTextResourceSupplier.class)
public class SplashTextResourceSupplierMixin {

    @Shadow @Final private List<String> splashTexts;

    @Shadow @Final private Session session;

    @Inject(method = "apply", at = @At(value = "TAIL"), cancellable = true)
    protected void apply(List<String> list, ResourceManager resourceManager, Profiler profiler, CallbackInfo ci) {
        this.splashTexts.add("CCPacks is Cool!");
    }

    @Inject(method = "get", at = @At(value = "HEAD"), cancellable = true)
    protected void get(CallbackInfoReturnable<String> cir) {
        if(this.session.getUsername().equals("ThatRobin3001")){
            cir.setReturnValue("The Great Wasp Massacre of 1996");
        }
        if(this.session.getUsername().equals("PixelNinja5467")){
            cir.setReturnValue("Go and work on Birds and Beasts");
        }
    }
}
