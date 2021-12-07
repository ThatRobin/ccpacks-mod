package io.github.thatrobin.ccpacks.mixins;

import io.github.thatrobin.ccpacks.util.ZipPack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

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
