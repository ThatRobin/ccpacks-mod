package io.github.ThatRobin.ccpacks.dataDrivenTypes.Blocks;

import net.minecraft.block.SlabBlock;

public class DDSlabBlock extends SlabBlock {

    private boolean make_block;

    public DDSlabBlock(Settings settings, boolean make_block) {
        super(settings);
        this.make_block = make_block;
    }
}
