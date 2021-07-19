package io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.EntityRenderer;

import io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.Entities.DDPigEntity;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.Entities.DDSheepEntity;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.Models.DDPigEntityModel;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.Models.DDSheepEntityModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.SheepEntityRenderer;
import net.minecraft.client.render.entity.feature.SaddleFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PigEntityModel;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.Identifier;

public class DDSheepEntityRenderer extends SheepEntityRenderer {
    public static Identifier TEXTURE = new Identifier("textures/entity/cow/brown_mooshroom.png");

    public DDSheepEntityRenderer(EntityRendererFactory.Context context, Identifier texture) {
        super(context);
        this.TEXTURE = texture;
    }

    @Override
    public Identifier getTexture(SheepEntity entity) {
        return TEXTURE;
    }
}
