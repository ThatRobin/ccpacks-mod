package io.github.ThatRobin.ccpacks;

import io.github.ThatRobin.ccpacks.Registries.CCPackServerRegistry;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.SERVER)
public class CCPacksServer implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        new CCPackServerRegistry();
    }
}
