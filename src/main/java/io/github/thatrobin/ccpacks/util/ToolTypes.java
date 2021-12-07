package io.github.thatrobin.ccpacks.util;

import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;

public enum ToolTypes {
    PICKAXES("pickaxes", FabricToolTags.PICKAXES),
    AXES("axes", FabricToolTags.AXES),
    SHOVELS("shovels", FabricToolTags.SHOVELS),
    HOES("hoes", FabricToolTags.HOES),
    SHEARS("shears", FabricToolTags.SHEARS),
    SWORDS("swords", FabricToolTags.SWORDS);

    public String name;
    public Tag<Item> tool;

    ToolTypes(String name, Tag<Item> tool) {
        this.name=name;
        this.tool=tool;
    }
}
