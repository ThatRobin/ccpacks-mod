package io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.EntityRenderer;

import io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.Entities.DDChickenEntity;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.Entities.DDPigEntity;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.Models.DDChickenEntityModel;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.Models.DDPigEntityModel;
import net.minecraft.client.render.entity.ChickenEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.SaddleFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PigEntityModel;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.util.Identifier;

public class DDChickenEntityRenderer extends ChickenEntityRenderer {
    public static Identifier TEXTURE = new Identifier("textures/entity/cow/brown_mooshroom.png");

    public DDChickenEntityRenderer(EntityRendererFactory.Context context, Identifier texture) {
        super(context);
        this.TEXTURE = texture;
    }

    @Override
    public Identifier getTexture(ChickenEntity entity) {
        return TEXTURE;
    }

}
