package io.github.ThatRobin.ccpacks.DataDrivenClasses.Blocks;

import net.minecraft.block.Block;

public class DDBlock extends Block {

    private boolean make_block;
    private int fuel_tick;
    public DDBlock(Settings settings, boolean make_block, int fuel_tick) {
        super(settings);
        this.make_block = make_block;
        this.fuel_tick = fuel_tick;
    }

}
