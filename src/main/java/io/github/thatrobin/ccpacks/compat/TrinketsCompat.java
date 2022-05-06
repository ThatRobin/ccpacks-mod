package io.github.thatrobin.ccpacks.compat;

import dev.emi.trinkets.TrinketSlot;
import dev.emi.trinkets.api.TrinketsApi;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import io.github.apace100.apoli.util.StackPowerUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.thatrobin.ccpacks.data_driven_classes.items.DDTrinketItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.function.Supplier;

public class TrinketsCompat {

    public static void trinketCheck(PlayerEntity player, TooltipContext context, List<Text> list, ItemStack stack, DDTrinketItem trinketItem) {
        TrinketsApi.getTrinketComponent(player).ifPresent(trinkets -> {
            trinkets.forEach(((slotReference, itemStack) -> {
                if (TrinketSlot.canInsert(stack, slotReference, player)) {
                    List<StackPowerUtil.StackPower> powers = trinketItem.getTrinketPowers(stack)
                            .stream()
                            .filter(sp -> !sp.isHidden)
                            .toList();
                    if (powers.size() > 0) {
                        list.add(LiteralText.EMPTY);
                        list.add((new TranslatableText("item.modifiers." + slotReference.inventory().getSlotType().getName())).formatted(Formatting.GRAY));
                        powers.forEach(sp -> {
                            if (PowerTypeRegistry.contains(sp.powerId)) {
                                PowerType<?> powerType = PowerTypeRegistry.get(sp.powerId);
                                list.add(
                                        new LiteralText(" ")
                                                .append(powerType.getName())
                                                .formatted(sp.isNegative ? Formatting.RED : Formatting.BLUE));
                                if (context.isAdvanced()) {
                                    list.add(
                                            new LiteralText("  ")
                                                    .append(powerType.getDescription())
                                                    .formatted(Formatting.GRAY));
                                }
                            }
                        });
                    }
                }
            }));
        });
    }

    public static Supplier<Item> createTrinketItem(FabricItemSettings settings, SerializableData.Instance data) {
        return () -> new DDTrinketItem(settings.maxDamage(data.getInt("durability")), data.get("name"), data.get("lore"), data.get("start_colour"), data.get("end_colour"), data.get("item_powers"));
    }
}
