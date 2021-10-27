package io.github.ThatRobin.ccpacks.Factories.ContentFactories;

import io.github.ThatRobin.ccpacks.Registries.CCPacksRegistries;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

public class SoundEventFactories {

    public static Identifier identifier(String string) {
        return new Identifier("sound", string);
    }

    public static void register() {
        register(new ContentFactory<>(identifier("sound_event"), Types.SOUND,
                new SerializableData(),
                data ->
                        (contentType, content) -> null));
    }

    private static void register(ContentFactory<Supplier<?>> serializer) {
        Registry.register(CCPacksRegistries.CONTENT_FACTORY, serializer.getSerializerId(), serializer);
    }
}
