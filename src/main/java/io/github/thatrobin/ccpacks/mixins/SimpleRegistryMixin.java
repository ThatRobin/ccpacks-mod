package io.github.thatrobin.ccpacks.mixins;

import blue.endless.jankson.annotation.Nullable;
import com.mojang.serialization.Lifecycle;
import io.github.thatrobin.ccpacks.util.ExtendedRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Function;

@Mixin(SimpleRegistry.class)
public abstract class SimpleRegistryMixin<T> extends Registry<T> implements ExtendedRegistry<T> {

    @Shadow
    private boolean frozen;
    @Shadow @Final
    @Nullable
    private Function<T, RegistryEntry.Reference<T>> valueToEntryFunction;
    @Shadow @Nullable private Map<T, RegistryEntry.Reference<T>> unfrozenValueToEntry;

    protected SimpleRegistryMixin(RegistryKey<? extends Registry<T>> key, Lifecycle lifecycle) {
        super(key, lifecycle);
    }

    @Override
    public void dynreg$unfreeze() {
        frozen = false;

        if (valueToEntryFunction != null)
            this.unfrozenValueToEntry = new IdentityHashMap<>();
    }
}
