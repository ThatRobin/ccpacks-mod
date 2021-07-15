package io.github.ThatRobin.ccpacks.dataDrivenTypes;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.SlimeEntityRenderer;
import net.minecraft.client.render.entity.feature.MooshroomMushroomFeatureRenderer;
import net.minecraft.client.render.entity.feature.SlimeOverlayFeatureRenderer;
import net.minecraft.client.render.entity.model.CowEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.SlimeEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class DDEntityRenderer extends MobEntityRenderer<DDEntity, DDEntityModel<DDEntity>> {
    public static Identifier TEXTURE = new Identifier("textures/entity/cow/brown_mooshroom.png");

    public DDEntityRenderer(EntityRendererFactory.Context context, Identifier identifier) {
        super(context, new DDEntityModel(context.getPart(EntityModelLayers.MOOSHROOM)), 0.7F);
        this.TEXTURE = identifier;
        this.addFeature(new DDFeatureRenderer(this));
    }

    @Override
    public Identifier getTexture(DDEntity entity) {
        return TEXTURE;
    }

}
