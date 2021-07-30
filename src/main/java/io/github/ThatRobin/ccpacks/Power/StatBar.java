package io.github.ThatRobin.ccpacks.Power;

import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.ThatRobin.ccpacks.util.AdvancedHudRender;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.util.HudRender;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.stat.Stat;
import net.minecraft.util.Identifier;

public class StatBar extends Power {

    private AdvancedHudRender hudRender;

    public StatBar(PowerType<?> type, LivingEntity entity, AdvancedHudRender hudRender) {
        super(type, entity);
        this.hudRender = hudRender;
    }

    public AdvancedHudRender getHudRender() {
        return this.hudRender;
    }


}
