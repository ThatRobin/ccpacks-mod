package io.github.thatrobin.ccpacks.networking;

import io.github.thatrobin.ccpacks.CCPacksMain;
import io.github.thatrobin.ccpacks.choice.Choice;
import io.github.thatrobin.ccpacks.choice.ChoiceLayer;
import io.github.thatrobin.ccpacks.choice.ChoiceLayers;
import io.github.thatrobin.ccpacks.choice.ChoiceRegistry;
import io.github.thatrobin.ccpacks.component.ChoiceComponent;
import io.github.thatrobin.ccpacks.component.ModComponents;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class CCPacksModPacketC2S {

    public static void register() {
        ServerLoginConnectionEvents.QUERY_START.register(CCPacksModPacketC2S::handshake);
        ServerLoginNetworking.registerGlobalReceiver(CCPacksModPackets.HANDSHAKE, CCPacksModPacketC2S::handleHandshakeReply);
        ServerPlayNetworking.registerGlobalReceiver(CCPacksModPackets.CHOOSE_CHOICE, CCPacksModPacketC2S::choosePowers);
    }

    private static void choosePowers(MinecraftServer minecraftServer, ServerPlayerEntity playerEntity, ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
        String originId = packetByteBuf.readString(32767);
        String layerId = packetByteBuf.readString(32767);
        minecraftServer.execute(() -> {
            ChoiceComponent component = ModComponents.CHOICE.get(playerEntity);
            ChoiceLayer layer = ChoiceLayers.getLayer(Identifier.tryParse(layerId));
            if(!component.hasAllChoices() && !component.hasChoice(layer)) {
                Identifier id = Identifier.tryParse(originId);
                if(id != null) {
                    Choice choice = ChoiceRegistry.get(id);
                    if(layer.contains(choice)) {
                        component.setChoice(layer, choice);
                        component.checkAutoChoosingLayers(playerEntity);
                        component.sync();
                        CCPacksMain.LOGGER.info("Player " + playerEntity.getDisplayName().asString() + " selected Choice: " + id + ", for layer: " + layerId);
                    } else {
                        CCPacksMain.LOGGER.info("Player " + playerEntity.getDisplayName().asString() + " tried to select unchoosable Choice for layer " + layerId + ": " + id + ".");
                        component.setChoice(layer, Choice.EMPTY);
                    }
                    confirmChoice(playerEntity, layer, component.getChoice(layer));
                    component.sync();
                } else {
                    CCPacksMain.LOGGER.warn("Player " + playerEntity.getDisplayName().asString() + " selected unknown Choice: " + id);
                }
            } else {
                CCPacksMain.LOGGER.warn("Player " + playerEntity.getDisplayName().asString() + " tried to select Choice for layer " + layerId + " while having one already.");
            }
        });
    }

    private static void handleHandshakeReply(MinecraftServer minecraftServer, ServerLoginNetworkHandler serverLoginNetworkHandler, boolean understood, PacketByteBuf packetByteBuf, ServerLoginNetworking.LoginSynchronizer loginSynchronizer, PacketSender packetSender) {
        if (understood) {
            int clientSemVerLength = packetByteBuf.readInt();
            int[] clientSemVer = new int[clientSemVerLength];
            boolean mismatch = clientSemVerLength != CCPacksMain.SEMVER.length;
            for(int i = 0; i < clientSemVerLength; i++) {
                clientSemVer[i] = packetByteBuf.readInt();
                if(i < clientSemVerLength - 1 && clientSemVer[i] != CCPacksMain.SEMVER[i]) {
                    mismatch = true;
                }
            }
            if(mismatch) {
                StringBuilder clientVersionString = new StringBuilder();
                for(int i = 0; i < clientSemVerLength; i++) {
                    clientVersionString.append(clientSemVer[i]);
                    if(i < clientSemVerLength - 1) {
                        clientVersionString.append(".");
                    }
                }
                serverLoginNetworkHandler.disconnect(new TranslatableText("choices.gui.version_mismatch", CCPacksMain.VERSION, clientVersionString));
            }
        } else {
            serverLoginNetworkHandler.disconnect(new LiteralText("This server requires you to install CCPacks (v" + CCPacksMain.VERSION + ") to play."));
        }
    }

    private static void confirmChoice(ServerPlayerEntity player, ChoiceLayer layer, Choice choice) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeIdentifier(layer.getIdentifier());
        buf.writeIdentifier(choice.getIdentifier());
        ActionFactory<Entity>.Instance action = choice.getAction();
        if(action != null) {
            action.accept(player);
        }
        ServerPlayNetworking.send(player, CCPacksModPackets.CONFIRM_CHOICE, buf);
    }

    private static void handshake(ServerLoginNetworkHandler serverLoginNetworkHandler, MinecraftServer minecraftServer, PacketSender packetSender, ServerLoginNetworking.LoginSynchronizer loginSynchronizer) {
        packetSender.sendPacket(CCPacksModPackets.HANDSHAKE, PacketByteBufs.empty());
    }

}
