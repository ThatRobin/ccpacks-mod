package io.github.ThatRobin.ccpacks.SerializableData;

import dev.emi.trinkets.TrinketSlot;
import dev.emi.trinkets.api.SlotAttributes;
import dev.emi.trinkets.api.SlotGroup;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.SlotType;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.PowerTypeReference;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.EquipmentSlot;

import java.util.List;

public class CCPackDataTypes {

    public static final SerializableDataType<List<String>> STRINGS =
            SerializableDataType.list(SerializableDataTypes.STRING);

    public static final SerializableDataType<List<PowerTypeReference>> POWER_TYPES =
            SerializableDataType.list(ApoliDataTypes.POWER_TYPE);

}