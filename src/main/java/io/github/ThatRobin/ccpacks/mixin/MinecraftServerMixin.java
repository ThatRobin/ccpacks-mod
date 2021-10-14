package io.github.ThatRobin.ccpacks.mixin;

import io.github.ThatRobin.ccpacks.CCPacksMain;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.DataPackSettings;
import net.minecraft.resource.FileResourcePackProvider;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.util.Set;

@Mixin(value = MinecraftServer.class, priority = 1001) // after Fabric
public class MinecraftServerMixin {


    @Inject(method = "loadDataPacks", at = @At(value = "HEAD"))
    private static void loadDataPacks(ResourcePackManager resourcePackManager, DataPackSettings dataPackSettings, boolean safeMode, CallbackInfoReturnable<DataPackSettings> cir) {
        File DATAPACKS_PATH = FabricLoader.getInstance().getGameDirectory().toPath().resolve("mods").toFile();
        Set<ResourcePackProvider> pack = ((AccessorMixin)resourcePackManager).getProviders();
        pack.add(
                new FileResourcePackProvider(
                        DATAPACKS_PATH,
                        CCPacksMain.RESOURCE_PACK_SOURCE
                )
        );

        ((AccessorMixin) resourcePackManager).setProviders(pack);
    }
}
