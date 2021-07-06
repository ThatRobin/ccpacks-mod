package io.github.connor3001.ccpacks.middleManTypes;

import io.github.apace100.apoli.ApoliClient;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class MMKeybind {

    public MMKeybind(String name, String catagory){
        KeyBinding key = new KeyBinding("key.ccpacks."+name, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category." + catagory);
        ApoliClient.registerPowerKeybinding("key.ccpacks."+name, key);
        KeyBindingHelper.registerKeyBinding(key);
    }
}
