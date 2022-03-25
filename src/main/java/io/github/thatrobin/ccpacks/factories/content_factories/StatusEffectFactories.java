package io.github.thatrobin.ccpacks.factories.content_factories;

import io.github.thatrobin.ccpacks.CCPackDataTypes;
import io.github.thatrobin.ccpacks.data_driven_classes.DDStatusEffect;
import io.github.thatrobin.ccpacks.registries.CCPacksRegistries;
import io.github.thatrobin.ccpacks.util.ColourHolder;
import io.github.apace100.calio.data.SerializableData;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.awt.*;
import java.awt.image.ColorConvertOp;
import java.util.function.Supplier;

public class StatusEffectFactories {

    public static Identifier identifier(String string) {
        return new Identifier("status_effect", string);
    }

    public static void register() {
        register(new ContentFactory<>(identifier("neutral"), Types.EFFECT,
                new SerializableData()
                        .add("colour", CCPackDataTypes.COLOUR, new ColourHolder(1,1,1,1)),
                data ->
                        (contentType, content) -> {
                            ColourHolder colourHolder = data.get("colour");
                            Color colour = new Color(Math.min(Math.round(colourHolder.getRed()*255),255), Math.min(Math.round(colourHolder.getGreen()*255),255), Math.min(Math.round(colourHolder.getBlue()*255),255));
                            return (Supplier<StatusEffect>) () -> new DDStatusEffect(StatusEffectCategory.NEUTRAL, colour.getRGB());
                        }));

        register(new ContentFactory<>(identifier("beneficial"), Types.EFFECT,
                new SerializableData()
                        .add("colour", CCPackDataTypes.COLOUR, new ColourHolder(1,1,1,1)),
                data ->
                        (contentType, content) -> {
                            ColourHolder colourHolder = data.get("colour");
                            Color colour = new Color(Math.min(Math.round(colourHolder.getRed()*255),255), Math.min(Math.round(colourHolder.getGreen()*255),255), Math.min(Math.round(colourHolder.getBlue()*255),255));
                            return (Supplier<StatusEffect>) () -> new DDStatusEffect(StatusEffectCategory.BENEFICIAL, colour.getRGB());
                        }));

        register(new ContentFactory<>(identifier("harmful"), Types.EFFECT,
                new SerializableData()
                        .add("colour", CCPackDataTypes.COLOUR, new ColourHolder(1,1,1,1)),
                data ->
                        (contentType, content) -> {
                            ColourHolder colourHolder = data.get("colour");
                            Color colour = new Color(Math.min(Math.round(colourHolder.getRed()*255),255), Math.min(Math.round(colourHolder.getGreen()*255),255), Math.min(Math.round(colourHolder.getBlue()*255),255));
                            return (Supplier<StatusEffect>) () -> new DDStatusEffect(StatusEffectCategory.HARMFUL, colour.getRGB());
                        }));
    }

    private static void register(ContentFactory<Supplier<?>> serializer) {
        Registry.register(CCPacksRegistries.CONTENT_FACTORY, serializer.getSerializerId(), serializer);
    }
}
