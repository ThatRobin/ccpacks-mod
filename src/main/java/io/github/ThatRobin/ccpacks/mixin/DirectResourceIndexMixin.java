package io.github.ThatRobin.ccpacks.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.resource.DirectResourceIndex;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;

@Mixin(value = DirectResourceIndex.class)
public class DirectResourceIndexMixin {

    @Mutable
    @Shadow @Final private File assetDir;

    @Inject(method = "getResource", at = @At(value = "HEAD"), cancellable = true)
    private void spawnEntity(Identifier identifier, CallbackInfoReturnable<File> cir) {
        this.assetDir = FabricLoader.getInstance().getGameDirectory().toPath().resolve("resourcepacks").toFile();
        cir.setReturnValue(new File(this.assetDir, identifier.toString().replace(':', '/')));
    }
}
