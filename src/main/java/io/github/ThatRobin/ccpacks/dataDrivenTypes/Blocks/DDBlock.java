package io.github.ThatRobin.ccpacks.dataDrivenTypes.Blocks;

import net.minecraft.block.Block;

public class DDBlock extends Block {

    private boolean make_block;

    public DDBlock(Settings settings, boolean make_block) {
        super(settings);
        this.make_block = make_block;
    }

}
