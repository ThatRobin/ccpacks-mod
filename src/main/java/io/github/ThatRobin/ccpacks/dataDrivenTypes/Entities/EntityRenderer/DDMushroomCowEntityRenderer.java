package io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.EntityRenderer;

import io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.Entities.DDMushroomCowEntity;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.FeatureRenderer.DDMushroomCowFeatureRenderer;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.Models.DDMushroomCowEntityModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;

public class DDMushroomCowEntityRenderer extends MobEntityRenderer<DDMushroomCowEntity, DDMushroomCowEntityModel<DDMushroomCowEntity>> {
    public static Identifier TEXTURE = new Identifier("textures/entity/cow/brown_mooshroom.png");

    public DDMushroomCowEntityRenderer(EntityRendererFactory.Context context, Identifier identifier, Identifier backItem) {
        super(context, new DDMushroomCowEntityModel(context.getPart(EntityModelLayers.MOOSHROOM)), 0.7F);
        this.TEXTURE = identifier;
        this.addFeature(new DDMushroomCowFeatureRenderer(this, backItem));
    }

    @Override
    public Identifier getTexture(DDMushroomCowEntity entity) {
        return TEXTURE;
    }

}
