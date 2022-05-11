package io.github.thatrobin.ccpacks.mixins;

import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.thatrobin.ccpacks.compat.TrinketsCompat;
import io.github.thatrobin.ccpacks.data_driven_classes.items.DDTrinketItem;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.command.argument.BlockArgumentParser;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.tag.TagKey;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
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
            TagKey<Block> tagKey = blockArgumentParser.getTagId();
            boolean bl = blockState != null;
            boolean bl2 = tagKey != null;
            if (bl) {
                return Lists.newArrayList(new Text[]{blockState.getBlock().getName().formatted(Formatting.DARK_GRAY)});
            }

            if (bl2) {
                List<Text> list = (List) Streams.stream(Registry.BLOCK.iterateEntries(tagKey)).map((entry) -> {
                    return ((Block)entry.value()).getName();
                }).map((text) -> {
                    return text.formatted(Formatting.DARK_GRAY);
                }).collect(Collectors.toList());
                if (!list.isEmpty()) {
                    return list;
                }
            }
        } catch (CommandSyntaxException var7) {
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
        if (FabricLoader.getInstance().isModLoaded("trinkets")) {
            if (stack.getItem() instanceof DDTrinketItem trinketItem) {
                TrinketsCompat.trinketCheck(player, context, list, stack, trinketItem);
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
    }

}
