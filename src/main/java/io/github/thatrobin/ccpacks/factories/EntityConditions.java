package io.github.thatrobin.ccpacks.factories;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import io.github.thatrobin.ccpacks.CCPacksMain;
import io.github.thatrobin.ccpacks.power.StatBar;
import io.github.apace100.apoli.Apoli;
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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;

import java.util.concurrent.atomic.AtomicBoolean;

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
                        resourceValue = ((StatBar)p).getValue();
                    }
                    return ((Comparison)data.get("comparison")).compare(resourceValue, data.getInt("compare_to"));
                }));

        register(new ConditionFactory<>(Apoli.identifier("equipped_trinket"), new SerializableData()
                .add("item_condition", ApoliDataTypes.ITEM_CONDITION),
                (data, entity) -> {
            if(entity instanceof PlayerEntity) {
                try {
                    AtomicBoolean found = new AtomicBoolean(false);
                    TrinketComponent component = TrinketsApi.getTrinketComponent((PlayerEntity) entity).get();
                    component.forEach((slotReference, itemStack) -> {
                        if (((ConditionFactory<ItemStack>.Instance) data.get("item_condition")).test(itemStack)) {
                            found.set(true);
                        }
                    });
                    return found.get();
                } catch (Exception e) {
                    return false;
                }
            } else {
                return false;
            }
        }));
    }

    private static void register(ConditionFactory<Entity> conditionFactory) {
        Registry.register(ApoliRegistries.ENTITY_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }
}
