package io.github.ThatRobin.ccpacks.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ItemCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.SummonCommand;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.Registry;

import java.util.Collection;
import java.util.Iterator;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ItemActionCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("itemaction").requires(cs -> cs.hasPermissionLevel(2))
                        .then(literal("summon")
                                .then(((RequiredArgumentBuilder) CommandManager.argument("pos", Vec3ArgumentType.vec3()))
                                    .then(argument("target", EntityArgumentType.entity())
                                            .then((RequiredArgumentBuilder)CommandManager.argument("sourceSlot", ItemSlotArgumentType.itemSlot())
                                                .then((RequiredArgumentBuilder)CommandManager.argument("pickupDelay", IntegerArgumentType.integer())
                                                        .executes((command) -> {
                                                            try {
                                                                double x = Vec3ArgumentType.getPosArgument(command, "pos").toAbsolutePos(command.getSource()).x;
                                                                double y = Vec3ArgumentType.getPosArgument(command, "pos").toAbsolutePos(command.getSource()).y;
                                                                double z = Vec3ArgumentType.getPosArgument(command, "pos").toAbsolutePos(command.getSource()).z;
                                                                Entity entity = EntityArgumentType.getEntity(command, "target");
                                                                int slotId = ItemSlotArgumentType.getItemSlot(command, "sourceSlot");
                                                                ItemEntity ientity = new ItemEntity(command.getSource().getWorld(), x, y, z, getStackInSlot(entity,slotId));
                                                                ientity.setPickupDelay(IntegerArgumentType.getInteger(command, "pickupDelay"));
                                                                command.getSource().getWorld().spawnEntity(ientity);
                                                                return 1;
                                                            } catch (Exception e){
                                                                command.getSource().sendFeedback(new LiteralText("Could not set attribute to scoreboard value"), true);
                                                                return 0;
                                                            }
                                                        })))))));

    }

    private static final DynamicCommandExceptionType NO_SUCH_SLOT_SOURCE_EXCEPTION = new DynamicCommandExceptionType((slot) -> {
        return new TranslatableText("commands.item.source.no_such_slot", new Object[]{slot});
    });

    private static ItemStack getStackInSlot(Entity entity, int slotId) throws CommandSyntaxException {
        StackReference stackReference = entity.getStackReference(slotId);
        if (stackReference == StackReference.EMPTY) {
            throw NO_SUCH_SLOT_SOURCE_EXCEPTION.create(slotId);
        } else {
            return stackReference.get().copy();
        }
    }
}
