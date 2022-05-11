package io.github.thatrobin.ccpacksapoli.compat;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.concurrent.atomic.AtomicBoolean;

public class TrinketsCompat {
    public static boolean trinketCheck(PlayerEntity player, ConditionFactory<ItemStack>.Instance condition) {
        AtomicBoolean found = new AtomicBoolean(false);
        TrinketComponent component = TrinketsApi.getTrinketComponent(player).get();
        component.forEach((slotReference, itemStack) -> {
            if (condition.test(itemStack)) {
                found.set(true);
            }
        });
        return found.get();
    }

}
