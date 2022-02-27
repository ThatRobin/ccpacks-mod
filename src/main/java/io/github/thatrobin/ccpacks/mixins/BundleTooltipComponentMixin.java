package io.github.thatrobin.ccpacks.mixins;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.tooltip.BundleTooltipComponent;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BundleTooltipComponent.class)
public abstract class BundleTooltipComponentMixin {

    @Shadow @Final private DefaultedList<ItemStack> inventory;

    @Shadow protected abstract int getColumns();

    @Shadow protected abstract int getRows();

    @Shadow @Final private int occupancy;

    @Shadow protected abstract void drawSlot(int x, int y, int index, boolean shouldBlock, TextRenderer textRenderer, MatrixStack matrices, ItemRenderer itemRenderer, int z);

    @Shadow protected abstract void drawOutline(int x, int y, int columns, int rows, MatrixStack matrices, int z);

    @Inject(method = "getColumns", at = @At("RETURN"), cancellable = true)
    public void getColumns(CallbackInfoReturnable<Integer> cir) {
        if(this.inventory.size() == 1) {
            cir.setReturnValue((int) Math.ceil(Math.sqrt((double) this.inventory.size())));
        }
    }

    @Inject(method = "getRows", at = @At("RETURN"), cancellable = true)
    public void getRows(CallbackInfoReturnable<Integer> cir) {
        if(this.inventory.size() == 1) {
            cir.setReturnValue((int) Math.ceil(((double) this.inventory.size() / (double) this.getColumns())));
        }
    }


    @Inject(method = "drawItems", at = @At("HEAD"), cancellable = true)
    private void getHeight(TextRenderer textRenderer, int x, int y, MatrixStack matrices, ItemRenderer itemRenderer, int z, CallbackInfo ci) {
        int i = getColumns();
        int j = getRows();
        boolean bl = this.occupancy >= 64;
        int k = 0;

        for(int l = 0; l < j; ++l) {
            for(int m = 0; m < i; ++m) {
                int n = x + m * 18 + 1;
                int o = y + l * 20 + 1;
                this.drawSlot(n, o, k++, bl, textRenderer, matrices, itemRenderer, z);
            }
        }

        this.drawOutline(x, y, i, j, matrices, z);
    }

    //@Inject(method = "getWidth", at = @At("HEAD"), cancellable = true)
    //private void getWidth(CallbackInfoReturnable<Integer> cir) {
    //    if(this.inventory.size() == 1) {
    //        cir.setReturnValue(1);
    //    }
    //}

}
