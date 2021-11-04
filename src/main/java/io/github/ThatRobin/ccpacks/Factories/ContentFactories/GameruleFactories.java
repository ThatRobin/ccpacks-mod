package io.github.ThatRobin.ccpacks.Factories.ContentFactories;

import com.google.common.collect.Lists;
import io.github.ThatRobin.ccpacks.CCPackDataTypes;
import io.github.ThatRobin.ccpacks.Registries.CCPacksRegistries;
import io.github.ThatRobin.ccpacks.Util.GameruleHolder;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameRules;
import org.lwjgl.glfw.GLFW;

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
