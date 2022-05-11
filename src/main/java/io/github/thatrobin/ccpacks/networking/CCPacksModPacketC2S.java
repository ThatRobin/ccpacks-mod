package io.github.thatrobin.ccpacks.networking;

import io.github.thatrobin.ccpacks.CCPacksMain;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

public class CCPacksModPacketC2S {

    public static void register() {
        ServerLoginConnectionEvents.QUERY_START.register(CCPacksModPacketC2S::handshake);
        ServerLoginNetworking.registerGlobalReceiver(CCPacksModPackets.HANDSHAKE, CCPacksModPacketC2S::handleHandshakeReply);
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

    private static void handshake(ServerLoginNetworkHandler serverLoginNetworkHandler, MinecraftServer minecraftServer, PacketSender packetSender, ServerLoginNetworking.LoginSynchronizer loginSynchronizer) {
        packetSender.sendPacket(CCPacksModPackets.HANDSHAKE, PacketByteBufs.empty());
    }

}
