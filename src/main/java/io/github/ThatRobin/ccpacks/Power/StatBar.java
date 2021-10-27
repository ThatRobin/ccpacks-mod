package io.github.ThatRobin.ccpacks.Power;

import io.github.ThatRobin.ccpacks.Util.StatBarHudRender;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.VariableIntPower;
import net.minecraft.entity.LivingEntity;

public class StatBar extends VariableIntPower {

    private StatBarHudRender hudRender;

    public StatBar(PowerType<?> type, LivingEntity entity, StatBarHudRender hudRender) {
        super(type, entity,20,0,20);
        this.hudRender = hudRender;
    }

    public StatBarHudRender getHudRender() {
        return this.hudRender;
    }


}
