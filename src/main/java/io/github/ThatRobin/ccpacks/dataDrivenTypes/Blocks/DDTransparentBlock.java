package io.github.ThatRobin.ccpacks.dataDrivenTypes.Blocks;

import net.minecraft.block.GlassBlock;

public class DDTransparentBlock extends GlassBlock {

    private boolean make_block;

    public DDTransparentBlock(Settings settings, boolean make_block) {
        super(settings);
        this.make_block = make_block;
    }
}
