package io.github.connor3001.ccpacks.Util;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public class BlockNamePos {

    private Block block;
    private BlockPos pos;

    public BlockNamePos(BlockPos position, Block block){
        this.block = block;
        this.pos = position;
    }
}
