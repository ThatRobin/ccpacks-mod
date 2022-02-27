package io.github.thatrobin.ccpacks.mixins;

import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(KeyBinding.class)
public interface KeyBindingAccessor {

    @Accessor("KEYS_BY_ID")
    public static Map<String, KeyBinding> getKeys() {
        throw new AssertionError();
    }
}
