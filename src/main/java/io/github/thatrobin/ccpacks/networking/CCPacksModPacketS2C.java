package io.github.thatrobin.ccpacks.networking;

import io.github.thatrobin.ccpacks.choice.Choice;
import io.github.thatrobin.ccpacks.choice.ChoiceLayer;
import io.github.thatrobin.ccpacks.choice.ChoiceLayers;
import io.github.thatrobin.ccpacks.choice.ChoiceRegistry;
import io.github.thatrobin.ccpacks.component.ChoiceComponent;
import io.github.thatrobin.ccpacks.component.ModComponents;
import io.github.thatrobin.ccpacks.screen.ChooseChoiceScreen;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class CCPacksModPacketS2C {

    @Environment(EnvType.CLIENT)
    public static void register() {
        ClientPlayConnectionEvents.INIT.register(((clientPlayNetworkHandler, minecraftClient) -> {
            ClientPlayNetworking.registerReceiver(CCPacksModPackets.CONFIRM_CHOICE, CCPacksModPacketS2C::chosenPowers);
            ClientPlayNetworking.registerReceiver(CCPacksModPackets.OPEN_CHOICE_SCREEN, CCPacksModPacketS2C::openChoiceScreen);
        }));
    }

    @Environment(EnvType.CLIENT)
    private static void openChoiceScreen(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
        boolean showDirtBackground = packetByteBuf.readBoolean();
        Identifier choiceLayer = packetByteBuf.readIdentifier();
        minecraftClient.execute(() -> {
            ArrayList<ChoiceLayer> layers = new ArrayList<>();
            ChoiceLayer layer = ChoiceLayers.getLayer(choiceLayer);
            if(layer.isEnabled()) {
                layers.add(layer);
            }
            minecraftClient.setScreen(new ChooseChoiceScreen(layers,0, showDirtBackground));
        });
    }

    @Environment(EnvType.CLIENT)
    private static void chosenPowers(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
        ChoiceLayer layer = ChoiceLayers.getLayer(packetByteBuf.readIdentifier());
        Choice choice = ChoiceRegistry.get(packetByteBuf.readIdentifier());
        ActionFactory<Entity>.Instance action = choice.getAction();
        if(action != null) {
            action.accept(minecraftClient.player);
        }
        minecraftClient.execute(() -> {
            assert minecraftClient.player != null;
            ChoiceComponent component = ModComponents.CHOICE.get(minecraftClient.player);
            component.setChoice(layer, choice);
        });
    }

}
