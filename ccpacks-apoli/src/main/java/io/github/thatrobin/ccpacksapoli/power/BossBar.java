package io.github.thatrobin.ccpacksapoli.power;

import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.VariableIntPower;
import io.github.thatrobin.ccpacksapoli.util.BossBarHudRender;
import io.github.thatrobin.ccpacksapoli.util.BossBarHudRendered;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.function.Consumer;

public class BossBar extends VariableIntPower implements BossBarHudRendered {

    private final Consumer<Entity> actionOnMin;
    private final Consumer<Entity> actionOnMax;
    private final BossBarHudRender hudRender;

    public BossBar(PowerType<?> type, LivingEntity entity, int min, int max, BossBarHudRender hudRender, int startValue, Consumer<Entity> actionOnMin, Consumer<Entity> actionOnMax) {
        super(type, entity, startValue, min, max);
        this.actionOnMin = actionOnMin;
        this.actionOnMax = actionOnMax;
        this.hudRender = hudRender;
    }

    @Override
    public int setValue(int newValue) {
        int oldValue = currentValue;
        int actualNewValue = super.setValue(newValue);
        if(oldValue != actualNewValue) {
            if(actionOnMin != null && actualNewValue == min) {
                actionOnMin.accept(entity);
            }
            if(actionOnMax != null && actualNewValue == max) {
                actionOnMax.accept(entity);
            }
        }
        return actualNewValue;
    }

    @Override
    public BossBarHudRender getRenderSettings() {
        return this.hudRender;
    }

    @Override
    public float getFill() {
        return this.currentValue;
    }

    public float getPercentage() {
        float total = this.max - this.min; // 10 - 0 = 10
        float unmulper = this.currentValue / total; // 10 / 10 = 1
        return unmulper;
    }

    @Override
    public boolean shouldRender() {
        if(entity instanceof PlayerEntity player) {
            return getRenderSettings().shouldRender(player);
        } else {
            return false;
        }
    }
}
