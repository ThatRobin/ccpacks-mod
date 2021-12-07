package io.github.thatrobin.ccpacks.client.model.armor;

import io.github.thatrobin.ccpacks.data_driven_classes.items.DDArmorItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class JsonArmorModel extends AnimatedGeoModel<DDArmorItem> {

    private String name;
    private Identifier id;

    public JsonArmorModel (Identifier id, String name) {
        this.name = name;
        this.id = id;
    }

    @Override
    public Identifier getModelLocation(DDArmorItem object) {
        return new Identifier(id.getNamespace(), "geo/"+this.name+".geo.json");
    }

    @Override
    public Identifier getTextureLocation(DDArmorItem object) {
        return new Identifier(id.getNamespace(), "textures/entity/armor/"+this.name+".png");
    }

    @Override
    public Identifier getAnimationFileLocation(DDArmorItem animatable) {
        return new Identifier(id.getNamespace(), "animations/entity/armor/"+this.name+".animation.json");
    }
}