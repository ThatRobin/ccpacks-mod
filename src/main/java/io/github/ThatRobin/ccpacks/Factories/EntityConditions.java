package io.github.ThatRobin.ccpacks.Factories;

import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.ThatRobin.ccpacks.Power.StatBar;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.util.registry.Registry;

public class EntityConditions {

    public static void register() {
        register(new ConditionFactory<>(CCPacksMain.identifier("check_stat"), new SerializableData()
                .add("stat_bar", ApoliDataTypes.POWER_TYPE)
                .add("comparison", ApoliDataTypes.COMPARISON)
                .add("compare_to", SerializableDataTypes.INT),
                (data, entity) -> {
                    int resourceValue = 0;
                    PowerHolderComponent component = PowerHolderComponent.KEY.get(entity);
                    Power p = component.getPower((PowerType<?>)data.get("stat_bar"));
                    if(p instanceof StatBar) {
                        resourceValue = ((StatBar)p).getHudRender().getValue();
                    }
                    return ((Comparison)data.get("comparison")).compare(resourceValue, data.getInt("compare_to"));
                }));
    }

    private static void register(ConditionFactory<Entity> conditionFactory) {
        Registry.register(ApoliRegistries.ENTITY_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }
}
