package io.github.thatrobin.ccpacks.mixins;

import io.github.thatrobin.ccpacks.util.PowerGrantingTrinket;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.util.PowerGrantingItem;
import io.github.apace100.apoli.util.StackPowerUtil;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.LinkedList;
import java.util.List;

@Mixin(StackPowerUtil.class)
public class StackPowerUtilMixin {

    @Inject(method = "getPowers", at = @At(value = "HEAD"), cancellable = true)
    private static void getPowers(ItemStack stack, EquipmentSlot slot, CallbackInfoReturnable<List<StackPowerUtil.StackPower>> cir) {
        NbtCompound nbt = stack.getNbt();
        NbtList list;
        List<StackPowerUtil.StackPower> powers = new LinkedList<>();
        if(stack.getItem() instanceof PowerGrantingItem pgi) {
            powers.addAll(pgi.getPowers(stack, slot));
        }
        if(stack.getItem() instanceof PowerGrantingTrinket pgi) {
            powers.addAll(pgi.getTrinketPowers(stack));
        }
        if(nbt != null && nbt.contains("Powers")) {
            NbtElement elem = nbt.get("Powers");
            if(elem.getType() != NbtType.LIST) {
                cir.setReturnValue(List.of());
            }
            list = (NbtList)elem;
            list.stream().map(p -> {
                if(p.getType() == NbtType.COMPOUND) {
                    return StackPowerUtil.StackPower.fromNbt((NbtCompound)p);
                } else {
                    Apoli.LOGGER.warn("Invalid power format on stack nbt, stack = " + stack + ", nbt = " + p);
                }
                return null;
            }).filter(sp -> sp != null && sp.slot == slot).forEach(powers::add);
        }
        cir.setReturnValue(powers);
        cir.cancel();
    }
}
