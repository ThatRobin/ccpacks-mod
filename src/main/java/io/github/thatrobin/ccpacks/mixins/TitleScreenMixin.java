package io.github.thatrobin.ccpacks.mixins;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.thatrobin.ccpacks.util.ZipPack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

    @Shadow private long backgroundFadeStart;
    @Shadow @Final private boolean doBackgroundFade;
    private static final Identifier ICON_TEXTURE = new Identifier("ccpacks", "textures/gui/build.png");
    public ZipPack pack = new ZipPack();

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "initWidgetsNormal", at = @At("RETURN"))
    private void initWidgetsNormal(int y, int spacingY, CallbackInfo ci) {
        this.addDrawableChild(new TexturedButtonWidget(this.width / 2 - 100 + 205, y, 20, 20, 0, 0, 20, ICON_TEXTURE, 32, 64, (button) -> pack.zipPack((TitleScreen) (Object) this, this.client)));
    }


}
