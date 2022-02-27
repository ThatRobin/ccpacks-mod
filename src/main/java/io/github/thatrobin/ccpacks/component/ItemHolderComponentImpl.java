package io.github.thatrobin.ccpacks.component;

import io.github.apace100.apoli.Apoli;
import io.github.thatrobin.ccpacks.CCPacksMain;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ItemHolderComponentImpl implements ItemHolderComponent {

    private final LivingEntity owner;
    private final ConcurrentHashMap<Identifier, ItemStack> items = new ConcurrentHashMap<>();

    public ItemHolderComponentImpl(LivingEntity owner) {
        this.owner = owner;
    }

    @Override
    public void removeItem(Identifier id) {
        items.remove(id);
    }

    @Override
    public int removeAllItems() {
        return 0;
    }

    @Override
    public List<ItemStack> getPowersFromSource() {
        return null;
    }

    @Override
    public boolean addItem(ItemStack stack, Identifier id) {
        items.put(id,stack);
        return true;
    }

    @Override
    public boolean hasItem(ItemStack stack) {
        return items.contains(stack);
    }

    @Override
    public boolean hasId(Identifier identifier) {
        return items.containsKey(identifier);
    }

    @Override
    public ItemStack getItem(Identifier id) {
        if(items.containsKey(id)) {
            return items.get(id);
        }
        return null;
    }

    @Override
    public List<ItemStack> getItems() {
        return items.values().stream().toList();
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound compoundTag) {
        this.fromTag(compoundTag);
    }

    private void fromTag(NbtCompound compoundTag) {
        ConcurrentHashMap<Identifier, ItemStack> tempItems = new ConcurrentHashMap<>();
        try {
            if (owner == null) {
                CCPacksMain.LOGGER.error("Owner was null in ItemHolderComponent#fromTag!");
            }
            NbtList powerList = (NbtList) compoundTag.get("Items");
            if(powerList != null) {
                for (int i = 0; i < powerList.size(); i++) {
                    NbtCompound powerTag = powerList.getCompound(i);
                    Identifier id = Identifier.tryParse(powerTag.getString("identifier"));
                    tempItems.put(id, ItemStack.fromNbt(powerTag));
                }
            }
            for(Map.Entry<Identifier, ItemStack> entry: tempItems.entrySet()) {
                addItem(entry.getValue(), entry.getKey());
            }
        } catch(Exception e) {
            CCPacksMain.LOGGER.info("Error while reading data: " + e.getMessage());
        }
    }

    @Override
    public void writeToNbt(NbtCompound compoundTag) {
        NbtList powerList = new NbtList();
        items.forEach((identifier, itemStack) -> {
            NbtCompound powerTag = new NbtCompound();
            itemStack.writeNbt(powerTag);
            powerTag.put("identifier", NbtString.of(identifier.toString()));
            powerList.add(powerTag);
        });
        compoundTag.put("Items", powerList);
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        NbtCompound compoundTag = buf.readNbt();
        if(compoundTag != null) {
            this.fromTag(compoundTag);
        }
    }

    @Override
    public void sync() {
        ItemHolderComponent.sync(this.owner);
    }

    @Override
    public void serverTick() {

    }
}
