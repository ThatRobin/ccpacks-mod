package io.github.thatrobin.ccpacks.factories.content_factories;

import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.thatrobin.ccpacks.CCPackDataTypes;
import io.github.thatrobin.ccpacks.registries.CCPacksRegistries;
import io.github.thatrobin.ccpacks.util.ColourHolder;
import io.github.thatrobin.ccpacks.util.Portal;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

public class PortalFactories {

    public static Identifier identifier(String string) {
        return new Identifier("portal", string);
    }

    public static void register() {
        register(new ContentFactory<>(identifier("vertical"), Types.PORTAL,
                new SerializableData()
                        .add("block", SerializableDataTypes.BLOCK)
                        .add("ignition_item", SerializableDataTypes.IDENTIFIER, new Identifier("flint_and_steel"))
                        .add("dimension", SerializableDataTypes.IDENTIFIER, new Identifier("the_end"))
                        .add("colour", CCPackDataTypes.COLOUR, new ColourHolder(1,1,1,1)),
                data ->
                        (contentType, content) -> (Supplier<Portal>) () -> new Portal(data.get("block"), data.getId("ignition_item"), data.getId("dimension"), data.get("colour"))));

        register(new ContentFactory<>(identifier("horizontal"), Types.PORTAL,
                new SerializableData()
                        .add("block", SerializableDataTypes.BLOCK)
                        .add("ignition_item", SerializableDataTypes.IDENTIFIER, new Identifier("flint_and_steel"))
                        .add("dimension", SerializableDataTypes.IDENTIFIER, new Identifier("the_end"))
                        .add("colour", CCPackDataTypes.COLOUR, new ColourHolder(1,1,1,1)),
                data ->
                        (contentType, content) -> (Supplier<Portal>) () -> new Portal(data.get("block"), data.getId("ignition_item"), data.getId("dimension"), data.get("colour"))));


    }

    private static void register(ContentFactory<Supplier<?>> serializer) {
        Registry.register(CCPacksRegistries.CONTENT_FACTORY, serializer.getSerializerId(), serializer);
    }
}
