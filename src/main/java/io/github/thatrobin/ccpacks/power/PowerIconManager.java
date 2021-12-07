package io.github.thatrobin.ccpacks.power;

import io.github.apace100.apoli.integration.PowerReloadCallback;
import io.github.apace100.apoli.power.PowerTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;

public class PowerIconManager {

    private final HashMap<Identifier, ItemStack> icons = new HashMap<>();

    public PowerIconManager() {
        PowerReloadCallback.EVENT.register(this::clear);
        PowerTypes.registerAdditionalData("icon", (powerId, factoryId, isSubPower, data, powerType) -> {
            addItem(powerId, Registry.ITEM.get(Identifier.tryParse(data.getAsString())).getDefaultStack());
        });
    }

    public void clear() {
        icons.clear();
    }

    public ItemStack getIcon(Identifier powerId) {
        if(!icons.containsKey(powerId)) {
            return Items.GRASS_BLOCK.getDefaultStack();
        }
        return icons.get(powerId);
    }

    public void addItem(Identifier powerId, ItemStack item) {
        icons.put(powerId,item);
    }
}
