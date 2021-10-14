package io.github.ThatRobin.ccpacks.Factories.ContentFactories;

import io.github.ThatRobin.ccpacks.CCPacksMain;
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

    @SuppressWarnings("unchecked")
    public static void register() {
        register(new ContentFactory<>(CCPacksMain.identifier("apoli"), Types.ENCHANTMENT,
                new SerializableData()
                        .add("name", SerializableDataTypes.STRING)
                        .add("category", SerializableDataTypes.STRING),
                data ->
                        (contentType, content) -> (Supplier<KeyBinding>) () -> new KeyBinding("key.ccpacks." + data.getString("name"), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category." + data.getString("category"))));
    }

    private static void register(ContentFactory<Supplier<?>> serializer) {
        Registry.register(CCPacksRegistries.CONTENT_FACTORY, serializer.getSerializerId(), serializer);
    }
}
