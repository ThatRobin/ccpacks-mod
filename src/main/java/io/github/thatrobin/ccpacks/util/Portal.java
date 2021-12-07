package io.github.thatrobin.ccpacks.util;

import net.minecraft.block.Block;
import net.minecraft.util.Identifier;

public class Portal {

    public Block frameBlock;
    public Identifier ignitionSource;
    public Identifier dimID;
    public ColourHolder colourHolder;

    public Portal(Block frameBlock, Identifier ignitionSource, Identifier dimID, ColourHolder colourHolder) {
        this.frameBlock = frameBlock;
        this.ignitionSource = ignitionSource;
        this.dimID= dimID;
        this.colourHolder = colourHolder;
    }
}
