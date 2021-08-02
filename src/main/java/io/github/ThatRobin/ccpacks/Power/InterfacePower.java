package io.github.ThatRobin.ccpacks.Power;

import io.github.ThatRobin.ccpacks.Screen.ContainerScreenHandler;
import io.github.ThatRobin.ccpacks.util.CustomAnvil;
import io.github.ThatRobin.ccpacks.util.CustomCraftingTable;
import io.github.apace100.apoli.power.Active;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.screen.*;
import net.minecraft.tag.Tag;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

import java.util.function.Predicate;

public class InterfacePower extends Power implements Inventory {

    private final int size;
    private final int rows;
    private final DefaultedList<ItemStack> inventory;
    public final TranslatableText containerName;
    private final String interfaceType;
    public final ScreenHandlerFactory factory;
    private final boolean shouldDropOnDeath;
    private final Predicate<ItemStack> dropOnDeathFilter;

    public InterfacePower(PowerType<?> type, LivingEntity entity, String containerName, String interfaceType, int size, int rows, boolean shouldDropOnDeath, Predicate<ItemStack> dropOnDeathFilter) {
        super(type, entity);
        size = size * rows;
        this.size = size;
        this.rows = rows;
        this.interfaceType = interfaceType;
        this.inventory = DefaultedList.ofSize(size, ItemStack.EMPTY);
        this.containerName = new TranslatableText(containerName);
        if(!entity.world.isClient && entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            if (this.interfaceType.equals("minecraft:chest")) {
                switch (rows) {
                    case 2:
                        this.factory = (i, playerInventory, playerEntity) -> {
                            return new ContainerScreenHandler(ScreenHandlerType.GENERIC_9X2, i, playerInventory, this, rows);
                        };
                        break;
                    case 3:
                        this.factory = (i, playerInventory, playerEntity) -> {
                            return new ContainerScreenHandler(ScreenHandlerType.GENERIC_9X3, i, playerInventory, this, rows);
                        };
                        break;
                    case 4:
                        this.factory = (i, playerInventory, playerEntity) -> {
                            return new ContainerScreenHandler(ScreenHandlerType.GENERIC_9X4, i, playerInventory, this, rows);
                        };
                        break;
                    case 5:
                        this.factory = (i, playerInventory, playerEntity) -> {
                            return new ContainerScreenHandler(ScreenHandlerType.GENERIC_9X5, i, playerInventory, this, rows);
                        };
                        break;
                    case 6:
                        this.factory = (i, playerInventory, playerEntity) -> {
                            return new ContainerScreenHandler(ScreenHandlerType.GENERIC_9X6, i, playerInventory, this, rows);
                        };
                        break;
                    default:
                        this.factory = (i, playerInventory, playerEntity) -> {
                            return new ContainerScreenHandler(ScreenHandlerType.GENERIC_9X1, i, playerInventory, this, rows);
                        };
                        break;
                }
            } else if (this.interfaceType.equals("minecraft:crafting_table")) {
                this.factory = (i, playerInventory, playerEntity) -> {
                    return new CustomCraftingTable(1, player.getInventory(),
                            ScreenHandlerContext.create(player.world, new BlockPos(0, 0, 0)));
                };
            } else if (this.interfaceType.equals("minecraft:hopper")) {
                this.factory = (i, playerInventory, playerEntity) -> {
                    return new HopperScreenHandler(i, playerInventory, this);
                };
            } else if (this.interfaceType.equals("minecraft:ender_chest")) {
                this.factory = (i, playerInventory, playerEntity) -> {
                    return GenericContainerScreenHandler.createGeneric9x3(i, playerInventory, player.getEnderChestInventory());
                };
            } else if (this.interfaceType.equals("minecraft:anvil")) {
                this.factory = (i, playerInventory, playerEntity) -> {
                    return new CustomAnvil(i, playerInventory, ScreenHandlerContext.create(player.world, player.getBlockPos()));
                };
            } else if (this.interfaceType.equals("minecraft:dropper")) {
                this.factory = (i, playerInventory, playerEntity) -> {
                    return new Generic3x3ContainerScreenHandler(i, playerInventory, this);
                };
            } else {
                this.factory = (i, playerInventory, playerEntity) -> {
                    return new Generic3x3ContainerScreenHandler(i, playerInventory, this);
                };
            }
        } else {
            this.factory = null;
        }
        this.shouldDropOnDeath = shouldDropOnDeath;
        this.dropOnDeathFilter = dropOnDeathFilter;
    }

    @Override
    public NbtCompound toTag() {
        NbtCompound tag = new NbtCompound();
        Inventories.writeNbt(tag, inventory);
        return tag;
    }

    @Override
    public void fromTag(NbtElement tag) {
        Inventories.readNbt((NbtCompound)tag, inventory);
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        return inventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return inventory.get(slot).split(amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack stack = inventory.get(slot);
        setStack(slot, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        inventory.set(slot, stack);
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean canPlayerUse(PlayerEntity entity) {
        return entity == this.entity;
    }

    @Override
    public void clear() {
        for(int i = 0; i < size; i++) {
            setStack(i, ItemStack.EMPTY);
        }
    }

    public boolean shouldDropOnDeath() {
        return shouldDropOnDeath;
    }

    public boolean shouldDropOnDeath(ItemStack stack) {
        return shouldDropOnDeath && dropOnDeathFilter.test(stack);
    }

    public boolean canUse() {
        return isActive();
    }


}
