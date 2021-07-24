package io.github.ThatRobin.ccpacks.serializableData;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.PowerTypeReference;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.registry.Registry;

import java.awt.*;
import java.util.List;

public class CCPackDataTypes {

    public static final SerializableDataType<List<String>> STRINGS =
            SerializableDataType.list(SerializableDataTypes.STRING);

    public static final SerializableDataType<List<PowerTypeReference>> POWER_TYPES =
            SerializableDataType.list(ApoliDataTypes.POWER_TYPE);

}