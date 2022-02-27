package io.github.thatrobin.ccpacks.component;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import io.github.thatrobin.ccpacks.CCPacksMain;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.List;

public interface ItemHolderComponent extends AutoSyncedComponent, ServerTickingComponent {
    ComponentKey<ItemHolderComponent> KEY = ComponentRegistry.getOrCreate(CCPacksMain.identifier("saved_items"), ItemHolderComponent.class);

    void removeItem(Identifier id);

    int removeAllItems();

    List<ItemStack> getPowersFromSource();

    boolean addItem(ItemStack stack, Identifier id);

    boolean hasItem(ItemStack stack);

    boolean hasId(Identifier identifier);

    ItemStack getItem(Identifier id);

    List<ItemStack> getItems();

    void sync();

    static void sync(Entity entity) {
        KEY.sync(entity);
    }

}
