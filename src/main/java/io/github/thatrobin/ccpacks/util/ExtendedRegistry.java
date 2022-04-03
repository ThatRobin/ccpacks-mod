package io.github.thatrobin.ccpacks.util;

import net.minecraft.util.registry.RegistryKey;

public interface ExtendedRegistry<T> {

    void dynreg$remove(RegistryKey<T> key);

    void dynreg$unfreeze();
}