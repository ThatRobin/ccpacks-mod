package io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.EntityRenderer;

import io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.Entities.DDMushroomCowEntity;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.Entities.DDPigEntity;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.FeatureRenderer.DDMushroomCowFeatureRenderer;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.Models.DDMushroomCowEntityModel;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.Models.DDPigEntityModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.SaddleFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PigEntityModel;
import net.minecraft.util.Identifier;

public class DDPigEntityRenderer extends MobEntityRenderer<DDPigEntity, DDPigEntityModel<DDPigEntity>> {
    public static Identifier TEXTURE = new Identifier("textures/entity/cow/brown_mooshroom.png");

    public DDPigEntityRenderer(EntityRendererFactory.Context context, Identifier texture) {
        super(context, new DDPigEntityModel(context.getPart(EntityModelLayers.PIG)), 0.7F);
        this.TEXTURE = texture;
        this.addFeature(new SaddleFeatureRenderer(this, new PigEntityModel(context.getPart(EntityModelLayers.PIG_SADDLE)), new Identifier("textures/entity/pig/pig_saddle.png")));
    }

    @Override
    public Identifier getTexture(DDPigEntity entity) {
        return TEXTURE;
    }

}
