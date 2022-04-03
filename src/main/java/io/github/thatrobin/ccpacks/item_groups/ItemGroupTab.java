package io.github.thatrobin.ccpacks.item_groups;

import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.Set;

public record ItemGroupTab(Icon icon, String name, TagKey<Item> contentTag, Identifier texture) implements TabbedItemGroup.ButtonDefinition {

    public static final Identifier DEFAULT_TEXTURE = new Identifier("ccpacks", "textures/gui/tabs.png");

    public boolean includes(Item item) {
        return (contentTag != null && item.getRegistryEntry().isIn(contentTag));
    }

    public String getTranslationKey(String groupKey) {
        return "itemGroup." + groupKey + ".tab." + name;
    }
}