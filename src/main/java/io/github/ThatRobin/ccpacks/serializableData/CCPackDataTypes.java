package io.github.ThatRobin.ccpacks.serializableData;

import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.ThatRobin.ccpacks.util.AdvancedHudRender;
import io.github.ThatRobin.ccpacks.util.ColourHolder;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.PowerTypeReference;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.util.HudRender;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import java.util.List;

public class CCPackDataTypes {

    public static final SerializableDataType<List<String>> STRINGS =
            SerializableDataType.list(SerializableDataTypes.STRING);

    public static final SerializableDataType<AdvancedHudRender> HUD_RENDER = SerializableDataType.compound(AdvancedHudRender.class, new
                    SerializableData()
                    .add("should_render", SerializableDataTypes.BOOLEAN, true)
                    .add("bar_index", SerializableDataTypes.INT, 0)
                    .add("side", SerializableDataTypes.STRING, "right")
                    .add("sprite_location", SerializableDataTypes.IDENTIFIER, new Identifier("ccpacks", "textures/gui/icons.png"))
                    .add("condition", ApoliDataTypes.ENTITY_CONDITION, null),
            (dataInst) -> new AdvancedHudRender(
                    dataInst.getBoolean("should_render"),
                    dataInst.getInt("bar_index"),
                    dataInst.getId("sprite_location"),
                    (ConditionFactory<LivingEntity>.Instance)dataInst.get("condition"),
                    dataInst.getString("side")),
            (data, inst) -> {
                SerializableData.Instance dataInst = data.new Instance();
                dataInst.set("should_render", inst.shouldRender());
                dataInst.set("bar_index", inst.getBarIndex());
                dataInst.set("sprite_location", inst.getSpriteLocation());
                dataInst.set("condition", inst.getCondition());
                dataInst.set("side", inst.getSide());
                return dataInst;
            });

    public static final SerializableDataType<ColourHolder> COLOR = SerializableDataType.compound(ColourHolder.class, new
                    SerializableData()
                    .add("red", SerializableDataTypes.INT, 1)
                    .add("green", SerializableDataTypes.INT, 1)
                    .add("blue", SerializableDataTypes.INT, 1),
            (dataInst) -> new ColourHolder(
                    dataInst.getInt("red"),
                    dataInst.getInt("green"),
                    dataInst.getInt("blue")),
            (data, inst) -> {
                SerializableData.Instance dataInst = data.new Instance();
                dataInst.set("red", inst.getRed());
                dataInst.set("green", inst.getGreen());
                dataInst.set("blue", inst.getBlue());
                return dataInst;
            });
}