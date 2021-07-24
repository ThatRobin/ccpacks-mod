package io.github.ThatRobin.ccpacks.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.apace100.apoli.power.MultiplePowerType;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.util.Identifier;

import java.util.LinkedList;
import java.util.List;

public class UniversalPower {

    private Identifier identifier;
    public List<PowerType<?>> powerTypes = new LinkedList<>();

    public static final SerializableData DATA = new SerializableData()
            .add("powers", SerializableDataTypes.IDENTIFIERS, Lists.newArrayList());

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

        return origin;
    }

    public UniversalPower add(PowerType<?>... powerTypes) {
        this.powerTypes.addAll(Lists.newArrayList(powerTypes));
        return this;
    }
}
