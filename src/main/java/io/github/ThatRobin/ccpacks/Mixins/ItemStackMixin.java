package io.github.ThatRobin.ccpacks.Mixins;

import io.github.ThatRobin.ccpacks.DataDrivenClasses.Items.DDItem;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.condition.LootConditionManager;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemStackMixin {

    @Inject(method = "getDefaultStack", at = @At("HEAD"), cancellable = true)
    public void getDefaultStack(CallbackInfoReturnable<ItemStack> cir) {
        DDItem item = ((DDItem)(Object)this);
        ItemStack is = new ItemStack(item);

        cir.setReturnValue(is);
    }

}
