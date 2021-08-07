package io.github.ThatRobin.ccpacks.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import io.github.ThatRobin.ccpacks.Screen.ChoiceScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.ScoreHolderArgumentType;
import net.minecraft.command.argument.ScoreboardObjectiveArgumentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.registry.Registry;

import java.util.Collection;
import java.util.Iterator;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SetHealthFromScoreboard {
    private static final SuggestionProvider<ServerCommandSource> SUGGESTION_PROVIDER = (context, builder) -> {
        return CommandSource.suggestIdentifiers(Registry.ATTRIBUTE.getIds(), builder);
    };

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("shfs").requires(cs -> cs.hasPermissionLevel(2))
                        .then(literal("set")
                                .then(argument("target", EntityArgumentType.player())
                                        .then(argument("targets", ScoreHolderArgumentType.scoreHolders()).suggests(ScoreHolderArgumentType.SUGGESTION_PROVIDER)
                                            .then(argument("objective", ScoreboardObjectiveArgumentType.scoreboardObjective())
                                                .executes((command) -> {
                                                    try {
                                                        LivingEntity entity = EntityArgumentType.getPlayer(command, "target");

                                                        setHealth(entity, (ServerCommandSource)command.getSource(), ScoreHolderArgumentType.getScoreboardScoreHolders(command, "targets"), ScoreboardObjectiveArgumentType.getObjective(command, "objective"));
                                                        command.getSource().sendFeedback(new LiteralText("Set attribute to scoreboard value successfully"), true);
                                                        return 1;
                                                    } catch (Exception e){
                                                        command.getSource().sendFeedback(new LiteralText("Could not set attribute to scoreboard value"), true);
                                                        return 0;
                                                    }
                                                })))
                        ))
                                .then(literal("add")
                                        .then(argument("target", EntityArgumentType.player())
                                                .then(argument("targets", ScoreHolderArgumentType.scoreHolders()).suggests(ScoreHolderArgumentType.SUGGESTION_PROVIDER)
                                                        .then(argument("objective", ScoreboardObjectiveArgumentType.scoreboardObjective())
                                                                .executes((command) -> {
                                                                    try {
                                                                        LivingEntity entity = EntityArgumentType.getPlayer(command, "target");

                                                                        addHealth(entity, (ServerCommandSource)command.getSource(), ScoreHolderArgumentType.getScoreboardScoreHolders(command, "targets"), ScoreboardObjectiveArgumentType.getObjective(command, "objective"));
                                                                        command.getSource().sendFeedback(new LiteralText("Set attribute to scoreboard value successfully"), true);
                                                                        return 1;
                                                                    } catch (Exception e){
                                                                        command.getSource().sendFeedback(new LiteralText("Could not set attribute to scoreboard value"), true);
                                                                        return 0;
                                                                    }
                                                                })))
                                        ))
                        .then(literal("remove")
                                .then(argument("target", EntityArgumentType.player())
                                        .then(argument("targets", ScoreHolderArgumentType.scoreHolders()).suggests(ScoreHolderArgumentType.SUGGESTION_PROVIDER)
                                                .then(argument("objective", ScoreboardObjectiveArgumentType.scoreboardObjective())
                                                        .executes((command) -> {
                                                            try {
                                                                LivingEntity entity = EntityArgumentType.getPlayer(command, "target");

                                                                removeHealth(entity, (ServerCommandSource)command.getSource(), ScoreHolderArgumentType.getScoreboardScoreHolders(command, "targets"), ScoreboardObjectiveArgumentType.getObjective(command, "objective"));
                                                                command.getSource().sendFeedback(new LiteralText("Set attribute to scoreboard value successfully"), true);
                                                                return 1;
                                                            } catch (Exception e){
                                                                command.getSource().sendFeedback(new LiteralText("Could not set attribute to scoreboard value"), true);
                                                                return 0;
                                                            }
                                                        })))
                                )

        ));
    }

    public static void setHealth(LivingEntity entity, ServerCommandSource source, Collection<String> targets, ScoreboardObjective objective){
        Scoreboard scoreboard = source.getServer().getScoreboard();
        Iterator var7 = targets.iterator();

        while(var7.hasNext()) {
            String string = (String)var7.next();
            ScoreboardPlayerScore scoreboardPlayerScore = scoreboard.getPlayerScore(string, objective);
            entity.setHealth(scoreboardPlayerScore.getScore());
        }
   }

    public static void addHealth(LivingEntity entity, ServerCommandSource source, Collection<String> targets, ScoreboardObjective objective){
        Scoreboard scoreboard = source.getServer().getScoreboard();
        Iterator var7 = targets.iterator();
        while(var7.hasNext()) {
            String string = (String)var7.next();
            ScoreboardPlayerScore scoreboardPlayerScore = scoreboard.getPlayerScore(string, objective);
            entity.setHealth(entity.getHealth() + scoreboardPlayerScore.getScore());
        }
    }

    public static void removeHealth(LivingEntity entity, ServerCommandSource source, Collection<String> targets, ScoreboardObjective objective){
        Scoreboard scoreboard = source.getServer().getScoreboard();
        Iterator var7 = targets.iterator();
        while(var7.hasNext()) {
            String string = (String)var7.next();
            ScoreboardPlayerScore scoreboardPlayerScore = scoreboard.getPlayerScore(string, objective);
            entity.setHealth(entity.getHealth() - scoreboardPlayerScore.getScore());
        }
    }
}