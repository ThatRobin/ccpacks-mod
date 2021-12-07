package io.github.thatrobin.ccpacks.util;

import net.minecraft.client.render.RenderLayer;

public enum RenderLayerTypes {
    SOLID("solid", RenderLayer.getSolid()),
    CUTOUT("cutout", RenderLayer.getCutout()),
    TRANSLUCENT("shovels", RenderLayer.getTranslucent());

    public String name;
    public RenderLayer renderLayer;

    RenderLayerTypes(String name, RenderLayer renderLayer) {
        this.name=name;
        this.renderLayer=renderLayer;
    }
}
