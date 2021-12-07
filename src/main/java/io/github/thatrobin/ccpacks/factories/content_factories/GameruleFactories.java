package io.github.thatrobin.ccpacks.factories.content_factories;

import com.google.common.collect.Lists;
import io.github.thatrobin.ccpacks.CCPackDataTypes;
import io.github.thatrobin.ccpacks.registries.CCPacksRegistries;
import io.github.thatrobin.ccpacks.util.GameruleHolder;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameRules;

import java.util.List;
import java.util.function.Supplier;

public class GameruleFactories {


    public static Identifier identifier(String string) {
        return new Identifier("gamerule", string);
    }

    public static void register() {
        register(new ContentFactory<>(identifier("boolean"),
                Types.KEYBIND,
                new SerializableData()
                        .add("powers", SerializableDataTypes.IDENTIFIERS, Lists.newArrayList())
                        .add("entity_entry", CCPackDataTypes.ENTITY_ENTRY, Lists.newArrayList()),
                data ->
                        (contentType, content) -> (Supplier<GameruleHolder>) () -> {
                            GameRules.Key<GameRules.BooleanRule> gamerule = GameRuleRegistry.register(contentType.getIdentifier().toString().replace(":", "."), GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(true));
                            return new GameruleHolder(gamerule, (List<Identifier>)data.get("powers"), (List<EntityType>)data.get("entity_entry"));
                        }));
    }

    private static void register(ContentFactory<Supplier<?>> serializer) {
        Registry.register(CCPacksRegistries.CONTENT_FACTORY, serializer.getSerializerId(), serializer);
    }
}
