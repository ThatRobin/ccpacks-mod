package io.github.ThatRobin.ccpacks.dataDrivenTypes;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeReference;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.Identifier;

import java.util.List;

public class DDStatusEffect extends StatusEffect {

    public DDStatusEffect(StatusEffectType type, int color) {
        super(type, color);
    }

}

