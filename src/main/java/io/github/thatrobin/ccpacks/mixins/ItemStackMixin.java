package io.github.thatrobin.ccpacks.mixins;

import com.google.common.collect.Lists;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.emi.trinkets.TrinketSlot;
import dev.emi.trinkets.api.TrinketsApi;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.thatrobin.ccpacks.CCPacksMain;
import io.github.thatrobin.ccpacks.data_driven_classes.items.DDTrinketItem;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import io.github.apace100.apoli.util.StackPowerUtil;
import io.github.thatrobin.ccpacks.power.BindPower;
import io.github.thatrobin.ccpacks.power.ItemUsePower;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.command.argument.BlockArgumentParser;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Mixin(value = ItemStack.class, priority = 999)
public abstract class ItemStackMixin {

    @Shadow
    protected static boolean isSectionVisible(int flags, ItemStack.TooltipSection tooltipSection) {
        return (flags & tooltipSection.getFlag()) == 0;
    }

    @Shadow protected abstract int getHideFlags();

    @Shadow private NbtCompound nbt;

    @Shadow
    protected static Collection<Text> parseBlockTag(String tag) {
        try {
            BlockArgumentParser blockArgumentParser = (new BlockArgumentParser(new StringReader(tag), true)).parse(true);
            BlockState blockState = blockArgumentParser.getBlockState();
            Identifier identifier = blockArgumentParser.getTagId();
            boolean bl = blockState != null;
            boolean bl2 = identifier != null;
            if (bl || bl2) {
                if (bl) {
                    return Lists.newArrayList(new Text[]{blockState.getBlock().getName().formatted(Formatting.DARK_GRAY)});
                }

                Tag<Block> tag2 = BlockTags.getTagGroup().getTag(identifier);
                if (tag2 != null) {
                    Collection<Block> collection = tag2.values();
                    if (!collection.isEmpty()) {
                        return (Collection)collection.stream().map(Block::getName).map((text) -> {
                            return text.formatted(Formatting.DARK_GRAY);
                        }).collect(Collectors.toList());
                    }
                }
            }
        } catch (CommandSyntaxException var8) {
        }

        return Lists.newArrayList(new Text[]{(new LiteralText("missingno")).formatted(Formatting.DARK_GRAY)});
    }

    @Shadow public abstract boolean hasNbt();

    @Shadow public abstract boolean isDamaged();

    @Shadow public abstract int getMaxDamage();

    @Shadow public abstract int getDamage();

    @Shadow public abstract Item getItem();

    @Inject(method = "getTooltip", cancellable = true, at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;hasNbt()Z", ordinal = 1), locals = LocalCapture.CAPTURE_FAILHARD)
    private void addEquipmentPowerTooltips(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir, List<Text> list) {
        ItemStack stack = ((ItemStack) (Object) this);
        if (stack.getItem() instanceof DDTrinketItem trinketItem) {
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
            int integer = this.getHideFlags();
            if (this.hasNbt()) {
                if (isSectionVisible(integer, ItemStack.TooltipSection.UNBREAKABLE) && this.nbt.getBoolean("Unbreakable")) {
                    list.add((new TranslatableText("item.unbreakable")).formatted(Formatting.BLUE));
                }

                NbtList nbtCompound;
                if (isSectionVisible(integer, ItemStack.TooltipSection.CAN_DESTROY) && this.nbt.contains("CanDestroy", 9)) {
                    nbtCompound = this.nbt.getList("CanDestroy", 8);
                    if (!nbtCompound.isEmpty()) {
                        list.add(LiteralText.EMPTY);
                        list.add((new TranslatableText("item.canBreak")).formatted(Formatting.GRAY));

                        for (int nbtList = 0; nbtList < nbtCompound.size(); ++nbtList) {
                            list.addAll(parseBlockTag(nbtCompound.getString(nbtList)));
                        }
                    }
                }

                if (isSectionVisible(integer, ItemStack.TooltipSection.CAN_PLACE) && this.nbt.contains("CanPlaceOn", 9)) {
                    nbtCompound = this.nbt.getList("CanPlaceOn", 8);
                    if (!nbtCompound.isEmpty()) {
                        list.add(LiteralText.EMPTY);
                        list.add((new TranslatableText("item.canPlace")).formatted(Formatting.GRAY));

                        for (int nbtList = 0; nbtList < nbtCompound.size(); ++nbtList) {
                            list.addAll(parseBlockTag(nbtCompound.getString(nbtList)));
                        }
                    }
                }
            }

            if (context.isAdvanced()) {
                if (this.isDamaged()) {
                    list.add(new TranslatableText("item.durability", new Object[]{this.getMaxDamage() - this.getDamage(), this.getMaxDamage()}));
                }

                list.add((new LiteralText(Registry.ITEM.getId(this.getItem()).toString())).formatted(Formatting.DARK_GRAY));
                if (this.hasNbt()) {
                    list.add((new TranslatableText("item.nbt_tags", new Object[]{this.nbt.getKeys().size()})).formatted(Formatting.DARK_GRAY));
                }
            }
            cir.setReturnValue(list);
        }
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
            PowerHolderComponent.getPowers(user, BindPower.class).forEach(bindPower -> {
                if(bindPower.doesApply(stackInHand)) {
                    if (bindPower.checkSlot((user.getInventory().getSlotWithStack(stackInHand)))) {
                        if(bindPower.doesPrevent(stackInHand)) {
                            info.setReturnValue(TypedActionResult.fail(stackInHand));
                        }
                    }
                }
            });
        }
    }
}
