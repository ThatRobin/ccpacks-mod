package io.github.ThatRobin.ccpacks.DataDrivenClasses.Entities.client.renderer;

import io.github.ThatRobin.ccpacks.DataDrivenClasses.Entities.DDAnimalEntity;
import io.github.ThatRobin.ccpacks.DataDrivenClasses.Entities.client.model.AnimalModel;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class DDGeoRenderer extends GeoEntityRenderer<DDAnimalEntity> {

    public DDGeoRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new AnimalModel());
    }

    @Override
    public RenderLayer getRenderType(DDAnimalEntity animatable, float partialTicks, MatrixStack stack,
                                     VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                     Identifier textureLocation) {
        return RenderLayer.getEntityTranslucent(getTextureLocation(animatable));
    }
}