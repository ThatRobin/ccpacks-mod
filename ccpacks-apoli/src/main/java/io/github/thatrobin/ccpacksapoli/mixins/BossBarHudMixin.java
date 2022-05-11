package io.github.thatrobin.ccpacksapoli.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.thatrobin.ccpacksapoli.power.BossBar;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Mixin(BossBarHud.class)
public class BossBarHudMixin extends DrawableHelper {

    @Shadow @Final private Map<UUID, ClientBossBar> bossBars;

    @Shadow @Final private MinecraftClient client;

    @Inject(method = "Lnet/minecraft/client/gui/hud/BossBarHud;render(Lnet/minecraft/client/util/math/MatrixStack;)V", at = @At("HEAD"), cancellable = true)
    public void renderStart(MatrixStack matrices, CallbackInfo ci) {
        if (this.bossBars.isEmpty()) {
            int baseValue = this.bossBars.size();
            int i = this.client.getWindow().getScaledWidth();
            int j = (baseValue * (10 + this.client.textRenderer.fontHeight)) + 12;
            PlayerEntity player = this.client.player;
            List<BossBar> bars = PowerHolderComponent.getPowers(player, BossBar.class);
            bars.sort((o1, o2) -> Integer.valueOf(o1.getRenderSettings().getPriority()).compareTo(o2.getRenderSettings().getPriority()));
            bars = bars.stream().filter(BossBar::shouldRender).collect(Collectors.toList());
            for (int bar = 0; bar < bars.size(); bar++) {
                BossBar bossBar = bars.get(bar);
                int k = i / 2 - 91;
                int l = j - 9;
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                RenderSystem.setShaderTexture(0, bossBar.getRenderSettings().getSpriteLocation());
                renderBossBarResource(matrices, k, l, bossBar);
                j += 10;
            }
            ci.cancel();
        }

    }

    @Inject(method = "Lnet/minecraft/client/gui/hud/BossBarHud;render(Lnet/minecraft/client/util/math/MatrixStack;)V", at = @At("TAIL"))
    public void render(MatrixStack matrices, CallbackInfo ci) {
        int baseValue = this.bossBars.size();
        int i = this.client.getWindow().getScaledWidth();
        int j = (baseValue * (10 + this.client.textRenderer.fontHeight)) + 12;
        PlayerEntity player = this.client.player;
        List<BossBar> bars = PowerHolderComponent.getPowers(player, BossBar.class);
        bars.sort((o1, o2) -> Integer.valueOf(o1.getRenderSettings().getPriority()).compareTo(o2.getRenderSettings().getPriority()));
        bars = bars.stream().filter(BossBar::shouldRender).collect(Collectors.toList());
        for(int bar = 0; bar < bars.size(); bar ++) {
            BossBar bossBar = bars.get(bar);
            int k = i / 2 - 91;
            int l = j - 9;
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.setShaderTexture(0, bossBar.getRenderSettings().getSpriteLocation());
            renderBossBarResource(matrices, k, l, bossBar);
            j += 10;
        }
    }

    private void renderBossBarResource(MatrixStack matrices, int x, int y, BossBar bossBar) {
        this.drawTexture(matrices, x, y, 0, bossBar.getRenderSettings().getBarIndex() * 5 * 2, 182, 5);
        int i;
        if ((i = (int) (bossBar.getPercentage() * 183.0f)) > 0) {
            if(bossBar.getRenderSettings().isInverted()) {
                i = 183 - i;
            }
            this.drawTexture(matrices, x, y, 0, bossBar.getRenderSettings().getBarIndex() * 5 * 2 + 5, i, 5);
        }
    }

}
