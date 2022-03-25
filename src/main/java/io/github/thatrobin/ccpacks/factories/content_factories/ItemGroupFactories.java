package io.github.thatrobin.ccpacks.factories.content_factories;

import com.google.common.collect.Lists;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.thatrobin.ccpacks.CCPackDataTypes;
import io.github.thatrobin.ccpacks.registries.CCPacksRegistries;
import io.github.thatrobin.ccpacks.util.GameruleHolder;
import io.github.thatrobin.ccpacks.util.ItemGroupHolder;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameRules;

import java.util.List;
import java.util.function.Supplier;

public class ItemGroupFactories {

    public static Identifier identifier(String string) {
        return new Identifier("item_group", string);
    }

    public static void register() {
        register(new ContentFactory<>(identifier("generic"), Types.ITEM_GROUP,
                new SerializableData(),
                data ->
                        (contentType, content) -> (Supplier<ItemGroupHolder>) () -> {
                            return null;
                        }));

        register(new ContentFactory<>(identifier("tabbed"), Types.ITEM_GROUP,
                new SerializableData(),
                data ->
                        (contentType, content) -> (Supplier<ItemGroupHolder>) () -> {
                            return null;
                        }));
    }

    private static void register(ContentFactory<Supplier<?>> serializer) {
        Registry.register(CCPacksRegistries.CONTENT_FACTORY, serializer.getSerializerId(), serializer);
    }
}
