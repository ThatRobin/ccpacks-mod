package io.github.thatrobin.ccpacks.commands;

import com.mojang.brigadier.CommandDispatcher;
import io.github.thatrobin.ccpacks.CCPacksMain;
import io.github.thatrobin.ccpacks.component.BlockMechanicHolder;
import io.github.thatrobin.ccpacks.factories.mechanic_factories.MechanicType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockPos;

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
