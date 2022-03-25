package io.github.thatrobin.ccpacks.util;

import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;

import java.util.List;

public class GameruleHolder {

    private final GameRules.Key<GameRules.BooleanRule> gamerule;
    private final List<Identifier> powers;
    private final List<EntityType<?>> entities;

    public GameruleHolder(GameRules.Key<GameRules.BooleanRule> gamerule, List<Identifier> powers, List<EntityType<?>> entities) {
        this.gamerule = gamerule;
        this.powers = powers;
        this.entities = entities;
    }

    public GameRules.Key<GameRules.BooleanRule> getGamerule() {
        return this.gamerule;
    }

    public List<Identifier> getPowers() {
        return this.powers;
    }


    public List<EntityType<?>> getEntities() {
        return this.entities;
    }
}
