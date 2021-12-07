package io.github.thatrobin.ccpacks.client.model.item;

import io.github.thatrobin.ccpacks.data_driven_classes.items.DDShieldItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.GeckoLib;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DDShieldItemModel extends AnimatedGeoModel<DDShieldItem> {

    @Override
    public Identifier getModelLocation(DDShieldItem object)
    {
        return new Identifier(GeckoLib.ModID, "geo/jack.geo.json");
    }

    @Override
    public Identifier getTextureLocation(DDShieldItem object)
    {
        return new Identifier(GeckoLib.ModID, "textures/item/jack.png");
    }

    @Override
    public Identifier getAnimationFileLocation(DDShieldItem object)
    {
        return new Identifier(GeckoLib.ModID, "animations/jackinthebox.animation.json");
    }

}
