package io.github.thatrobin.ccpacks.networking;

import io.github.thatrobin.ccpacks.CCPacksClient;
import io.github.thatrobin.ccpacks.CCPacksMain;
import io.github.thatrobin.ccpacks.factories.mechanic_factories.MechanicFactory;
import io.github.thatrobin.ccpacks.factories.mechanic_factories.MechanicRegistry;
import io.github.thatrobin.ccpacks.factories.mechanic_factories.MechanicType;
import io.github.thatrobin.ccpacks.registries.CCPacksRegistries;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class CCPacksModPacketS2C {

    @Environment(EnvType.CLIENT)
    public static void register() {
        ClientLoginNetworking.registerGlobalReceiver(CCPacksModPackets.HANDSHAKE, CCPacksModPacketS2C::handleHandshake);
        ClientPlayConnectionEvents.INIT.register(((clientPlayNetworkHandler, minecraftClient) -> {
            ClientPlayNetworking.registerReceiver(CCPacksModPackets.MECHANIC_LIST, CCPacksModPacketS2C::receiveMechanicList);
        }));
    }

    @Environment(EnvType.CLIENT)
    private static void receiveMechanicList(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
        int powerCount = packetByteBuf.readInt();
        HashMap<Identifier, MechanicType> factories = new HashMap<>();
        for(int i = 0; i < powerCount; i++) {
            Identifier mechanicId = packetByteBuf.readIdentifier();
            Identifier factoryId = packetByteBuf.readIdentifier();
            try {
                MechanicFactory factory = CCPacksRegistries.MECHANIC_FACTORY.get(factoryId);
                MechanicFactory.Instance factoryInstance = factory.read(packetByteBuf);
                MechanicType type = new MechanicType(mechanicId, factoryInstance);
                factories.put(mechanicId, type);
            } catch(Exception e) {
                CCPacksMain.LOGGER.error("Error while receiving \"" + mechanicId + "\" (factory: \"" + factoryId + "\"): " + e.getMessage());
                e.printStackTrace();
            }
        }
        minecraftClient.execute(() -> {
            MechanicRegistry.clear();
            factories.forEach(MechanicRegistry::register);
        });
    }


    @Environment(EnvType.CLIENT)
    private static CompletableFuture<PacketByteBuf> handleHandshake(MinecraftClient minecraftClient, ClientLoginNetworkHandler clientLoginNetworkHandler, PacketByteBuf packetByteBuf, Consumer<GenericFutureListener<? extends Future<? super Void>>> genericFutureListenerConsumer) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(CCPacksMain.SEMVER.length);
        for(int i = 0; i < CCPacksMain.SEMVER.length; i++) {
            buf.writeInt(CCPacksMain.SEMVER[i]);
        }
        CCPacksClient.isServerRunningCCPacks = true;
        return CompletableFuture.completedFuture(buf);
    }

}
