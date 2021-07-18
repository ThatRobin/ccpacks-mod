package io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.EntityRenderer;

import io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.Entities.DDCowEntity;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.Entities.DDMushroomCowEntity;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.FeatureRenderer.DDMushroomCowFeatureRenderer;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.Models.DDCowEntityModel;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.Models.DDMushroomCowEntityModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;

public class DDCowEntityRenderer extends MobEntityRenderer<DDCowEntity, DDCowEntityModel<DDCowEntity>> {
    public static Identifier TEXTURE = new Identifier("textures/entity/cow/brown_mooshroom.png");

    public DDCowEntityRenderer(EntityRendererFactory.Context context, Identifier identifier) {
        super(context, new DDCowEntityModel(context.getPart(EntityModelLayers.COW)), 0.7F);
        this.TEXTURE = identifier;
    }

    @Override
    public Identifier getTexture(DDCowEntity entity) {
        return TEXTURE;
    }

}
