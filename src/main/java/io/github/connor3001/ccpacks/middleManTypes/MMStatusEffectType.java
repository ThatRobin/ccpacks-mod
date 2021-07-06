package io.github.connor3001.ccpacks.middleManTypes;

import io.github.apace100.apoli.power.PowerTypeReference;
import io.github.connor3001.ccpacks.dataDrivenTypes.DDStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.List;

public class MMStatusEffectType {

    public MMStatusEffectType(Identifier identifier, String hex, List<PowerTypeReference> powers){
        StatusEffect effect = new DDStatusEffect(StatusEffectType.NEUTRAL,0x98D982, powers, identifier);
        Registry.register(Registry.STATUS_EFFECT, identifier, effect);
    }
}
