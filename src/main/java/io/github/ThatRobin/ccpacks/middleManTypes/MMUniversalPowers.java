package io.github.ThatRobin.ccpacks.middleManTypes;

import io.github.apace100.apoli.power.PowerTypeReference;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.DDUniversalPowers;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.List;

public class MMUniversalPowers {

    public MMUniversalPowers(List<PowerTypeReference> powers){
        StatusEffect effect = new DDUniversalPowers(StatusEffectType.NEUTRAL,0x98D982, powers, new Identifier("ccpacks", "universal_powers"));
        Registry.register(Registry.STATUS_EFFECT, new Identifier("ccpacks", "universal_powers"), effect);
    }

}
