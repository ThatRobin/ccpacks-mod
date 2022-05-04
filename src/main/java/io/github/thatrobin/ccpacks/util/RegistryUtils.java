package io.github.thatrobin.ccpacks.util;

import net.minecraft.util.registry.Registry;

public class RegistryUtils {

    public static void unfreeze(Registry<?> registry) {
        ((ExtendedRegistry<?>) registry).dynreg$unfreeze();
    }
}
