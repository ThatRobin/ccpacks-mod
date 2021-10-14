package io.github.ThatRobin.ccpacks.Power;

import io.github.ThatRobin.ccpacks.Util.StatBarHudRender;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.entity.LivingEntity;

public class StatBar extends Power {

    private StatBarHudRender hudRender;

    public StatBar(PowerType<?> type, LivingEntity entity, StatBarHudRender hudRender) {
        super(type, entity);
        this.hudRender = hudRender;
    }

    public StatBarHudRender getHudRender() {
        return this.hudRender;
    }


}
