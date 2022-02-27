package io.github.thatrobin.ccpacks.mixins;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.thatrobin.ccpacks.power.BindPower;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resource.ResourcePackProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Set;

@Mixin(KeyBinding.class)
public class KeyBindingMixin {

    @Shadow @Final private static Map<String, KeyBinding> KEYS_BY_ID;

    @Inject(method = "wasPressed", at= @At("HEAD"), cancellable = true)
    public void test(CallbackInfoReturnable<Boolean> cir) {
        KeyBinding swapKey = KEYS_BY_ID.get("key.swapOffhand");
        KeyBinding dropKey = KEYS_BY_ID.get("key.drop");
        if((KeyBinding)(Object) this == swapKey) {
            PlayerEntity player = MinecraftClient.getInstance().player;
            PowerHolderComponent.getPowers(player, BindPower.class).forEach(preventItemSelectionPower -> {
                if(preventItemSelectionPower.doesApply(player.getMainHandStack()) || preventItemSelectionPower.doesApply(player.getOffHandStack())) {
                    if (preventItemSelectionPower.checkSlot(player.getInventory().getSlotWithStack(player.getMainHandStack())) || preventItemSelectionPower.checkSlot(player.getInventory().getSlotWithStack(player.getOffHandStack()))) {
                        cir.setReturnValue(false);
                    }
                }
            });
        } else if ((KeyBinding)(Object) this == dropKey) {
            PlayerEntity player = MinecraftClient.getInstance().player;
            PowerHolderComponent.getPowers(player, BindPower.class).forEach(preventItemSelectionPower -> {
                if(preventItemSelectionPower.doesApply(player.getMainHandStack())) {
                    if (preventItemSelectionPower.checkSlot(player.getInventory().getSlotWithStack(player.getMainHandStack()))) {
                        cir.setReturnValue(false);
                    }
                }
            });
        }
    }
}
