package io.github.ThatRobin.ccpacks.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.suggestion.SuggestionProvider;
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

public class SetFoodFromScoreboard {
    private static final SuggestionProvider<ServerCommandSource> SUGGESTION_PROVIDER = (context, builder) -> {
        return CommandSource.suggestIdentifiers(Registry.ATTRIBUTE.getIds(), builder);
    };

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("sffs").requires(cs -> cs.hasPermissionLevel(2))
                        .then(literal("set")
                                .then(argument("target", EntityArgumentType.player())
                                        .then(argument("targets", ScoreHolderArgumentType.scoreHolders()).suggests(ScoreHolderArgumentType.SUGGESTION_PROVIDER)
                                            .then(argument("objective", ScoreboardObjectiveArgumentType.scoreboardObjective())
                                                .executes((command) -> {
                                                    try {
                                                        LivingEntity entity = EntityArgumentType.getPlayer(command, "target");

                                                        temp(entity, (ServerCommandSource)command.getSource(), ScoreHolderArgumentType.getScoreboardScoreHolders(command, "targets"), ScoreboardObjectiveArgumentType.getObjective(command, "objective"));
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

    public static void temp(LivingEntity entity, ServerCommandSource source, Collection<String> targets, ScoreboardObjective objective){
        Scoreboard scoreboard = source.getServer().getScoreboard();
        Iterator var7 = targets.iterator();
        while(var7.hasNext()) {
            String string = (String)var7.next();
            ScoreboardPlayerScore scoreboardPlayerScore = scoreboard.getPlayerScore(string, objective);
            ((PlayerEntity)entity).getHungerManager().setFoodLevel(scoreboardPlayerScore.getScore());
        }
   }
}
