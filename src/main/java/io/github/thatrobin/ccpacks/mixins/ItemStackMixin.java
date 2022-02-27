package io.github.thatrobin.ccpacks.mixins;

import dev.emi.trinkets.TrinketSlot;
import dev.emi.trinkets.api.TrinketsApi;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.thatrobin.ccpacks.data_driven_classes.items.DDTrinketItem;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import io.github.apace100.apoli.util.StackPowerUtil;
import io.github.thatrobin.ccpacks.power.BindPower;
import io.github.thatrobin.ccpacks.power.ItemUsePower;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(method = "getTooltip", cancellable = true, at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;hasNbt()Z", ordinal = 1), locals = LocalCapture.CAPTURE_FAILHARD)
    private void addEquipmentPowerTooltips(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir, List<Text> list) {
        ItemStack stack = ((ItemStack) (Object) this);
        TrinketsApi.getTrinketComponent(player).ifPresent(trinkets -> {
            trinkets.forEach(((slotReference, itemStack) -> {
                if (stack.getItem() instanceof DDTrinketItem trinketItem) {
                    TrinketSlot.canInsert(stack, slotReference, player);
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

    @Inject(method = "use", at = @At("HEAD"))
    private void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        PowerHolderComponent.getPowers(user, ItemUsePower.class).forEach(power -> {
            if (power.doesApply((ItemStack) (Object) this)) {
                power.executeActions((ItemStack) (Object) this);
            }
        });
    }

    @Inject(at = @At("HEAD"), method = "use", cancellable = true)
    public void preventUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> info) {
        if(user != null) {
            ItemStack stackInHand = user.getStackInHand(hand);
            PowerHolderComponent.getPowers(user, BindPower.class).forEach(preventItemSelectionPower -> {
                if(preventItemSelectionPower.doesApply(stackInHand)) {
                    if (preventItemSelectionPower.checkSlot((user.getInventory().getSlotWithStack(stackInHand)))) {
                        if(preventItemSelectionPower.doesPrevent(stackInHand)) {
                            info.setReturnValue(TypedActionResult.fail(stackInHand));
                        }
                    }
                }
            });
        }
    }
}
