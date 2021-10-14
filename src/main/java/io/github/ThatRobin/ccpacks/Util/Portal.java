package io.github.ThatRobin.ccpacks.Util;

import net.kyrptonaught.customportalapi.portal.PortalIgnitionSource;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;

public class Portal {

    public Block frameBlock;
    public PortalIgnitionSource ignitionSource;
    public Identifier dimID;
    public int r;
    public int g;
    public int b;

    public Portal(Block frameBlock, PortalIgnitionSource ignitionSource, Identifier dimID, int r, int g, int b) {
        this.frameBlock = frameBlock;
        this.ignitionSource = ignitionSource;
        this.dimID= dimID;
        this.r = r;
        this.b = b;
        this.g = g;
    }
}
