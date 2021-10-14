package io.github.ThatRobin.ccpacks.dataDrivenTypes.Blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;

public class DDStairBlock extends StairsBlock {

    private boolean make_block;

    public DDStairBlock(BlockState baseBlockState, Settings settings, boolean make_block) {
        super(baseBlockState, settings);
        this.make_block = make_block;
    }
}
