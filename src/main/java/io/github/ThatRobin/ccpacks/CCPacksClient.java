package io.github.ThatRobin.ccpacks;

import io.github.ThatRobin.ccpacks.networkin.ModPacketsServerToClient;
import io.github.ThatRobin.ccpacks.registries.CCPackClientRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class CCPacksClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModPacketsServerToClient.register();
        new CCPackClientRegistry();
    }
}
