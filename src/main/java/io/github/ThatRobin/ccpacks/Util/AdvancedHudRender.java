package io.github.ThatRobin.ccpacks.Util;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class AdvancedHudRender {

    private boolean shouldRender;
    private Identifier spriteLocation;
    private ConditionFactory<LivingEntity>.Instance playerCondition;

    public AdvancedHudRender(boolean shouldRender, Identifier spriteLocation, ConditionFactory<LivingEntity>.Instance playerCondition) {
        this.shouldRender = shouldRender;
        this.spriteLocation = spriteLocation;
        this.playerCondition = playerCondition;
    }

    public Identifier getSpriteLocation() {
        return spriteLocation;
    }

    public boolean shouldRender() {
        return shouldRender;
    }

    public boolean shouldRender(PlayerEntity player) {
        return shouldRender && (playerCondition == null || playerCondition.test(player));
    }

    public ConditionFactory<LivingEntity>.Instance getCondition() {
        return playerCondition;
    }
}
