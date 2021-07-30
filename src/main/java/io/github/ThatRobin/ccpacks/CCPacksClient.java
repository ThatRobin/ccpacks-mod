package io.github.ThatRobin.ccpacks;

import io.github.ThatRobin.ccpacks.registries.CCPackClientRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemGroup;

@Environment(EnvType.CLIENT)
public class CCPacksClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        new CCPackClientRegistry();
    }
}
