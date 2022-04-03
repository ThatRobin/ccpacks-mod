package io.github.thatrobin.ccpacks.mixins;

import com.mojang.serialization.Lifecycle;
import io.github.thatrobin.ccpacks.util.DeletableObjectInternal;
import io.github.thatrobin.ccpacks.util.ExtendedRegistry;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.fabricmc.fabric.api.event.registry.RegistryEntryRemovedCallback;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.*;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    @Shadow public abstract Optional<RegistryEntry<T>> getEntry(RegistryKey<T> key);

    @Shadow @Final private Object2IntMap<T> entryToRawId;
    @Shadow @Final private ObjectList<RegistryEntry.Reference<T>> rawIdToEntry;
    @Shadow @Final private Map<Identifier, RegistryEntry.Reference<T>> idToEntry;
    @Shadow @Final private Map<RegistryKey<T>, RegistryEntry.Reference<T>> keyToEntry;
    @Shadow @Final private Map<T, RegistryEntry.Reference<T>> valueToEntry;
    @Shadow @Final private Map<T, Lifecycle> entryToLifecycle;
    @Shadow private volatile Map<TagKey<T>, RegistryEntryList.Named<T>> tagToEntryList;
    @Shadow @Nullable private List<RegistryEntry.Reference<T>> cachedEntries;

    @Override
    public void dynreg$remove(RegistryKey<T> key) {
        if (frozen) {
            throw new IllegalStateException("Registry is frozen (trying to remove key " + key + ")");
        }

        RegistryEntry.Reference<T> entry = (RegistryEntry.Reference<T>) getEntry(key).orElseThrow();
        int rawId = entryToRawId.getInt(entry.value());
        RegistryEntryRemovedCallback.event(this).invoker().onEntryRemoved(rawId, entry.registryKey().getValue(), entry.value());

        rawIdToEntry.set(rawId, null);
        entryToRawId.removeInt(entry);
        idToEntry.remove(key.getValue());
        keyToEntry.remove(key);
        valueToEntry.remove(entry.value());
        entryToLifecycle.remove(entry.value());
        cachedEntries = null;

        ((DeletableObjectInternal) entry).markAsDeleted();

        if (entry.value() instanceof DeletableObjectInternal obj)
            obj.markAsDeleted();
    }

    @Override
    public void dynreg$unfreeze() {
        frozen = false;

        if (valueToEntryFunction != null)
            this.unfrozenValueToEntry = new IdentityHashMap<>();
    }
}
