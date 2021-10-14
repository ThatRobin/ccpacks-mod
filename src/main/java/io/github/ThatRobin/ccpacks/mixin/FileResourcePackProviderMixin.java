package io.github.ThatRobin.ccpacks.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.FileResourcePackProvider;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Mixin(FileResourcePackProvider.class)
public abstract class FileResourcePackProviderMixin {


    @Shadow @Final private static FileFilter POSSIBLE_PACK;

    @Shadow @Final private File packsFolder;

    @Shadow protected abstract Supplier<ResourcePack> createResourcePack(File file);

    @Shadow @Final private ResourcePackSource source;

    @Inject(method = "register", at = @At(value = "HEAD"), cancellable = true)
    private void register(Consumer<ResourcePackProfile> profileAdder, ResourcePackProfile.Factory factory, CallbackInfo ci) throws IOException {
        File DATAPACKS_PATH = FabricLoader.getInstance().getGameDirectory().toPath().resolve("mods").toFile();
        File[] files = DATAPACKS_PATH.listFiles(this.POSSIBLE_PACK);
        if (files != null) {
            for(int i = 0; i < files.length; ++i) {
                File file = files[i];
                String string = "file/" + file.getName();
                Path test = Path.of(file.getPath(),"data/ccpacks/content");
                ResourcePackProfile resourcePackProfile = null;
                if(file.getName().endsWith(".zip")) {
                    ZipFile zipFile = new ZipFile(file);
                    Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
                    String string3 = "data/ccpacks/content";
                    boolean ccpack = false;
                    while (enumeration.hasMoreElements()) {
                        ZipEntry zipEntry = enumeration.nextElement();
                        if (zipEntry.getName().startsWith(string3)) {
                            ccpack = true;
                        }
                    }
                    if(ccpack) {
                        resourcePackProfile = ResourcePackProfile.of(string, true, this.createResourcePack(file), factory, ResourcePackProfile.InsertionPosition.TOP, ResourcePackSource.PACK_SOURCE_BUILTIN);
                    }
                } else if(test.toFile().exists()) {
                    resourcePackProfile = ResourcePackProfile.of(string, true, this.createResourcePack(file), factory, ResourcePackProfile.InsertionPosition.TOP, ResourcePackSource.PACK_SOURCE_BUILTIN);
                }
                if (resourcePackProfile != null) {
                    profileAdder.accept(resourcePackProfile);
                }
            }

        }
    }
}
