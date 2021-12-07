package io.github.thatrobin.ccpacks;

import io.github.thatrobin.ccpacks.networking.CCPacksModPacketS2C;
import net.fabricmc.api.ClientModInitializer;

public class CCPacksClient implements ClientModInitializer {



	@Override
	public void onInitializeClient() {
		CCPacksModPacketS2C.register();
	}

}

