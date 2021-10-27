package io.github.ThatRobin.ccpacks.DataDrivenClasses.Blocks;

import net.minecraft.block.SlabBlock;

public class DDSlabBlock extends SlabBlock {

    private boolean make_block;
    private int fuel_tick;
    public DDSlabBlock(Settings settings, boolean make_block, int fuel_tick) {
        super(settings);
        this.make_block = make_block;
        this.fuel_tick = fuel_tick;
    }

}
