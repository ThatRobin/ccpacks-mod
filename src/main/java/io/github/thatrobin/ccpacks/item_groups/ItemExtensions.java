package io.github.thatrobin.ccpacks.item_groups;

import net.minecraft.item.ItemGroup;

public interface ItemExtensions {

    /**
     * @return The 0-indexed tab id this item resides in, {@code -1} if none is defined
     */
    int getTab();

}