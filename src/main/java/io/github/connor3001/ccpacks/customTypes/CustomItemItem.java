package io.github.connor3001.ccpacks.customTypes;

import io.github.apace100.apoli.power.Active;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CommandItemSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.function.Consumer;

public class CustomItemItem extends Item {

    private Boolean consume;
    private Consumer<Entity> action;
    private ConditionFactory<LivingEntity>.Instance condition;
    private PlayerEntity player;

    public CustomItemItem(Settings settings, Boolean consume,Consumer<Entity> action, ConditionFactory<LivingEntity>.Instance condition) {
        super(settings);
        this.action = action;
        this.condition = condition;
        this.consume = consume;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (this.isFood()) {
            ItemStack itemStack = user.getStackInHand(hand);
            if (user.canConsume(this.getFoodComponent().isAlwaysEdible())) {
                user.setCurrentHand(hand);
                return TypedActionResult.consume(itemStack);
            } else {
                return TypedActionResult.fail(itemStack);
            }
        } else {
            if((this.condition).test((LivingEntity)user)) {
                if (consume) {
                    this.action.accept(user);
                    return TypedActionResult.consume(user.getStackInHand(hand));
                } else {
                    this.action.accept(user);
                    return TypedActionResult.pass(user.getStackInHand(hand));
                }
            } else {
                return TypedActionResult.pass(user.getStackInHand(hand));
            }
        }
    }
}
