package io.github.ThatRobin.ccpacks.Factories.ContentFactories;

import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.ThatRobin.ccpacks.Registries.CCPacksRegistries;
import io.github.ThatRobin.ccpacks.Util.Portal;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.kyrptonaught.customportalapi.portal.PortalIgnitionSource;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

public class PortalFactories {

    public static Identifier identifier(String string) {
        return new Identifier("portal", string);
    }

    @SuppressWarnings("unchecked")
    public static void register() {
        register(new ContentFactory<>(CCPacksMain.identifier("vertical"), Types.PORTAL,
                new SerializableData()
                        .add("red", SerializableDataTypes.INT, 255)
                        .add("green", SerializableDataTypes.INT, 255)
                        .add("blue", SerializableDataTypes.INT, 255),
                data ->
                        (contentType, content) -> (Supplier<Portal>) () -> new Portal((Block) data.get("block"), PortalIgnitionSource.ItemUseSource((Item) data.get("ignition_item")), data.getId("dimension"), data.getInt("red"), data.getInt("green"), data.getInt("blue"))));

    }

    private static void register(ContentFactory<Supplier<?>> serializer) {
        Registry.register(CCPacksRegistries.CONTENT_FACTORY, serializer.getSerializerId(), serializer);
    }
}
