package io.github.ThatRobin.ccpacks.dataDrivenTypes.Blocks;

import net.minecraft.block.WallBlock;

public class DDWallBlock extends WallBlock {

    private boolean make_block;

    public DDWallBlock(Settings settings, boolean make_block) {
        super(settings);
        this.make_block = make_block;
    }
}
