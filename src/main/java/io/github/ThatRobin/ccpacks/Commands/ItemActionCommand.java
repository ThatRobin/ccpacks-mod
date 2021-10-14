package io.github.ThatRobin.ccpacks.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import io.github.ThatRobin.ccpacks.Component.ItemHolderComponent;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ItemActionCommand {

    private static final SuggestionProvider<ServerCommandSource> MODIFIER_SUGGESTION_PROVIDER = (context, builder) -> {
        LootFunctionManager lootFunctionManager = context.getSource().getServer().getItemModifierManager();
        return CommandSource.suggestIdentifiers(lootFunctionManager.getFunctionIds(), builder);
    };

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                (LiteralArgumentBuilder<ServerCommandSource>) literal("itemaction").requires(cs -> cs.hasPermissionLevel(2))
                        .then(literal("summon").then(((RequiredArgumentBuilder) CommandManager.argument("pos", Vec3ArgumentType.vec3())).then(argument("target", EntityArgumentType.entity()).then((RequiredArgumentBuilder)CommandManager.argument("sourceSlot", ItemSlotArgumentType.itemSlot()).then((RequiredArgumentBuilder)CommandManager.argument("pickupDelay", IntegerArgumentType.integer()).executes((command) -> {
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
                                                        }
                                                        )))
                                )))
                                .then(literal("save")
                                        .then(((RequiredArgumentBuilder) CommandManager.argument("identifier", IdentifierArgumentType.identifier()))
                                                .then(argument("target", EntityArgumentType.entity())
                                                        .then((RequiredArgumentBuilder)CommandManager.argument("sourceSlot", ItemSlotArgumentType.itemSlot())
                                                                .executes((command) -> {
                                                                    try {
                                                                        Entity entity = EntityArgumentType.getEntity(command, "target");
                                                                        Identifier id = IdentifierArgumentType.getIdentifier(command, "identifier");
                                                                        int slotId = ItemSlotArgumentType.getItemSlot(command, "sourceSlot");
                                                                        ItemStack itemStack = getStackInSlot(entity,slotId);
                                                                        ItemHolderComponent component = ItemHolderComponent.KEY.get(entity);
                                                                        component.addItem(itemStack, id);
                                                                        command.getSource().sendFeedback(new LiteralText("Item saved to Entity"), true);
                                                                        component.sync();
                                                                        return 1;
                                                                    } catch (Exception e){
                                                                        command.getSource().sendFeedback(new LiteralText("Could not save Item to Entity"), true);
                                                                        return 0;
                                                                    }
                                                                })))))
                        .then(literal("add")
                                .then(((RequiredArgumentBuilder) CommandManager.argument("identifier", IdentifierArgumentType.identifier()))
                                        .then(argument("target", EntityArgumentType.entity())
                                                .then((RequiredArgumentBuilder)CommandManager.argument("item", ItemStackArgumentType.itemStack())
                                                        .executes((command) -> {
                                                            try {
                                                                Entity entity = EntityArgumentType.getEntity(command, "target");
                                                                Identifier id = IdentifierArgumentType.getIdentifier(command, "identifier");
                                                                ItemStack itemStack = ItemStackArgumentType.getItemStackArgument(command, "item").createStack(1, false);
                                                                ItemHolderComponent component = ItemHolderComponent.KEY.get(entity);
                                                                component.addItem(itemStack, id);
                                                                command.getSource().sendFeedback(new LiteralText("Item saved to Entity"), true);
                                                                component.sync();
                                                                return 1;
                                                            } catch (Exception e){
                                                                command.getSource().sendFeedback(new LiteralText("Could not save Item to Entity"), true);
                                                                return 0;
                                                            }
                                                        })))))
                        .then(literal("load")
                                .then(((RequiredArgumentBuilder) CommandManager.argument("identifier", IdentifierArgumentType.identifier()))
                                        .then(argument("target", EntityArgumentType.entity())
                                                .then((RequiredArgumentBuilder)CommandManager.argument("sourceSlot", ItemSlotArgumentType.itemSlot())
                                                        .executes((command) -> {
                                                            try {
                                                                Entity entity = EntityArgumentType.getEntity(command, "target");
                                                                Identifier id = IdentifierArgumentType.getIdentifier(command, "identifier");
                                                                int slotId = ItemSlotArgumentType.getItemSlot(command, "sourceSlot");
                                                                ItemHolderComponent component = ItemHolderComponent.KEY.get(entity);
                                                                ItemStack itemStack = component.getItem(id);
                                                                StackReference stackReference = entity.getStackReference(slotId);
                                                                stackReference.set(itemStack);
                                                                command.getSource().sendFeedback(new LiteralText("Item loaded to Entity"), true);
                                                                component.sync();
                                                                return 1;
                                                            } catch (Exception e){
                                                                command.getSource().sendFeedback(new LiteralText("Could not load Item to Entity"), true);
                                                                return 0;
                                                            }
                                                        })))))
                        .then(literal("has")
                                .then(((RequiredArgumentBuilder) CommandManager.argument("identifier", IdentifierArgumentType.identifier()))
                                        .then(argument("target", EntityArgumentType.entity())
                                                .then((RequiredArgumentBuilder)CommandManager.argument("sourceSlot", ItemSlotArgumentType.itemSlot())
                                                        .executes((command) -> {
                                                            try {
                                                                Entity entity = EntityArgumentType.getEntity(command, "target");
                                                                Identifier id = IdentifierArgumentType.getIdentifier(command, "identifier");
                                                                int slotId = ItemSlotArgumentType.getItemSlot(command, "sourceSlot");
                                                                ItemHolderComponent component = ItemHolderComponent.KEY.get(entity);
                                                                ItemStack stack = getStackInSlot(entity, slotId);
                                                                ItemStack stack2 = component.getItem(id);
                                                                if (stack.isItemEqual(stack2)) {
                                                                    command.getSource().sendFeedback(new LiteralText("Found Item on Entity"), true);
                                                                    return 1;
                                                                } else {
                                                                    command.getSource().sendFeedback(new LiteralText("Could not find Item on Entity"), true);
                                                                    return 0;
                                                                }
                                                            } catch (Exception e){
                                                                command.getSource().sendFeedback(new LiteralText("Could not find Item on Entity"), true);
                                                                return 0;
                                                            }
                                                        })))))
                        .then(literal("remove")
                                .then(((RequiredArgumentBuilder) CommandManager.argument("identifier", IdentifierArgumentType.identifier()))
                                        .then(argument("target", EntityArgumentType.entity())
                                                .then((ArgumentBuilder)CommandManager.argument("sourceSlot", ItemSlotArgumentType.itemSlot())
                                                        .executes((command) -> {
                                                            try {
                                                                Entity entity = EntityArgumentType.getEntity(command, "target");
                                                                Identifier id = IdentifierArgumentType.getIdentifier(command, "identifier");
                                                                int slotId = ItemSlotArgumentType.getItemSlot(command, "sourceSlot");
                                                                ItemHolderComponent component = ItemHolderComponent.KEY.get(entity);
                                                                ItemStack stack = getStackInSlot(entity, slotId);
                                                                ItemStack stack2 = component.getItem(id);
                                                                if (stack.isItemEqual(stack2)) {
                                                                    command.getSource().sendFeedback(new LiteralText("Found Item on Entity"), true);
                                                                    StackReference stackReference = entity.getStackReference(slotId);
                                                                    stackReference.set(Items.AIR.getDefaultStack());
                                                                    return 1;
                                                                } else {
                                                                    command.getSource().sendFeedback(new LiteralText("Could not find Item on Entity"), true);
                                                                    return 0;
                                                                }
                                                            } catch (Exception e){
                                                                command.getSource().sendFeedback(new LiteralText("Could not find Item on Entity"), true);
                                                                return 0;
                                                            }
                                                        })))))
                        .then(literal("modify")
                                .then(((RequiredArgumentBuilder) CommandManager.argument("identifier", IdentifierArgumentType.identifier()))
                                    .then(argument("target", EntityArgumentType.entity())
                                            .then(literal("modifier")
                                                .then(CommandManager.argument("modifier", IdentifierArgumentType.identifier()).suggests(MODIFIER_SUGGESTION_PROVIDER)
                                                    .executes((command) -> {
                                                        try {
                                                            Entity entity = EntityArgumentType.getEntity(command, "target");
                                                            Identifier id = IdentifierArgumentType.getIdentifier(command, "identifier");
                                                            ItemHolderComponent component = ItemHolderComponent.KEY.get(entity);
                                                            ItemStack itemStack = component.getItem(id);
                                                            LootFunction modifier = IdentifierArgumentType.getItemModifierArgument(command, "modifier");
                                                            getStackWithModifier(command.getSource(), modifier, itemStack);
                                                            return 1;
                                                        } catch (Exception e){
                                                            command.getSource().sendFeedback(new LiteralText("Could not find Item on Entity"), true);
                                                            return 0;
                                                        }
                                                    })))
                                            .then(literal("value"))
                                            .then(CommandManager.argument("value", NbtElementArgumentType.nbtElement())
                                                    .then(CommandManager.argument("path", NbtPathArgumentType.nbtPath())
                                                        .executes((command) -> {
                                                            try {
                                                                Entity entity = EntityArgumentType.getEntity(command, "target");
                                                                Identifier id = IdentifierArgumentType.getIdentifier(command, "identifier");
                                                                ItemHolderComponent component = ItemHolderComponent.KEY.get(entity);
                                                                ItemStack itemStack = component.getItem(id);
                                                                NbtPathArgumentType.NbtPath nbtPath = NbtPathArgumentType.getNbtPath(command, "targetPath");
                                                                List<NbtElement> list = Collections.singletonList(NbtElementArgumentType.getNbtElement(command, "value"));
                                                                NbtCompound compound = new NbtCompound();
                                                                itemStack.writeNbt(compound);
                                                                for(NbtElement element : list) {
                                                                    compound.put(nbtPath.toString(), element);
                                                                }
                                                                return 1;
                                                            } catch (Exception e){
                                                                command.getSource().sendFeedback(new LiteralText("Could not find Item on Entity"), true);
                                                                return 0;
                                                            }

                                                        }))))
        )));


    }

    private static void getStackWithModifier(ServerCommandSource source, LootFunction modifier, ItemStack stack) {
        ServerWorld serverWorld = source.getWorld();
        LootContext.Builder builder = (new LootContext.Builder(serverWorld)).parameter(LootContextParameters.ORIGIN, source.getPosition()).optionalParameter(LootContextParameters.THIS_ENTITY, source.getEntity());
        modifier.apply(stack, builder.build(LootContextTypes.COMMAND));
    }

    private static final DynamicCommandExceptionType NO_SUCH_SLOT_SOURCE_EXCEPTION = new DynamicCommandExceptionType((slot) -> new TranslatableText("commands.item.source.no_such_slot", slot));

    private static ItemStack getStackInSlot(Entity entity, int slotId) throws CommandSyntaxException {
        StackReference stackReference = entity.getStackReference(slotId);
        if (stackReference == StackReference.EMPTY) {
            throw NO_SUCH_SLOT_SOURCE_EXCEPTION.create(slotId);
        } else {
            return stackReference.get().copy();
        }
    }
}
