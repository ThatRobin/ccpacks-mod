package io.github.ThatRobin.ccpacks;

import io.github.ThatRobin.ccpacks.Networking.CCPacksModPacketS2C;
import net.fabricmc.api.ClientModInitializer;

public class CCPacksClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		CCPacksModPacketS2C.register();
	}

}

