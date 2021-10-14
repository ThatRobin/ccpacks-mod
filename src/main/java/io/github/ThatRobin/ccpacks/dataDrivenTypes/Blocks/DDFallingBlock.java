package io.github.ThatRobin.ccpacks.dataDrivenTypes.Blocks;

import net.minecraft.block.FallingBlock;

public class DDFallingBlock extends FallingBlock {

    private final boolean make_block;

    public DDFallingBlock(Settings settings, boolean make_block) {
        super(settings);
        this.make_block = make_block;
    }
}
