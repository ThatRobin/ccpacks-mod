package io.github.thatrobin.ccpacks.commands;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.thatrobin.ccpacks.factories.mechanic_factories.MechanicRegistry;
import io.github.thatrobin.ccpacks.factories.mechanic_factories.MechanicType;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class MechanicArgument implements ArgumentType<Identifier> {

    public static final DynamicCommandExceptionType LAYER_NOT_FOUND = new DynamicCommandExceptionType((object) -> new TranslatableText("commands.choice.layer_not_found", new Object[]{object}));

    public MechanicArgument() {
    }

    public static MechanicArgument mechanic() {
        return new MechanicArgument();
    }

    public Identifier parse(StringReader p_parse_1_) throws CommandSyntaxException {
        return Identifier.fromCommandInput(p_parse_1_);
    }

    public MechanicType getMechanic(CommandContext<ServerCommandSource> context, String argumentName) throws CommandSyntaxException {
        Identifier id = context.getArgument(argumentName, Identifier.class);

        try {
            return MechanicRegistry.get(id);
        } catch (IllegalArgumentException var4) {
            throw LAYER_NOT_FOUND.create(id);
        }
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestIdentifiers(MechanicRegistry.identifiers(), builder);
    }
}
