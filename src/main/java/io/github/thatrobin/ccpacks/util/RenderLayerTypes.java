package io.github.thatrobin.ccpacks.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;

public enum RenderLayerTypes {
    SOLID("solid"),
    CUTOUT("cutout"),
    TRANSLUCENT("translucent");

    public String name;

    RenderLayerTypes(String name) {
        this.name=name;
    }
}
