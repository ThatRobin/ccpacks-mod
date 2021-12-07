package io.github.thatrobin.ccpacks.data_driven_classes.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;

public class DDStairBlock extends StairsBlock {

    private boolean make_block;
    private int fuel_tick;

    public DDStairBlock(BlockState baseBlockState, Settings settings, boolean make_block, int fuel_tick) {
        super(baseBlockState, settings);
        this.make_block = make_block;
        this.fuel_tick = fuel_tick;
    }

}
