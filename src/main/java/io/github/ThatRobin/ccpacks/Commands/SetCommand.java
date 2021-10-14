package io.github.ThatRobin.ccpacks.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.command.argument.ScoreHolderArgumentType;
import net.minecraft.command.argument.ScoreboardObjectiveArgumentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.registry.Registry;

import java.util.Collection;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SetCommand {
    private static final SuggestionProvider<ServerCommandSource> SUGGESTION_PROVIDER = (context, builder) -> CommandSource.suggestIdentifiers(Registry.ATTRIBUTE.getIds(), builder);


    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                (LiteralArgumentBuilder<ServerCommandSource>) literal("set").requires(cs -> cs.hasPermissionLevel(2))
                        .then(literal("attribute")
                                .then(argument("target", EntityArgumentType.player())
                                        .then((RequiredArgumentBuilder) argument("attribute", IdentifierArgumentType.identifier()).suggests(SUGGESTION_PROVIDER)
                                                .then(argument("targets", ScoreHolderArgumentType.scoreHolders()).suggests(ScoreHolderArgumentType.SUGGESTION_PROVIDER)
                                                        .then(argument("objective", ScoreboardObjectiveArgumentType.scoreboardObjective())
                                                                .executes((command) -> {
                                                                    try {
                                                                        EntityAttribute attribute = IdentifierArgumentType.getAttributeArgument(command, "attribute");
                                                                        LivingEntity entity = EntityArgumentType.getPlayer(command, "target");
                                                                        EntityAttributeInstance entityAttributeInstance = entity.getAttributes().getCustomInstance(attribute);
                                                                        setAttribute(entity, command.getSource(), ScoreHolderArgumentType.getScoreboardScoreHolders(command, "targets"), ScoreboardObjectiveArgumentType.getObjective(command, "objective"), entityAttributeInstance);
                                                                        command.getSource().sendFeedback(new LiteralText("Set attribute to scoreboard value successfully"), true);
                                                                        return 1;
                                                                    } catch (Exception e) {
                                                                        command.getSource().sendFeedback(new LiteralText("Could not set attribute to scoreboard value"), true);
                                                                        return 0;
                                                                    }
                                                                })))
                                        )))
                        .then(literal(("food"))
                            .then(argument("target", EntityArgumentType.player())
                                    .then(argument("targets", ScoreHolderArgumentType.scoreHolders()).suggests(ScoreHolderArgumentType.SUGGESTION_PROVIDER)
                                            .then(argument("objective", ScoreboardObjectiveArgumentType.scoreboardObjective())
                                                .executes((command) -> {
                                                    try {
                                                        LivingEntity entity = EntityArgumentType.getPlayer(command, "target");

                                                        setHunger(entity, command.getSource(), ScoreHolderArgumentType.getScoreboardScoreHolders(command, "targets"), ScoreboardObjectiveArgumentType.getObjective(command, "objective"));
                                                        command.getSource().sendFeedback(new LiteralText("Set attribute to scoreboard value successfully"), true);
                                                        return 1;
                                                    } catch (Exception e) {
                                                        command.getSource().sendFeedback(new LiteralText("Could not set attribute to scoreboard value"), true);
                                                        return 0;
                                                    }
                                                })))
                        ))
                        .then(literal(("health"))
                                .then(argument("target", EntityArgumentType.player())
                                        .then(argument("targets", ScoreHolderArgumentType.scoreHolders()).suggests(ScoreHolderArgumentType.SUGGESTION_PROVIDER)
                                                .then(argument("objective", ScoreboardObjectiveArgumentType.scoreboardObjective())
                                                        .executes((command) -> {
                                                            try {
                                                                LivingEntity entity = EntityArgumentType.getPlayer(command, "target");

                                                                setHealth(entity, command.getSource(), ScoreHolderArgumentType.getScoreboardScoreHolders(command, "targets"), ScoreboardObjectiveArgumentType.getObjective(command, "objective"));
                                                                command.getSource().sendFeedback(new LiteralText("Set attribute to scoreboard value successfully"), true);
                                                                return 1;
                                                            } catch (Exception e) {
                                                                command.getSource().sendFeedback(new LiteralText("Could not set attribute to scoreboard value"), true);
                                                                return 0;
                                                            }
                                                        })))

        )));


    }

    public static void setHealth(LivingEntity entity, ServerCommandSource source, Collection<String> targets, ScoreboardObjective objective){
        Scoreboard scoreboard = source.getServer().getScoreboard();

        for (String string : targets) {
            ScoreboardPlayerScore scoreboardPlayerScore = scoreboard.getPlayerScore(string, objective);
            entity.setHealth(scoreboardPlayerScore.getScore());
        }
    }

    public static void setHunger(LivingEntity entity, ServerCommandSource source, Collection<String> targets, ScoreboardObjective objective){
        Scoreboard scoreboard = source.getServer().getScoreboard();
        for (String string : targets) {
            ScoreboardPlayerScore scoreboardPlayerScore = scoreboard.getPlayerScore(string, objective);
            ((PlayerEntity) entity).getHungerManager().setFoodLevel(scoreboardPlayerScore.getScore());
        }
    }


    public static void setAttribute(LivingEntity entity, ServerCommandSource source, Collection<String> targets, ScoreboardObjective objective, EntityAttributeInstance eai) {
        Scoreboard scoreboard = source.getServer().getScoreboard();
        for (String string : targets) {
            ScoreboardPlayerScore scoreboardPlayerScore = scoreboard.getPlayerScore(string, objective);
            eai.setBaseValue(scoreboardPlayerScore.getScore());
            if (eai.getAttribute().getTranslationKey().equals("attribute.name.generic.max_health")) {
                entity.setHealth(scoreboardPlayerScore.getScore());
            }

        }
    }
}
