package io.github.ThatRobin.ccpacks.Factories.ContentFactories;

import io.github.ThatRobin.ccpacks.Registries.CCPacksRegistries;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.lwjgl.glfw.GLFW;

import java.util.function.Supplier;

public class KeybindFactories {

    public static Identifier identifier(String string) {
        return new Identifier("keybind", string);
    }

    public static void register() {
        register(new ContentFactory<>(identifier("apoli"),
                Types.KEYBIND,
                new SerializableData()
                        .add("name", SerializableDataTypes.STRING, null)
                        .add("category", SerializableDataTypes.STRING, null),
                data ->
                        (contentType, content) -> (Supplier<KeyBinding>) () -> {
                            if (data.isPresent("name") && data.isPresent("category")) {
                                return new KeyBinding("key.ccpacks." + data.getString("name"), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category." + data.getString("category"));
                            } else {
                                return new KeyBinding("key."+contentType.getIdentifier().getNamespace()+"."+contentType.getIdentifier().getPath(), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category." + contentType.getIdentifier().getNamespace());
                            }

                        }));
    }

    private static void register(ContentFactory<Supplier<?>> serializer) {
        Registry.register(CCPacksRegistries.CONTENT_FACTORY, serializer.getSerializerId(), serializer);
    }
}
