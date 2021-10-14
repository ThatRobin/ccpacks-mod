package io.github.ThatRobin.ccpacks;

import io.github.ThatRobin.ccpacks.Networking.CCPacksModPacketS2C;
import net.fabricmc.api.ClientModInitializer;

public class CCPacksClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		//LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
		//	if (entityRenderer instanceof PlayerEntityRenderer) {
		//		registrationHelper.register(new PowerFeatureRenderer((PlayerEntityRenderer) entityRenderer));
		//	}
		//});
		CCPacksModPacketS2C.register();
	}

}

