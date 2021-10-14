package io.github.ThatRobin.ccpacks.Factories.ContentFactories;

import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.ThatRobin.ccpacks.Registries.CCPacksRegistries;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.DDStatusEffect;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.awt.*;
import java.util.function.Supplier;

public class StatusEffectFactories {

    public static Identifier identifier(String string) {
        return new Identifier("status_effect", string);
    }

    @SuppressWarnings("unchecked")
    public static void register() {
        register(new ContentFactory<>(CCPacksMain.identifier("neutral"), Types.EFFECT,
                new SerializableData()
                        .add("red", SerializableDataTypes.INT, 255)
                        .add("green", SerializableDataTypes.INT, 255)
                        .add("blue", SerializableDataTypes.INT, 255),
                data ->
                        (contentType, content) -> {
                            float[] color = Color.RGBtoHSB(data.getInt("red"), data.getInt("green"), data.getInt("blue"), null);
                            return (Supplier<StatusEffect>) () -> new DDStatusEffect(StatusEffectType.NEUTRAL, Color.getHSBColor(color[0], color[1], color[2]).hashCode());
                        }));

        register(new ContentFactory<>(CCPacksMain.identifier("beneficial"), Types.EFFECT,
                new SerializableData()
                        .add("red", SerializableDataTypes.INT, 255)
                        .add("green", SerializableDataTypes.INT, 255)
                        .add("blue", SerializableDataTypes.INT, 255),
                data ->
                        (contentType, content) -> {
                            float[] color = Color.RGBtoHSB(data.getInt("red"), data.getInt("green"), data.getInt("blue"), null);
                            return (Supplier<StatusEffect>) () -> new DDStatusEffect(StatusEffectType.BENEFICIAL, Color.getHSBColor(color[0], color[1], color[2]).hashCode());
                        }));

        register(new ContentFactory<>(CCPacksMain.identifier("harmful"), Types.EFFECT,
                new SerializableData()
                        .add("red", SerializableDataTypes.INT, 255)
                        .add("green", SerializableDataTypes.INT, 255)
                        .add("blue", SerializableDataTypes.INT, 255),
                data ->
                        (contentType, content) -> {
                            float[] color = Color.RGBtoHSB(data.getInt("red"), data.getInt("green"), data.getInt("blue"), null);
                            return (Supplier<StatusEffect>) () -> new DDStatusEffect(StatusEffectType.HARMFUL, Color.getHSBColor(color[0], color[1], color[2]).hashCode());
                        }));
    }

    private static void register(ContentFactory<Supplier<?>> serializer) {
        Registry.register(CCPacksRegistries.CONTENT_FACTORY, serializer.getSerializerId(), serializer);
    }
}
