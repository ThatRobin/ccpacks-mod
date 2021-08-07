package io.github.ThatRobin.ccpacks.Screen;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.ThatRobin.ccpacks.util.Choice;
import io.github.ThatRobin.ccpacks.util.Outcome;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.*;
import net.minecraft.util.Identifier;

import java.util.List;

public class ChoiceScreen extends Screen {

    private static final Identifier WINDOW = new Identifier(CCPacksMain.MODID, "textures/gui/choice_screen.png");

    private int maxSelection = 0;
    private static final int windowWidth = 256;
    private static final int windowHeight = 182;
    private int border = 13;

    private int guiTop, guiLeft;

    private boolean showDirtBackground;

    public ChoiceScreen(boolean showDirtBackground, Choice choices) {
        super(new TranslatableText(CCPacksMain.MODID+ ".screen.choose_option"));
        this.showDirtBackground = showDirtBackground;
    }


    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    protected void init() {
        super.init();
        guiLeft = (this.width - windowWidth) / 2;
        guiTop = (this.height - windowHeight) / 2;
        addDrawableChild(new ButtonWidget(guiLeft - 40,this.height / 2 - 10, 20, 20, new LiteralText("<"), b -> {
            this.client.openScreen((Screen)null);
        }));
        addDrawableChild(new ButtonWidget(guiLeft + windowWidth + 20, this.height / 2 - 10, 20, 20, new LiteralText(">"), b -> {
            this.client.openScreen((Screen)null);
        }));
        addDrawableChild(new ButtonWidget(guiLeft + windowWidth / 2 - 50, guiTop + windowHeight + 5, 100, 20, new TranslatableText(CCPacksMain.MODID + ".gui.select"), b -> {
            this.client.openScreen((Screen)null);
        }));
    }

    @Override
    public void renderBackground(MatrixStack matrices, int vOffset) {
        if(showDirtBackground) {
            super.renderBackgroundTexture(vOffset);
        } else {
            super.renderBackground(matrices, vOffset);
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.renderOriginWindow(matrices);
        super.render(matrices, mouseX, mouseY, delta);
    }

    private void renderOriginWindow(MatrixStack matrices) {
        RenderSystem.enableBlend();
        renderWindowBackground(matrices, 8, 0);
        this.renderOriginContent(matrices);
        RenderSystem.setShaderTexture(0, WINDOW);
        this.drawTexture(matrices, guiLeft, guiTop, 0, 0, windowWidth, windowHeight);
        renderOriginName(matrices);
        RenderSystem.setShaderTexture(0, WINDOW);
        Text title = new TranslatableText(CCPacksMain.MODID + ".gui.choose_option.title");
        this.drawCenteredText(matrices, this.textRenderer, title.getString(), width / 2, guiTop - 15, 0xFFFFFF);
        RenderSystem.disableBlend();
    }

    private void renderOriginName(MatrixStack matrices) {
        StringVisitable originName = textRenderer.trimToWidth(new TranslatableText(CCPacksMain.MODID + ".gui.option.title"), windowWidth - 116);
        this.drawStringWithShadow(matrices, textRenderer, originName.getString(), guiLeft + 30, guiTop + 13, 0xFFFFFF);
        ItemStack is = Items.ANCIENT_DEBRIS.getDefaultStack();
        this.itemRenderer.renderInGui(is, guiLeft + 9, guiTop + 9);
    }

    private void renderWindowBackground(MatrixStack matrices, int offsetYStart, int offsetYEnd) {
        int endX = guiLeft + windowWidth - border;
        int endY = guiTop + windowHeight - border;
        RenderSystem.setShaderTexture(0, WINDOW);
        for(int x = guiLeft; x < endX; x += 16) {
            for(int y = guiTop + offsetYStart; y < endY + offsetYEnd; y += 16) {
                this.drawTexture(matrices, x, y, 0, windowHeight, Math.max(16, endX - x), Math.max(16, endY + offsetYEnd - y));
            }
        }
    }

    private void renderOriginContent(MatrixStack matrices) {
        int x = guiLeft + 113;
        int y = guiTop + 18;
        int startY = y;
        int endY = y - 72 + windowHeight;

        Text orgDesc = new TranslatableText("ccpacks.gui.description");
        List<OrderedText> descLines = textRenderer.wrapLines(orgDesc, windowWidth - 140);
        for(OrderedText line : descLines) {
            if(y >= startY - 18 && y <= endY + 12) {
                textRenderer.draw(matrices, line, x + 2, y - 6, 0xCCCCCC);
            }
            y += 12;
        }
    }
}