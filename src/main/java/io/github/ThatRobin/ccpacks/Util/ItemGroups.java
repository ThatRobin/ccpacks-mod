package io.github.ThatRobin.ccpacks.Util;

import net.minecraft.item.ItemGroup;

public enum ItemGroups {
    BUILDING_BLOCKS(ItemGroup.BUILDING_BLOCKS),
    BREWING(ItemGroup.BREWING),
    TRANSPORTATION(ItemGroup.TRANSPORTATION),
    COMBAT(ItemGroup.COMBAT),
    DECORATIONS(ItemGroup.DECORATIONS),
    FOOD(ItemGroup.FOOD),
    MATERIALS(ItemGroup.MATERIALS),
    REDSTONE(ItemGroup.REDSTONE),
    TOOLS(ItemGroup.TOOLS),
    MISC(ItemGroup.MISC);

    public ItemGroup group;

    ItemGroups(ItemGroup group) {
        this.group=group;
    }
}
