package io.github.ThatRobin.ccpacks.Commands;

import com.mojang.brigadier.CommandDispatcher;
import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.ThatRobin.ccpacks.Choice.Choice;
import io.github.ThatRobin.ccpacks.Choice.ChoiceLayer;
import io.github.ThatRobin.ccpacks.Component.BlockMechanicHolder;
import io.github.ThatRobin.ccpacks.Component.ChoiceComponent;
import io.github.ThatRobin.ccpacks.Component.ModComponents;
import io.github.ThatRobin.ccpacks.DataDrivenClasses.Blocks.DDBlockEntity;
import io.github.ThatRobin.ccpacks.Factories.MechanicFactories.MechanicType;
import io.github.ThatRobin.ccpacks.Networking.CCPacksModPackets;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.Collection;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class MechanicCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("mechanic").requires(cs -> cs.hasPermissionLevel(2))
                        .then(literal("grant").then(argument("pos", BlockPosArgumentType.blockPos()).then(argument("mechanic", MechanicArgument.mechanic()).executes((command) -> {
                                try {
                                    MechanicType mechanicType = MechanicArgument.mechanic().getMechanic(command, "mechanic");
                                    BlockPos block = BlockPosArgumentType.getBlockPos(command, "pos");
                                    BlockEntity blockEntity = command.getSource().getWorld().getBlockEntity(block);
                                    BlockMechanicHolder.KEY.get(blockEntity).addMechanic(mechanicType);
                                    return 1;
                                } catch (Exception e) {
                                    CCPacksMain.LOGGER.info(e.getMessage());
                                }
                            return 0;
                        })))));
    }


}
