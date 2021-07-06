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

public class DDUniversalPowers extends StatusEffect {

    private List<PowerTypeReference> power;
    private Identifier source;

    public DDUniversalPowers(StatusEffectType type, int color, List<PowerTypeReference> powers, Identifier source) {
        super(type, color);
        this.power = powers;
        this.source = source;
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (this.power != null && this.power.size() > 0) {
            for (int i = 0; i < this.power.size(); i++) {
                PowerType<?> type = PowerTypeRegistry.get(this.power.get(i).getIdentifier());
                PowerHolderComponent component = PowerHolderComponent.KEY.get(entity);
                if (component.hasPower(type, this.source)) {
                    component.removePower(type, this.source);
                }
                component.sync();
            }
        }
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (this.power != null && this.power.size() > 0) {
            for (int i = 0; i < this.power.size(); i++) {
                PowerType<?> type = PowerTypeRegistry.get(this.power.get(i).getIdentifier());
                PowerHolderComponent component = PowerHolderComponent.KEY.get(entity);
                if (!component.hasPower(type, this.source)) {
                    boolean give = component.addPower(type, this.source);
                    if (give) {
                        component.sync();
                    }
                }
            }
        }
    }

}
