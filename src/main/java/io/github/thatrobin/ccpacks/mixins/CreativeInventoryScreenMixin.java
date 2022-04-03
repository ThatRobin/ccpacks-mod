package io.github.thatrobin.ccpacks.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.thatrobin.ccpacks.item_groups.CreativeInventoryScreenExtensions;
import io.github.thatrobin.ccpacks.item_groups.ItemGroupButtonWidget;
import io.github.thatrobin.ccpacks.item_groups.TabbedItemGroup;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemGroup;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.commons.compress.utils.Lists;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(CreativeInventoryScreen.class)
public abstract class CreativeInventoryScreenMixin extends AbstractInventoryScreen<CreativeInventoryScreen.CreativeScreenHandler> implements CreativeInventoryScreenExtensions {

    @Shadow
    private static int selectedTab;

    @Unique
    private final List<ItemGroupButtonWidget> owo$buttons = Lists.newArrayList();

    @Shadow
    @Final
    private static Identifier TEXTURE;

    @Unique
    private TabbedItemGroup owo$owoGroup = null;

    @ModifyArg(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/CreativeInventoryScreen;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 1), index = 3)
    private int injectCustomScrollbarTextureU(int original) {
        if (owo$owoGroup == null) return original;
        return original - 232;
    }

    @ModifyArg(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/CreativeInventoryScreen;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 1), index = 4)
    private int injectCustomScrollbarTextureV(int original) {
        if (owo$owoGroup == null) return original;
        return 136;
    }

    @Inject(method = "drawBackground", at = @At("RETURN"))
    private void releaseGroupInstance(MatrixStack matrices, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        this.owo$owoGroup = null;
    }

    @ModifyArg(method = "renderTabIcon", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/CreativeInventoryScreen;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"), index = 3)
    private int injectCustomTabTextureLocation(int original) {
        if (owo$owoGroup == null) return original;
        return owo$owoGroup.getColumn() == 0 ? 195 : 223;
    }

    @Inject(method = "renderTabIcon", at = @At("RETURN"))
    private void restoreTabTexture(MatrixStack matrices, ItemGroup group, CallbackInfo ci) {
        if (owo$owoGroup == null) return;
        this.owo$owoGroup = null;
        RenderSystem.setShaderTexture(0, TEXTURE);
    }

    // -------------
    // oωo tab title
    // -------------

    @ModifyArg(method = "drawForeground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I"))
    private Text injectTabNameAsTitle(Text original) {
        if (!(ItemGroup.GROUPS[selectedTab] instanceof TabbedItemGroup owoGroup) || !owoGroup.shouldDisplayTabNamesAsTitle()) return original;
        return owo$buttons.get(owoGroup.getSelectedTabIndex()).getMessage();
    }

    // ---------------
    // oωo tab buttons
    // ---------------

    @Inject(at = @At("HEAD"), method = "setSelectedTab(Lnet/minecraft/item/ItemGroup;)V")
    private void setSelectedTab(ItemGroup group, CallbackInfo ci) {
        owo$buttons.forEach(this::remove);
        owo$buttons.clear();

        if (group instanceof TabbedItemGroup owoGroup) {

            int tabRootY = this.y;

            final int stackHeight = owoGroup.getStackHeight();
            if (stackHeight > 4) tabRootY -= 13 * (stackHeight - 4);

            if (owoGroup.shouldDisplaySingleTab() || owoGroup.tabs.size() > 1) {
                for (int i = 0; i < owoGroup.tabs.size(); i++) {
                    var tab = owoGroup.tabs.get(i);

                    int xOffset = this.x - 27 - (i / stackHeight) * 26;
                    int yOffset = tabRootY + 10 + (i % stackHeight) * 30;

                    var tabButton = new ItemGroupButtonWidget(xOffset, yOffset, false, tab, group.getName(), owo$createSelectAction(this, owoGroup, i));

                    if (i == owoGroup.getSelectedTabIndex()) tabButton.isSelected = true;

                    owo$buttons.add(tabButton);
                    this.addDrawableChild(tabButton);
                }
            }
        }
    }

    @Inject(at = @At("TAIL"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V")
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta, CallbackInfo cbi) {
        owo$buttons.forEach(button -> {
            if (button.isHovered()) renderTooltip(matrixStack, button.getMessage(), mouseX, mouseY);
        });
    }

    @Override
    public int owo$getRootX() {
        return this.x;
    }

    @Override
    public int owo$getRootY() {
        return this.y;
    }

    @Unique
    private static ButtonWidget.PressAction owo$createSelectAction(Screen targetScreen, TabbedItemGroup group, int targetTabIndex) {
        return button -> {
            group.setSelectedTab(targetTabIndex);
            MinecraftClient.getInstance().setScreen(targetScreen);
            ((ItemGroupButtonWidget) button).isSelected = true;
        };
    }

    public CreativeInventoryScreenMixin(CreativeInventoryScreen.CreativeScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }
}