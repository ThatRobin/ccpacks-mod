package io.github.ThatRobin.ccpacks.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.ThatRobin.ccpacks.serializableData.CCPackDataTypes;
import io.github.apace100.apoli.power.MultiplePowerType;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.LinkedList;
import java.util.List;

public class UniversalPower {

    private Identifier identifier;
    public List<PowerType<?>> powerTypes = new LinkedList<>();
    public List<EntityType<?>> entityTypes = new LinkedList<>();

    public static final SerializableData DATA = new SerializableData()
            .add("powers", SerializableDataTypes.IDENTIFIERS, Lists.newArrayList())
            .add("entity_entry", CCPackDataTypes.ENTITY_ENTRY, Lists.newArrayList());

    public UniversalPower(Identifier id) {
        this.identifier = id;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public static UniversalPower fromJson(Identifier id, JsonObject json) {
        return createFromData(id, DATA.read(json));
    }

    @SuppressWarnings("unchecked")
    public static UniversalPower createFromData(Identifier id, SerializableData.Instance data) {
        UniversalPower origin = new UniversalPower(id);

        ((List<Identifier>)data.get("powers")).forEach(powerId -> {
            try {
                PowerType powerType = PowerTypeRegistry.get(powerId);
                origin.add(powerType);
                if(powerType instanceof MultiplePowerType) {
                    ImmutableList<Identifier> subPowers = ((MultiplePowerType)powerType).getSubPowers();
                    for(Identifier subPowerId : subPowers) {
                        origin.add(PowerTypeRegistry.get(subPowerId));
                    }
                }
            } catch(IllegalArgumentException e) {
                CCPacksMain.LOGGER.error("Powerset \"" + id + "\" contained unregistered power: \"" + powerId + "\"");
            }
        });
        if(data.get("entity_entry") != null) {
            ((List<EntityType>) data.get("entity_entry")).forEach(entityType -> {
                try {
                    CCPacksMain.LOGGER.info(entityType);
                    origin.addEntity(entityType);
                } catch (IllegalArgumentException e) {
                    CCPacksMain.LOGGER.error("Powerset \"" + id + "\" contained unregistered entity: \"" + entityType + "\"");
                }
            });
        } else {
            origin.addEntity(EntityType.PLAYER);
        }

        return origin;
    }

    public UniversalPower add(PowerType<?>... powerTypes) {
        this.powerTypes.addAll(Lists.newArrayList(powerTypes));
        return this;
    }

    public UniversalPower addEntity(EntityType<?>... powerTypes) {
        this.entityTypes.addAll(Lists.newArrayList(powerTypes));
        return this;
    }

}
