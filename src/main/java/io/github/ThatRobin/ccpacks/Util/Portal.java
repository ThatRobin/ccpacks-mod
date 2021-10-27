package io.github.ThatRobin.ccpacks.Util;

import net.kyrptonaught.customportalapi.portal.PortalIgnitionSource;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;

public class Portal {

    public Block frameBlock;
    public PortalIgnitionSource ignitionSource;
    public Identifier dimID;
    public ColourHolder colourHolder;

    public Portal(Block frameBlock, PortalIgnitionSource ignitionSource, Identifier dimID, ColourHolder colourHolder) {
        this.frameBlock = frameBlock;
        this.ignitionSource = ignitionSource;
        this.dimID= dimID;
        this.colourHolder = colourHolder;
    }
}
