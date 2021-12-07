package io.github.thatrobin.ccpacks.networking;

import io.github.thatrobin.ccpacks.CCPacksMain;
import io.github.thatrobin.ccpacks.choice.Choice;
import io.github.thatrobin.ccpacks.choice.ChoiceLayer;
import io.github.thatrobin.ccpacks.choice.ChoiceLayers;
import io.github.thatrobin.ccpacks.choice.ChoiceRegistry;
import io.github.thatrobin.ccpacks.component.ChoiceComponent;
import io.github.thatrobin.ccpacks.component.ModComponents;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
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
        Identifier choiceId = packetByteBuf.readIdentifier();
        Identifier layerId = packetByteBuf.readIdentifier();
        Choice choice = ChoiceRegistry.get(choiceId);
        ActionFactory<Entity>.Instance action = choice.getAction();
        if(action != null) {
            action.accept(playerEntity);
        }
        minecraftServer.execute(() -> {
            ChoiceLayer layer = ChoiceLayers.getLayer(layerId);
            ChoiceComponent component = ModComponents.CHOICE.get(playerEntity);
            if(!component.hasAllChoices()) {
                if(choiceId != null) {
                    if(layer.contains(choice)) {
                        component.setChoice(layer, choice);
                        component.sync();
                        CCPacksMain.LOGGER.info("Player " + playerEntity.getDisplayName().asString() + " selected Choice: " + choiceId + ", for layer: " + layerId);
                    } else {
                        CCPacksMain.LOGGER.info("Player " + playerEntity.getDisplayName().asString() + " tried to select unchoosable Choice for layer " + layerId + ": " + choiceId + ".");
                        component.setChoice(layer, Choice.EMPTY);
                    }
                    confirmChoice(playerEntity, layer, component.getChoice(layer));
                    component.sync();
                } else {
                    CCPacksMain.LOGGER.warn("Player " + playerEntity.getDisplayName().asString() + " selected unknown Choice: " + choiceId);
                }
            } else {
                CCPacksMain.LOGGER.warn("Player " + playerEntity.getDisplayName().asString() + " tried to select Choice for layer " + layerId + " while having one already.");
            }
            component.setChoice(layer, choice);
            confirmChoice(playerEntity, layer, choice);
            component.sync();
        });
    }

    private static void confirmChoice(ServerPlayerEntity player, ChoiceLayer layer, Choice choice) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeIdentifier(layer.getIdentifier());
        buf.writeIdentifier(choice.getIdentifier());
        ServerPlayNetworking.send(player, CCPacksModPackets.CONFIRM_CHOICE, buf);
    }

}
