package io.github.thatrobin.ccpacks;

import io.github.apace100.apoli.ApoliClient;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.networking.ModPackets;
import io.github.apace100.apoli.power.Active;
import io.github.apace100.apoli.power.Power;
import io.github.thatrobin.ccpacks.networking.CCPacksModPacketS2C;
import io.github.thatrobin.ccpacks.networking.CCPacksModPackets;
import io.github.thatrobin.ccpacks.registries.ClientContentManager;
import io.github.thatrobin.ccpacks.util.OnLoadResourceManager;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.network.PacketByteBuf;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CCPacksClient implements ClientModInitializer {

	public static boolean isServerRunningCCPacks = false;

	@Override
	public void onInitializeClient() {
		OnLoadResourceManager.addSingleListener(new ClientContentManager());
		CCPacksModPacketS2C.register();
	}

}

