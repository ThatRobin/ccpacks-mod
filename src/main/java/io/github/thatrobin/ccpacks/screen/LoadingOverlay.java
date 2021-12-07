package io.github.thatrobin.ccpacks.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.thatrobin.ccpacks.CCPacksMain;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.BackgroundHelper;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.resource.metadata.TextureResourceMetadata;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.DefaultResourcePack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.IntSupplier;

@Environment(EnvType.CLIENT)
public class LoadingOverlay extends Overlay {
    static final Identifier LOGO = CCPacksMain.identifier("textures/gui/title/icon.png");
    private static final int CCPACKS_BLUE = BackgroundHelper.ColorMixer.getArgb(255, 25, 125, 143);
    private static final int MONOCHROME_BLACK = BackgroundHelper.ColorMixer.getArgb(255, 0, 0, 0);
    private static final IntSupplier BRAND_ARGB = () -> {
        return MinecraftClient.getInstance().options.monochromeLogo ? MONOCHROME_BLACK : CCPACKS_BLUE;
    };
    private final MinecraftClient client;
    private final boolean reloading;
    private float progress;
    private long reloadStartTime = -1L;

    public LoadingOverlay(MinecraftClient client, boolean reloading) {
        this.client = client;
        this.reloading = reloading;
    }

    public void init(MinecraftClient client) {
        client.getTextureManager().registerTexture(LOGO, new LoadingOverlay.LogoTexture());
    }

    private static int withAlpha(int color, int alpha) {
        return color & 16777215 | alpha << 24;
    }

    public void setProgress(float progress){
        this.progress = progress;
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int i = this.client.getWindow().getScaledWidth();
        int j = this.client.getWindow().getScaledHeight();
        long l = Util.getMeasuringTimeMs();
        if (this.reloading && this.reloadStartTime == -1L) {
            this.reloadStartTime = l;
        }

        float g = this.reloadStartTime > -1L ? (float)(l - this.reloadStartTime) / 500.0F : -1.0F;
        float s;
        int m;
        if (progress/100 >= 1.0F) {
            if (this.client.currentScreen != null) {
                this.client.currentScreen.render(matrices, 0, 0, delta);
            }

            m = MathHelper.ceil((1.0F - MathHelper.clamp(progress/100 - 1.0F, 0.0F, 1.0F)) * 255.0F);
            fill(matrices, 0, 0, i, j, withAlpha(BRAND_ARGB.getAsInt(), m));
            s = 1.0F - MathHelper.clamp(progress/100 - 1.0F, 0.0F, 1.0F);
        } else if (this.reloading) {
            if (this.client.currentScreen != null && g < 1.0F) {
                this.client.currentScreen.render(matrices, mouseX, mouseY, delta);
            }

            m = MathHelper.ceil(MathHelper.clamp((double)g, 0.15D, 1.0D) * 255.0D);
            fill(matrices, 0, 0, i, j, withAlpha(BRAND_ARGB.getAsInt(), m));
            s = MathHelper.clamp(g, 0.0F, 1.0F);
        } else {
            m = BRAND_ARGB.getAsInt();
            float p = (float)(m >> 16 & 255) / 255.0F;
            float q = (float)(m >> 8 & 255) / 255.0F;
            float r = (float)(m & 255) / 255.0F;
            GlStateManager._clearColor(p, q, r, 1.0F);
            GlStateManager._clear(16384, MinecraftClient.IS_SYSTEM_MAC);
            s = 1.0F;
        }

        m = (int)((double)this.client.getWindow().getScaledWidth() * 0.5D);
        int u = (int)((double)this.client.getWindow().getScaledHeight() * 0.5D);
        double d = Math.min((double)this.client.getWindow().getScaledWidth() * 0.75D, (double)this.client.getWindow().getScaledHeight()) * 0.25D;
        int v = (int)(d * 0.5D);
        double e = d * 4.0D;
        int w = (int)(e * 0.5D);
        RenderSystem.setShaderTexture(0, LOGO);
        RenderSystem.enableBlend();
        RenderSystem.blendEquation(32774);
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, s);
        drawTexture(matrices, this.client.getWindow().getScaledWidth()/2 - 30, u - v, 60, 60, 0, 0, 60, 60, 60, 60);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        int x = (int)((double)this.client.getWindow().getScaledHeight() * 0.8325D);
        float alpha;
        if(progress < 5) {
            alpha = inverseLerp(0, 5, progress);
        } else if (progress > 95) {
            alpha = inverseLerp(100, 95, progress);
        } else {
            alpha = 1;
        }
        if (progress < 99) {
            this.renderProgressBar(matrices, i / 2 - w, x - 5, i / 2 + w, x + 5, alpha);
        }

        if (progress >= 99) {
            this.client.setOverlay((Overlay)null);
        }

        if (this.reloading) {
            if (this.client.currentScreen != null) {
                this.client.currentScreen.init(this.client, this.client.getWindow().getScaledWidth(), this.client.getWindow().getScaledHeight());
            }
        }

    }

    private void renderProgressBar(MatrixStack matrices, int minX, int minY, int maxX, int maxY, float opacity) {
        int i = MathHelper.ceil(this.progress * 2);
        int j = Math.round(opacity * 255.0F);
        int k = BackgroundHelper.ColorMixer.getArgb(j, 255, 255, 255);
        fill(matrices, minX + 2, minY + 2, minX + i, maxY - 2, k);
        fill(matrices, minX + 1, minY, maxX - 1, minY + 1, k);
        fill(matrices, minX + 1, maxY, maxX - 1, maxY - 1, k);
        fill(matrices, minX, minY, minX + 1, maxY, k);
        fill(matrices, maxX, minY, maxX - 1, maxY, k);
    }

    private float inverseLerp(float A, float B, float T)
    {
        return (T - A)/(B - A);
    }

    public boolean pausesGame() {
        return true;
    }

    @Environment(EnvType.CLIENT)
    private static class LogoTexture extends ResourceTexture {
        public LogoTexture() {
            super(LoadingOverlay.LOGO);
        }

        protected TextureData loadTextureData(ResourceManager resourceManager) {
            MinecraftClient minecraftClient = MinecraftClient.getInstance();
            DefaultResourcePack defaultResourcePack = minecraftClient.getResourcePackProvider().getPack();

            try {
                InputStream inputStream = defaultResourcePack.open(ResourceType.CLIENT_RESOURCES, LoadingOverlay.LOGO);

                TextureData var5;
                try {
                    var5 = new TextureData(new TextureResourceMetadata(true, true), NativeImage.read(inputStream));
                } catch (Throwable var8) {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (Throwable var7) {
                            var8.addSuppressed(var7);
                        }
                    }

                    throw var8;
                }

                if (inputStream != null) {
                    inputStream.close();
                }

                return var5;
            } catch (IOException var9) {
                return new TextureData(var9);
            }
        }
    }
}
