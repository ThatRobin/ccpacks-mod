package io.github.thatrobin.ccpacks.client.renderer.item;

import io.github.thatrobin.ccpacks.data_driven_classes.items.DDShieldItem;
import io.github.thatrobin.ccpacks.client.model.item.DDShieldItemModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class DDShieldItemRenderer extends GeoItemRenderer<DDShieldItem> {
    public DDShieldItemRenderer() {
        super(new DDShieldItemModel());
    }
}

