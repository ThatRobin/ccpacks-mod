package io.github.ThatRobin.ccpacks.Mixins;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.*;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.util.Set;

@Mixin(value = MinecraftServer.class, priority = 1001) // after Fabric
public class MinecraftServerMixin {

    private static final ResourcePackSource RESOURCE_PACK_SOURCE = ResourcePackSource.nameAndSource("pack.source.ccpacks");


    @Inject(method = "loadDataPacks", at = @At(value = "HEAD"))
    private static void loadDataPacks(ResourcePackManager resourcePackManager, DataPackSettings dataPackSettings, boolean safeMode, CallbackInfoReturnable<DataPackSettings> cir) {
        File DATAPACKS_PATH = FabricLoader.getInstance().getGameDirectory().toPath().resolve("mods").toFile();
        Set<ResourcePackProvider> pack = ((AccessorMixin)resourcePackManager).getProviders();
        pack.add(
                new FileResourcePackProvider(
                        DATAPACKS_PATH,
                        RESOURCE_PACK_SOURCE
                )
        );

        ((AccessorMixin) resourcePackManager).setProviders(pack);
    }
}
