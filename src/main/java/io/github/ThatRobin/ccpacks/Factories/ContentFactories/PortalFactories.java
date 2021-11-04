package io.github.ThatRobin.ccpacks.Factories.ContentFactories;

import io.github.ThatRobin.ccpacks.CCPackDataTypes;
import io.github.ThatRobin.ccpacks.Registries.CCPacksRegistries;
import io.github.ThatRobin.ccpacks.Util.ColourHolder;
import io.github.ThatRobin.ccpacks.Util.Portal;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.kyrptonaught.customportalapi.portal.PortalIgnitionSource;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
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
