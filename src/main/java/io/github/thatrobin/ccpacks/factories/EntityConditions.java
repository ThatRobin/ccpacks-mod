package io.github.thatrobin.ccpacks.factories;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import io.github.apace100.apoli.power.*;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.thatrobin.ccpacks.CCPacksMain;
import io.github.thatrobin.ccpacks.util.AccessFactory;
import io.github.thatrobin.ccpacks.power.StatBar;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class EntityConditions {

    public static void register() {
        register(new ConditionFactory<>(CCPacksMain.identifier("check_stat"), new SerializableData()
                .add("stat_bar", ApoliDataTypes.POWER_TYPE)
                .add("comparison", ApoliDataTypes.COMPARISON, Comparison.EQUAL)
                .add("compare_to", SerializableDataTypes.INT, 0),
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

        register(new ConditionFactory<>(CCPacksMain.identifier("active_power_type"), new SerializableData()
                .add("power_type", SerializableDataTypes.IDENTIFIER)
                .add("blacklisted_powers", SerializableDataType.list(ApoliDataTypes.POWER_TYPE)),
                (data, entity) -> {
                    Identifier powerTypeId = data.getId("power_type");
                    List<PowerType<?>> blacklistedPowers = data.get("blacklisted_powers");
                    PowerHolderComponent component = PowerHolderComponent.KEY.get(entity);
                    List<PowerType<?>> correctPowers = Lists.newArrayList();

                    component.getPowerTypes(true).forEach(powerType -> {
                        if(!blacklistedPowers.contains(powerType) && component.hasPower(powerType)) {
                            Identifier allowedPowerId = (((AccessFactory) powerType.getFactory()).getFactory().getSerializerId());
                            if (allowedPowerId.equals(powerTypeId)) {
                                correctPowers.add(powerType);
                            }
                        }
                    });

                    return correctPowers.stream().anyMatch(pt -> pt.isActive(entity));
                }));

        register(new ConditionFactory<>(CCPacksMain.identifier("resource_percentage"), new SerializableData()
                .add("resource", ApoliDataTypes.POWER_TYPE)
                .add("comparison", ApoliDataTypes.COMPARISON, Comparison.EQUAL)
                .add("percentage", SerializableDataTypes.INT, 50),
                (data, entity) -> {
                    int resourceValue = 0;
                    float percentageValue = 0;
                    PowerHolderComponent component = PowerHolderComponent.KEY.get(entity);
                    Power p = component.getPower((PowerType<?>)data.get("resource"));
                    if(p instanceof VariableIntPower r) {
                        resourceValue = r.getValue();
                        percentageValue = ((float)resourceValue / (float)r.getMax()) * 100;

                    } else if(p instanceof CooldownPower cp) {
                        resourceValue = cp.getRemainingTicks();
                        percentageValue = ((float)resourceValue / (float)cp.cooldownDuration) * 100;
                    }
                    CCPacksMain.LOGGER.info(percentageValue);
                    CCPacksMain.LOGGER.info(data.getInt("percentage"));
                    return ((Comparison)data.get("comparison")).compare(percentageValue, data.getInt("percentage"));
                }));
    }

    private static void register(ConditionFactory<Entity> conditionFactory) {
        Registry.register(ApoliRegistries.ENTITY_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }
}
