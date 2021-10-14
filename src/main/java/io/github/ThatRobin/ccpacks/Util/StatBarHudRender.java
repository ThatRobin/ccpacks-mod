package io.github.ThatRobin.ccpacks.Util;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.util.HudRender;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class StatBarHudRender extends HudRender {

    private final String side;
    int value;

    public StatBarHudRender(boolean shouldRender, int barIndex, Identifier spriteLocation, ConditionFactory<LivingEntity>.Instance condition, String side) {
        super(shouldRender, barIndex, spriteLocation, condition, false);
        this.side = side;
    }

    public String getSide() {
        return this.side;
    }

    public void addValue(int add) {
        this.value += add;
    }

    public int setValue(int newValue) {
        if(newValue > getMaxValue())
            newValue = getMaxValue();
        if(newValue < getMinValue())
            newValue = getMinValue();
        value = newValue;
        return value;
    }

    public int getValue() {
        return value;
    }

    public int getMinValue() {
        return 0;
    }

    public int getMaxValue() {
        return 20;
    }

}
