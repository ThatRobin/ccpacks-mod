package io.github.connor3001.ccpacks.mixin;

import io.github.connor3001.ccpacks.CCPacksMain;
import net.minecraft.resource.*;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(value = MinecraftServer.class, priority = 1001) // after Fabric
public class MinecraftServerMixin {


    @Inject(method = "loadDataPacks", at = @At(value = "HEAD"))
    private static void loadDataPacks(ResourcePackManager resourcePackManager, DataPackSettings dataPackSettings, boolean safeMode, CallbackInfoReturnable<DataPackSettings> cir) {
        Set<ResourcePackProvider> pack = ((AccessorMixin)resourcePackManager).getProviders();
        pack.add(
                new FileResourcePackProvider(
                        CCPacksMain.DATAPACKS_PATH.toFile(),
                        CCPacksMain.RESOURCE_PACK_SOURCE
                )
        );

        ((AccessorMixin) resourcePackManager).setProviders(pack);
    }
}