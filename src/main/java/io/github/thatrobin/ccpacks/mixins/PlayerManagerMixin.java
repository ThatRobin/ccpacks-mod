package io.github.thatrobin.ccpacks.mixins;

import dev.onyxstudios.cca.api.v3.component.ComponentProvider;
import io.github.thatrobin.ccpacks.CCPacksMain;
import io.github.thatrobin.ccpacks.choice.Choice;
import io.github.thatrobin.ccpacks.choice.ChoiceLayers;
import io.github.thatrobin.ccpacks.choice.ChoiceRegistry;
import io.github.thatrobin.ccpacks.component.BlockMechanicHolder;
import io.github.thatrobin.ccpacks.component.ChoiceComponent;
import io.github.thatrobin.ccpacks.component.ModComponents;
import io.github.thatrobin.ccpacks.factories.mechanic_factories.MechanicFactory;
import io.github.thatrobin.ccpacks.factories.mechanic_factories.MechanicRegistry;
import io.github.thatrobin.ccpacks.factories.mechanic_factories.MechanicType;
import io.github.thatrobin.ccpacks.networking.CCPacksModPackets;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@SuppressWarnings("rawtypes")
@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    @Shadow public abstract List<ServerPlayerEntity> getPlayerList();

    @Inject(at = @At("TAIL"), method = "Lnet/minecraft/server/PlayerManager;onPlayerConnect(Lnet/minecraft/network/ClientConnection;Lnet/minecraft/server/network/ServerPlayerEntity;)V")
    private void openChoiceGui(ClientConnection connection, ServerPlayerEntity player, CallbackInfo info) {
        ChoiceComponent component = ModComponents.CHOICE.get(player);

        PacketByteBuf choiceListData = new PacketByteBuf(Unpooled.buffer());
        choiceListData.writeInt(ChoiceRegistry.size() - 1);
        ChoiceRegistry.entries().forEach((entry) -> {
            if(entry.getValue() != Choice.EMPTY) {
                choiceListData.writeIdentifier(entry.getKey());
                entry.getValue().write(choiceListData);
            }
        });

        PacketByteBuf choiceLayerData = new PacketByteBuf(Unpooled.buffer());
        choiceLayerData.writeInt(ChoiceLayers.size());
        ChoiceLayers.getLayers().forEach((layer) -> {
            layer.write(choiceLayerData);
            if(layer.isEnabled()) {
                if (!component.hasChoice(layer)) {
                    component.setChoice(layer, Choice.EMPTY);
                }
            }
        });
        ServerPlayNetworking.send(player, CCPacksModPackets.CHOICE_LIST, choiceListData);
        ServerPlayNetworking.send(player, CCPacksModPackets.LAYER_LIST, choiceLayerData);

        List<ServerPlayerEntity> playerList = getPlayerList();
        playerList.forEach(spe -> ModComponents.CHOICE.syncWith(spe, ComponentProvider.fromEntity(player)));
        ChoiceComponent.sync(player);
        component.sync();

        PacketByteBuf mechanicListData = new PacketByteBuf(Unpooled.buffer());
        mechanicListData.writeInt(MechanicRegistry.size());
        MechanicRegistry.entries().forEach((entry) -> {
            MechanicType<?> type = entry.getValue();
            MechanicFactory.Instance factory = type.getFactory();
            if(factory != null) {
                mechanicListData.writeIdentifier(entry.getKey());
                factory.write(mechanicListData);
            }
        });

        ServerPlayNetworking.send(player, CCPacksModPackets.MECHANIC_LIST, mechanicListData);
    }
}
