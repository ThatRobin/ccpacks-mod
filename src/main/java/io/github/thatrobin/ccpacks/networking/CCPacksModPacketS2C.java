package io.github.thatrobin.ccpacks.networking;

import io.github.apace100.calio.data.SerializableData;
import io.github.thatrobin.ccpacks.CCPacksClient;
import io.github.thatrobin.ccpacks.CCPacksMain;
import io.github.thatrobin.ccpacks.choice.Choice;
import io.github.thatrobin.ccpacks.choice.ChoiceLayer;
import io.github.thatrobin.ccpacks.choice.ChoiceLayers;
import io.github.thatrobin.ccpacks.choice.ChoiceRegistry;
import io.github.thatrobin.ccpacks.component.ChoiceComponent;
import io.github.thatrobin.ccpacks.component.ModComponents;
import io.github.thatrobin.ccpacks.factories.mechanic_factories.MechanicFactory;
import io.github.thatrobin.ccpacks.factories.mechanic_factories.MechanicRegistry;
import io.github.thatrobin.ccpacks.factories.mechanic_factories.MechanicType;
import io.github.thatrobin.ccpacks.registries.CCPacksRegistries;
import io.github.thatrobin.ccpacks.screen.ChooseChoiceScreen;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
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
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class CCPacksModPacketS2C {

    @Environment(EnvType.CLIENT)
    public static void register() {
        ClientLoginNetworking.registerGlobalReceiver(CCPacksModPackets.HANDSHAKE, CCPacksModPacketS2C::handleHandshake);
        ClientPlayConnectionEvents.INIT.register(((clientPlayNetworkHandler, minecraftClient) -> {
            ClientPlayNetworking.registerReceiver(CCPacksModPackets.MECHANIC_LIST, CCPacksModPacketS2C::receiveMechanicList);
            ClientPlayNetworking.registerReceiver(CCPacksModPackets.CONFIRM_CHOICE, CCPacksModPacketS2C::chosenPowers);
            ClientPlayNetworking.registerReceiver(CCPacksModPackets.OPEN_CHOICE_SCREEN, CCPacksModPacketS2C::openChoiceScreen);
            ClientPlayNetworking.registerReceiver(CCPacksModPackets.CHOICE_LIST, CCPacksModPacketS2C::receiveChoiceList);
            ClientPlayNetworking.registerReceiver(CCPacksModPackets.LAYER_LIST, CCPacksModPacketS2C::receiveLayerList);
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

    @Environment(EnvType.CLIENT)
    private static void openChoiceScreen(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
        boolean showDirtBackground = packetByteBuf.readBoolean();
        Identifier choiceLayer = packetByteBuf.readIdentifier();
        minecraftClient.execute(() -> {
            ArrayList<ChoiceLayer> layers = new ArrayList<>();
            ChoiceComponent component = ModComponents.CHOICE.get(minecraftClient.player);
            ChoiceLayer layer = ChoiceLayers.getLayer(choiceLayer);
            component.setChoice(layer, Choice.EMPTY);
            layers.add(layer);
            Collections.sort(layers);
            minecraftClient.setScreen(new ChooseChoiceScreen(layers,0, showDirtBackground));
        });
    }

    @Environment(EnvType.CLIENT)
    private static void receiveChoiceList(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
        try {
            Identifier[] ids = new Identifier[packetByteBuf.readInt()];
            SerializableData.Instance[] choices = new SerializableData.Instance[ids.length];
            for(int i = 0; i < choices.length; i++) {
                ids[i] = Identifier.tryParse(packetByteBuf.readString());
                choices[i] = Choice.DATA.read(packetByteBuf);
            }
            minecraftClient.execute(() -> {
                CCPacksClient.isServerRunningCCPacks = true;
                ChoiceRegistry.reset();
                for(int i = 0; i < ids.length; i++) {
                    ChoiceRegistry.register(ids[i], Choice.createFromData(ids[i], choices[i]));
                }
            });
        } catch (Exception e) {
            CCPacksMain.LOGGER.error(e);
        }
    }

    @Environment(EnvType.CLIENT)
    private static void receiveLayerList(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
        try {
            int layerCount = packetByteBuf.readInt();
            ChoiceLayer[] layers = new ChoiceLayer[layerCount];
            for(int i = 0; i < layerCount; i++) {
                layers[i] = ChoiceLayer.read(packetByteBuf);
            }
            minecraftClient.execute(() -> {
                ChoiceLayers.clear();
                for(int i = 0; i < layerCount; i++) {
                    ChoiceLayers.add(layers[i]);
                }
                CCPacksDataLoadedCallback.EVENT.invoker().onDataLoaded(true);
            });
        } catch (Exception e) {
            CCPacksMain.LOGGER.error(e);
        }
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
            ChoiceComponent component = ModComponents.CHOICE.get(minecraftClient.player);
            component.setChoice(layer, choice);
        });
    }

}
