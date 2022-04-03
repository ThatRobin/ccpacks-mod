package io.github.thatrobin.ccpacks.item_groups;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.impl.item.group.ItemGroupExtensions;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Objects;

public abstract class TabbedItemGroup extends ItemGroup {

    public final List<ItemGroupTab> tabs = Lists.newArrayList();

    private int selectedTab = 0;
    private boolean initialized = false;

    private int stackHeight = 4;
    private Identifier customTexture = null;
    private boolean displayTabNamesAsTitle = true;
    private boolean displaySingleTab = false;

    public TabbedItemGroup(Identifier id) {
        super(createTabIndex(), String.format("%s.%s", id.getNamespace(), id.getPath()));
    }

    public void initialize() {
        if (initialized) return;

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) setup();
        if (tabs.size() == 0) this.addTab(Icon.of(Items.AIR), "based_placeholder_tab", null);
        this.initialized = true;
    }


    protected void addTab(Icon icon, String name, TagKey<Item> contentTag) {
        this.tabs.add(new ItemGroupTab(icon, name, contentTag, ItemGroupTab.DEFAULT_TEXTURE));
    }

    @Override
    public void appendStacks(DefaultedList<ItemStack> stacks) {
        if (!initialized) throw new IllegalStateException("Owo item group not initialized, was 'initialize()' called?");
        Registry.ITEM.stream().filter(this::includes).forEach(item -> stacks.add(new ItemStack(item)));
    }

    protected boolean includes(Item item) {
        if (tabs.size() > 1)
            return getSelectedTab().includes(item) || (item.getGroup() == this && ((ItemExtensions) item).getTab() == this.getSelectedTabIndex());
        else
            return item.getGroup() != null && Objects.equals(item.getGroup().getName(), this.getName());
    }

    public int getSelectedTabIndex() {
        return selectedTab;
    }

    public void setSelectedTab(int selectedTab) {
        this.selectedTab = selectedTab;
    }

    public ItemGroupTab getSelectedTab() {
        return tabs.get(selectedTab);
    }

    public boolean shouldDisplayTabNamesAsTitle() {
        return displayTabNamesAsTitle && this.tabs.size() > 1;
    }

    public int getStackHeight() {
        return stackHeight;
    }

    public boolean shouldDisplaySingleTab() {
        return displaySingleTab;
    }

    protected void displaySingleTab() {
        this.displaySingleTab = true;
    }

    protected abstract void setup();

    private static int createTabIndex() {
        ((ItemGroupExtensions) ItemGroup.BUILDING_BLOCKS).fabric_expandArray();
        return ItemGroup.GROUPS.length - 1;
    }

    @Override
    public ItemStack createIcon() {
        return null;
    }

    public interface ButtonDefinition {
        Icon icon();

        Identifier texture();

        String getTranslationKey(String groupKey);
    }
}
