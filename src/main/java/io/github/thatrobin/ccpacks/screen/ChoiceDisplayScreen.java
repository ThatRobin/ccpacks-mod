package io.github.thatrobin.ccpacks.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.thatrobin.ccpacks.CCPacksMain;
import io.github.thatrobin.ccpacks.choice.Choice;
import io.github.thatrobin.ccpacks.choice.ChoiceLayer;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.LinkedList;
import java.util.List;

public class ChoiceDisplayScreen extends Screen {

    private static final Identifier WINDOW = new Identifier(CCPacksMain.MODID, "textures/gui/choice.png");

    private Choice choice;
    private ChoiceLayer layer;

    protected static final int windowWidth = 256;
    protected static final int windowHeight = 186;
    protected int scrollPos = 0;
    private int currentMaxScroll = 0;

    protected Text description;
    protected Text name;

    protected int guiTop, guiLeft;

    protected final boolean showDirtBackground;

    public List<SelectableButtonWidget> buttons = Lists.newArrayList();

    public ChoiceDisplayScreen(Text title, boolean showDirtBackground) {
        super(title);
        this.showDirtBackground = showDirtBackground;
    }

    public void showChoice(Choice choice, ChoiceLayer layer) {
        this.choice = choice;
        this.layer = layer;
        this.scrollPos = 0;
    }

    @Override
    protected void init() {
        super.init();
        guiLeft = (this.width - windowWidth) / 2;
        guiTop = (this.height - windowHeight) / 2;
        renderPowers();
    }

    public Choice getCurrentChoice() {
        return choice;
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
        this.renderChoiceWindow(matrices, mouseX, mouseY);
        super.render(matrices, mouseX, mouseY, delta);
    }

    private boolean scrolling = false;
    private int scrollDragStart = 0;
    private double mouseDragStart = 0;

    private boolean canScroll() {
        return choice != null && currentMaxScroll > 0;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        scrolling = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(canScroll()) {
            scrolling = false;
            int scrollbarY = 36;
            int maxScrollbarOffset = 141;
            float part = scrollPos / (float)currentMaxScroll;
            scrollbarY += (maxScrollbarOffset - scrollbarY) * part;
            if(mouseX >= guiLeft + 156 && mouseX < guiLeft + 156 + 6) {
                if(mouseY >= guiTop + scrollbarY && mouseY < guiTop + scrollbarY + 27) {
                    scrolling = true;
                    scrollDragStart = scrollbarY;
                    mouseDragStart = mouseY;
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if(this.scrolling) {
            int delta = (int)(mouseY - mouseDragStart);
            int newScrollPos = Math.max(36, Math.min(141, scrollDragStart + delta));
            float part = (newScrollPos - 36) / (float)(141 - 36);
            scrollPos = (int)(part * currentMaxScroll);
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    private void renderChoiceWindow(MatrixStack matrices, int mouseX, int mouseY) {
        RenderSystem.enableBlend();
        buttons = Lists.newArrayList();
        renderWindowBackground(matrices);
        RenderSystem.setShaderTexture(0, WINDOW);
        this.drawTexture(matrices, guiLeft, guiTop, 0, 0, windowWidth, windowHeight);
        if(choice != null) {
            renderChoiceName(matrices);
            renderChoiceContent(matrices);
            renderBadgeTooltip(matrices, mouseX, mouseY);
            RenderSystem.setShaderTexture(0, WINDOW);
            Text title = new TranslatableText(CCPacksMain.MODID + ".gui.choice.title", new TranslatableText(layer.getTranslationKey()));
            drawCenteredText(matrices, this.textRenderer, title.getString(), width / 2, guiTop - 15, 0xFFFFFF);
        }

        RenderSystem.disableBlend();
    }

    private void renderBadgeTooltip(MatrixStack matrices, int mouseX, int mouseY) {
        buttons.forEach(selectableButtonWidget -> {
            if(mouseX >=selectableButtonWidget.x && mouseX < selectableButtonWidget.x + 24) {
                if(mouseY >= selectableButtonWidget.y && mouseY < selectableButtonWidget.y + 24) {
                    String hoverText;
                    if(buttons.indexOf(selectableButtonWidget) == 0) {
                        hoverText = "Description";
                    } else {
                        hoverText = selectableButtonWidget.getMessage().getString();
                    }
                    if(hoverText.contains("\n")) {
                        List<Text> lines = new LinkedList<>();
                        String[] texts = hoverText.split("\n");
                        for (String text : texts) {
                            lines.add(new TranslatableText(text));
                        }
                        renderTooltip(matrices, lines, mouseX, mouseY);
                    } else {
                        Text text = new TranslatableText(hoverText);
                        renderTooltip(matrices, text, mouseX, mouseY);
                    }
                }
            }
        });
    }

    private void renderChoiceName(MatrixStack matrices) {
        StringVisitable choiceName = textRenderer.trimToWidth(getCurrentChoice().getName(), windowWidth - 36);
        drawStringWithShadow(matrices, textRenderer, choiceName.getString(), guiLeft + 12, guiTop + 13, 0xFFFFFF);

    }

    private void renderPowers() {
        List<PowerType<?>> powerTypes = Lists.newArrayList();
        getCurrentChoice().getPowerTypes().forEach(powerType -> {
            if(!powerType.isHidden()) {
                powerTypes.add(powerType);
            }
        });

        int row = -1;
        int column = 0;
        int spacing = 24;

        for(int i = 0; i < powerTypes.size()+1; i++) {
            int finalI = i;
            row++;
            if(row == 4) {
                column++;
                row = 0;
            }
            int x = (guiLeft+8) + (spacing*row);
            int y = (guiTop+31) + (spacing*column);
            int width = 24;
            int height = 24;
            if(i == 0){
                buttons.add(new SelectableButtonWidget(getCurrentChoice(), getCurrentChoice().getIcon(), this.itemRenderer, x, y, width, height, getCurrentChoice().getName(),0,0,24,CCPacksMain.identifier("textures/gui/widgets.png"),256,256, button -> {
                    description = getCurrentChoice().getDescription();
                    name = new LiteralText("Description").formatted(Formatting.UNDERLINE);
                }));
            } else {
                buttons.add(new SelectableButtonWidget(getCurrentChoice(), CCPacksMain.powerIconManager.getIcon(powerTypes.get(i-1).getIdentifier()), this.itemRenderer, x, y, width, height, powerTypes.get(i-1).getName(), 0, 0, 24, CCPacksMain.identifier("textures/gui/widgets.png"), 256, 256, button -> {
                    description = powerTypes.get(finalI-1).getDescription();
                    name = powerTypes.get(finalI-1).getName().formatted(Formatting.UNDERLINE);
                }));
            }
        }

        buttons.forEach(this::addDrawableChild);

    }

    private void renderWindowBackground(MatrixStack matrices) {
        int border = 13;
        int endX = guiLeft + windowWidth - border;
        int endY = guiTop + windowHeight - border;
        RenderSystem.setShaderTexture(0, WINDOW);
        for(int x = guiLeft; x < endX; x += 16) {
            for(int y = guiTop + 16; y < endY; y += 16) {
                this.drawTexture(matrices, x, y, windowWidth, 0, Math.max(16, endX - x), Math.max(16, endY - y));
            }
        }
    }

    @Override
    public boolean mouseScrolled(double x, double y, double z) {
        boolean retValue = super.mouseScrolled(x, y, z);
        int np = this.scrollPos - (int)z * 4;
        this.scrollPos = np < 0 ? 0 : Math.min(np, this.currentMaxScroll);
        return retValue;
    }

    private void renderChoiceContent(MatrixStack matrices) {

        int textWidth = 120;

        int x = guiLeft + 116;
        int y = guiTop + 4;
        int startY = y;
        int endY = y - 72 + windowHeight;
        y -= scrollPos;

        if(description == null) {
            description = getCurrentChoice().getDescription();
        }
        if(name == null) {
            name = new LiteralText("Description").formatted(Formatting.UNDERLINE);
        }

        List<OrderedText> name2 = textRenderer.wrapLines(name, textWidth);
        Text desc = description;
        List<OrderedText> drawLines = textRenderer.wrapLines(desc, textWidth);
        for(OrderedText line : name2) {
            if (y >= startY - 24 && y <= endY + 12) {
                y += 12;
                textRenderer.draw(matrices, line, x, y, 0xFFFFFF);
            }
        }

        for(OrderedText line : drawLines) {
            y += 12;
            if(y >= startY - 24 && y <= endY + 12) {
                textRenderer.draw(matrices, line, x, y, 0xFFFFFF);
            }
        }

        y += scrollPos;
        currentMaxScroll = y - 14 - (guiTop + 158);
        if(currentMaxScroll < 0) {
            currentMaxScroll = 0;
        }
    }



}