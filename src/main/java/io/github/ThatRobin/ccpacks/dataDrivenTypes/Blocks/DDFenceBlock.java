package io.github.ThatRobin.ccpacks.dataDrivenTypes.Blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.Direction;

public class DDFenceBlock extends FenceBlock {

    private boolean make_block;

    public DDFenceBlock(Settings settings, boolean make_block) {
        super(settings);
        this.make_block = make_block;
    }

    @Override
    public boolean canConnect(BlockState state, boolean neighborIsFullSquare, Direction dir) {
        Block block = state.getBlock();
        boolean bl = this.canConnectToFence(state);
        boolean bl2 = block instanceof FenceGateBlock && FenceGateBlock.canWallConnect(state, dir);
        return !cannotConnect(state) && neighborIsFullSquare || bl || bl2;
    }

    private boolean canConnectToFence(BlockState state) {
        return state.isIn(BlockTags.FENCES) && state.isIn(BlockTags.WOODEN_FENCES) == this.getDefaultState().isIn(BlockTags.WOODEN_FENCES) || state.isOf(this);
    }

}
