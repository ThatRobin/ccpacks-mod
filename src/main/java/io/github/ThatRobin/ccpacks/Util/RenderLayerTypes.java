package io.github.ThatRobin.ccpacks.Util;

import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;

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
