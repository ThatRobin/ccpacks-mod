package io.github.ThatRobin.ccpacks.Util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;

public class CustomAnvil extends AnvilScreenHandler {

    public CustomAnvil(int int1, PlayerInventory var1, ScreenHandlerContext blockContext) {
        super(int1, var1, blockContext);
    }

    @Override
    public boolean canUse(PlayerEntity var1) {
        return true;
    }


}