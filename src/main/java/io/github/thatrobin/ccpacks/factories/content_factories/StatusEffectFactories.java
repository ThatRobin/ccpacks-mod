package io.github.thatrobin.ccpacks.factories.content_factories;

import io.github.thatrobin.ccpacks.CCPackDataTypes;
import io.github.thatrobin.ccpacks.data_driven_classes.DDStatusEffect;
import io.github.thatrobin.ccpacks.registries.CCPacksRegistries;
import io.github.thatrobin.ccpacks.util.ColourHolder;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.awt.*;
import java.util.function.Supplier;

public class StatusEffectFactories {

    public static Identifier identifier(String string) {
        return new Identifier("status_effect", string);
    }

    public static void register() {
        register(new ContentFactory<>(identifier("neutral"), Types.EFFECT,
                new SerializableData()
                        .add("color", CCPackDataTypes.COLOR, new ColourHolder(1,1,1,1)),
                data ->
                        (contentType, content) -> {
                            ColourHolder colourHolder = data.get("color");
                            float[] color = Color.RGBtoHSB((int)colourHolder.getRed()*255, (int)colourHolder.getGreen()*255, (int)colourHolder.getBlue()*255, null);
                            return (Supplier<StatusEffect>) () -> new DDStatusEffect(StatusEffectCategory.NEUTRAL, Color.getHSBColor(color[0], color[1], color[2]).hashCode());
                        }));

        register(new ContentFactory<>(identifier("beneficial"), Types.EFFECT,
                new SerializableData()
                        .add("color", CCPackDataTypes.COLOR, new ColourHolder(1,1,1,1)),
                data ->
                        (contentType, content) -> {
                            ColourHolder colourHolder = data.get("color");
                            float[] color = Color.RGBtoHSB((int)colourHolder.getRed()*255, (int)colourHolder.getGreen()*255, (int)colourHolder.getBlue()*255, null);
                            return (Supplier<StatusEffect>) () -> new DDStatusEffect(StatusEffectCategory.BENEFICIAL, Color.getHSBColor(color[0], color[1], color[2]).hashCode());
                        }));

        register(new ContentFactory<>(identifier("harmful"), Types.EFFECT,
                new SerializableData()
                        .add("color", CCPackDataTypes.COLOR, new ColourHolder(1,1,1,1)),
                data ->
                        (contentType, content) -> {
                            ColourHolder colourHolder = data.get("color");
                            float[] color = Color.RGBtoHSB((int)colourHolder.getRed()*255, (int)colourHolder.getGreen()*255, (int)colourHolder.getBlue()*255, null);
                            return (Supplier<StatusEffect>) () -> new DDStatusEffect(StatusEffectCategory.HARMFUL, Color.getHSBColor(color[0], color[1], color[2]).hashCode());
                        }));
    }

    private static void register(ContentFactory<Supplier<?>> serializer) {
        Registry.register(CCPacksRegistries.CONTENT_FACTORY, serializer.getSerializerId(), serializer);
    }
}
