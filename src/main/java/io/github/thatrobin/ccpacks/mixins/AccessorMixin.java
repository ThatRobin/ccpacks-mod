package io.github.thatrobin.ccpacks.mixins;

import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(ResourcePackManager.class)
public interface AccessorMixin {

    @Accessor("providers")
    @Mutable
    Set<ResourcePackProvider> getProviders();

    @Accessor("providers")
    void setProviders(Set<ResourcePackProvider> value);

}