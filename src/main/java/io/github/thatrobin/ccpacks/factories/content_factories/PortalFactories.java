package io.github.thatrobin.ccpacks.factories.content_factories;

import io.github.thatrobin.ccpacks.CCPackDataTypes;
import io.github.thatrobin.ccpacks.registries.CCPacksRegistries;
import io.github.thatrobin.ccpacks.util.ColourHolder;
import io.github.thatrobin.ccpacks.util.Portal;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.block.Block;
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
                        .add("block", SerializableDataTypes.BLOCK, Blocks.ACACIA_LEAVES)
                        .add("ignition_item", SerializableDataTypes.IDENTIFIER, new Identifier("flint_and_steel"))
                        .add("dimension", SerializableDataTypes.IDENTIFIER, new Identifier("the_end"))
                        .add("color", CCPackDataTypes.COLOR, new ColourHolder(1,1,1,1)),
                data ->
                        (contentType, content) -> (Supplier<Portal>) () -> new Portal((Block) data.get("block"), data.getId("ignition_item"), data.getId("dimension"), (ColourHolder) data.get("color"))));

        register(new ContentFactory<>(identifier("horizontal"), Types.PORTAL,
                new SerializableData()
                        .add("block", SerializableDataTypes.BLOCK, Blocks.ACACIA_LEAVES)
                        .add("ignition_item", SerializableDataTypes.IDENTIFIER, new Identifier("flint_and_steel"))
                        .add("dimension", SerializableDataTypes.IDENTIFIER, new Identifier("the_end"))
                        .add("color", CCPackDataTypes.COLOR, new ColourHolder(1,1,1,1)),
                data ->
                        (contentType, content) -> (Supplier<Portal>) () -> new Portal((Block) data.get("block"), data.getId("ignition_item"), data.getId("dimension"), (ColourHolder) data.get("color"))));


    }

    private static void register(ContentFactory<Supplier<?>> serializer) {
        Registry.register(CCPacksRegistries.CONTENT_FACTORY, serializer.getSerializerId(), serializer);
    }
}
