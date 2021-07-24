package io.github.ThatRobin.ccpacks.mixin;

import io.github.ThatRobin.ccpacks.CCPacksMain;
import net.minecraft.resource.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mixin(FileResourcePackProvider.class)
public abstract class FileResourcePackProviderMixin {


    @Shadow @Final private static FileFilter POSSIBLE_PACK;

    @Shadow @Final private File packsFolder;

    @Shadow protected abstract Supplier<ResourcePack> createResourcePack(File file);

    @Shadow @Final private ResourcePackSource source;

    @Inject(method = "register", at = @At(value = "HEAD"), cancellable = true)
    private void register(Consumer<ResourcePackProfile> profileAdder, ResourcePackProfile.Factory factory, CallbackInfo ci) {
        File[] files = this.packsFolder.listFiles(this.POSSIBLE_PACK);
        if (files != null) {
            File[] var4 = files;
            int var5 = files.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                File file = var4[var6];
                String string = "file/" + file.getName();
                Path test = Path.of(file.getPath(),"ccdata");
                ResourcePackProfile resourcePackProfile;
                if(test.toFile().exists()) {
                    resourcePackProfile = ResourcePackProfile.of(string, true, this.createResourcePack(file), factory, ResourcePackProfile.InsertionPosition.TOP, ResourcePackSource.PACK_SOURCE_BUILTIN);
                } else {
                    resourcePackProfile = ResourcePackProfile.of(string, false, this.createResourcePack(file), factory, ResourcePackProfile.InsertionPosition.TOP, this.source);
                }
                if (resourcePackProfile != null) {
                    profileAdder.accept(resourcePackProfile);
                }
            }

        }
        ci.cancel();
    }
}
