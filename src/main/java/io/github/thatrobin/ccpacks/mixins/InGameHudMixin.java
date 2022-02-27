package io.github.thatrobin.ccpacks.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.apace100.apoli.power.Power;
import io.github.thatrobin.ccpacks.power.GuiElementPower;
import io.github.thatrobin.ccpacks.power.StatBar;
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {

    @Shadow
    private int scaledWidth;

    @Shadow
    protected abstract PlayerEntity getCameraPlayer();

    @Shadow
    private int scaledHeight;

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "renderStatusBars", at = @At("TAIL"))
    private void renderStatusBars(MatrixStack matrices, CallbackInfo ci) {
        PlayerEntity playerEntity = getCameraPlayer();

        int leftBars = 0;
        int rightBars = 0;

        int airMax = playerEntity.getMaxAir();
        int cappedAir = Math.min(playerEntity.getAir(), airMax);

        for(int bar = 0; bar < PowerHolderComponent.getPowers(playerEntity, StatBar.class).size(); bar ++) {
            StatBar statBar = PowerHolderComponent.getPowers(playerEntity, StatBar.class).get(bar);

            int baseBars = 0;

            if(statBar.getHudRender().getSide().equals("left")) {
                int hasArmor = playerEntity.getArmor() > 0 ? 1 : 0;
                baseBars = 1 + hasArmor + leftBars;
            } else if (statBar.getHudRender().getSide().equals("right")) {
                int isUnderwater = playerEntity.isSubmergedIn(FluidTags.WATER) || cappedAir < airMax ? 1 : 0;
                baseBars = 1 + isUnderwater + rightBars;
            }

            RenderSystem.setShaderTexture(0, statBar.getHudRender().getSpriteLocation());


            int scaledScaledHeight = this.scaledHeight - 39;
            int oscaledScaledWidth = scaledWidth / 2 + 90;
            int ascaledScaledWidth = scaledWidth / 2 - 10;

            this.client.getProfiler().swap("air");



            int oorigY = scaledScaledHeight - (baseBars*10);
            int aorigY = scaledScaledHeight - (baseBars*10);

            int armorToughness = statBar.getValue();
            if(statBar.getHudRender().shouldRender(playerEntity) && armorToughness > 0) {
                int aorigX;
                int oorigX;
                int index = statBar.getHudRender().getBarIndex();
                switch (statBar.getHudRender().getSide()) {
                    case "left" -> {
                        leftBars += 1;
                        for (int i = 0; i < 10; i++) {
                            aorigX = ascaledScaledWidth - (10 - i) * 8;
                            if (i * 2 + 1 < armorToughness) {
                                this.drawTexture(matrices, aorigX, aorigY, 27, index * 9, 9, 9);
                            }

                            if (i * 2 + 1 == armorToughness) {
                                this.drawTexture(matrices, aorigX, aorigY, 9, index * 9, 9, 9);
                            }

                            if (i * 2 + 1 > armorToughness) {
                                this.drawTexture(matrices, aorigX, aorigY, 0, index * 9, 9, 9);
                            }
                        }
                    }
                    case "right" -> {
                        rightBars += 1;
                        armorToughness += 2;
                        for (int i = 10; i > 0; i--) {
                            oorigX = oscaledScaledWidth - i * 8;
                            if (i * 2 + 1 < armorToughness) {
                                this.drawTexture(matrices, oorigX, oorigY, 27, index * 9, 9, 9);
                            }

                            if (i * 2 + 1 == armorToughness) {
                                this.drawTexture(matrices, oorigX, oorigY, 18, index * 9, 9, 9);
                            }

                            if (i * 2 + 1 > armorToughness) {
                                this.drawTexture(matrices, oorigX, oorigY, 0, index * 9, 9, 9);
                            }
                        }
                    }
                }

                client.getProfiler().pop();
            }
        }
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void render(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        PowerHolderComponent.getPowers(this.client.player, GuiElementPower.class).forEach((guiElementPower -> {
            if (this.client.options.getPerspective().isFirstPerson()) {
                if(guiElementPower.isActive()) {
                    this.renderOverlay(guiElementPower.texture, guiElementPower.opacity, guiElementPower.minx, guiElementPower.miny, guiElementPower.maxx, guiElementPower.maxy);
                }
            }
        }));
    }

    private void renderOverlay(Identifier texture, float opacity, float minx, float miny, float maxx, float maxy) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, opacity);
        RenderSystem.setShaderTexture(0, texture);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(minx, maxy, -90.0D).texture(0.0F, 1.0F).next();
        bufferBuilder.vertex(maxx, maxy, -90.0D).texture(1.0F, 1.0F).next();
        bufferBuilder.vertex(maxx, miny, -90.0D).texture(1.0F, 0.0F).next();
        bufferBuilder.vertex(minx, miny, -90.0D).texture(0.0F, 0.0F).next();
        tessellator.draw();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, opacity);
    }
}