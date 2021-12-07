package io.github.thatrobin.ccpacks.data_driven_classes.blocks;

import net.minecraft.block.WallBlock;

public class DDWallBlock extends WallBlock {

    private boolean make_block;
    private int fuel_tick;
    public DDWallBlock(Settings settings, boolean make_block, int fuel_tick) {
        super(settings);
        this.make_block = make_block;
        this.fuel_tick = fuel_tick;
    }

}
