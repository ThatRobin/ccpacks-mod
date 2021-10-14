package io.github.ThatRobin.ccpacks.Networking;

import io.github.ThatRobin.ccpacks.Choice.ChoiceRegistry;
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class CCPacksModPacketC2S {

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(CCPacksModPackets.CHOOSE_CHOICE, CCPacksModPacketC2S::choosePowers);
    }

    private static void choosePowers(MinecraftServer minecraftServer, ServerPlayerEntity playerEntity, ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
        Identifier id = packetByteBuf.readIdentifier();
        Identifier id2 = packetByteBuf.readIdentifier();
        ChoiceRegistry.get(id).getPowerTypes().forEach(powerType -> {
            minecraftServer.execute(() -> {
                PowerHolderComponent component = PowerHolderComponent.KEY.get(playerEntity);
                component.addPower(powerType, id);
                confirmPowers(playerEntity, id, id2);
                component.sync();
            });
        });
    }

    private static void confirmPowers(ServerPlayerEntity player, Identifier identifier, Identifier identifier2) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeIdentifier(identifier);
        buf.writeIdentifier(identifier2);
        ServerPlayNetworking.send(player, CCPacksModPackets.CONFIRM_POWERS, buf);
    }

}
