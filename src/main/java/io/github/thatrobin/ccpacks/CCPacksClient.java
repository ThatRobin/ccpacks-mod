package io.github.thatrobin.ccpacks;

import io.github.thatrobin.ccpacks.networking.CCPacksModPacketS2C;
import io.github.thatrobin.ccpacks.registries.ClientContentManager;
import io.github.thatrobin.ccpacks.util.OnLoadResourceManager;
import net.fabricmc.api.ClientModInitializer;

public class CCPacksClient implements ClientModInitializer {

	public static boolean isServerRunningCCPacks = false;

	@Override
	public void onInitializeClient() {
		OnLoadResourceManager.addSingleListener(new ClientContentManager());
		CCPacksModPacketS2C.register();
	}

}

