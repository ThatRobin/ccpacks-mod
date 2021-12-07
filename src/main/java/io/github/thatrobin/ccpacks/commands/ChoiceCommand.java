package io.github.thatrobin.ccpacks.commands;

import com.mojang.brigadier.CommandDispatcher;
import io.github.thatrobin.ccpacks.CCPacksMain;
import io.github.thatrobin.ccpacks.choice.Choice;
import io.github.thatrobin.ccpacks.choice.ChoiceLayer;
import io.github.thatrobin.ccpacks.component.ChoiceComponent;
import io.github.thatrobin.ccpacks.component.ModComponents;
import io.github.thatrobin.ccpacks.networking.CCPacksModPackets;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Collection;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ChoiceCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("choice").requires(cs -> cs.hasPermissionLevel(2))
                        .then(literal("gui").then(argument("targets", EntityArgumentType.entities()).then(argument("layer", LayerArgument.layer()).executes((command) -> {
                                try {
                                    ChoiceLayer l = LayerArgument.layer().getLayer(command, "layer");
                                    Collection<ServerPlayerEntity> targets = EntityArgumentType.getPlayers(command, "targets");

                                    targets.forEach(target -> {
                                        ChoiceComponent component = ModComponents.CHOICE.get(target);
                                        if (l.isEnabled()) {
                                            component.setChoice(l, Choice.EMPTY);
                                        }

                                        component.checkAutoChoosingLayers();
                                        component.sync();
                                        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
                                        data.writeBoolean(false);
                                        data.writeIdentifier(l.getIdentifier());
                                        ServerSidePacketRegistry.INSTANCE.sendToPlayer(target, CCPacksModPackets.OPEN_CHOICE_SCREEN, data);
                                    });

                                    return 1;
                                } catch (Exception e) {
                                    CCPacksMain.LOGGER.info(e.getMessage());
                                }
                            return 0;
                        })))));
    }


}
