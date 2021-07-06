package io.github.connor3001.ccpacks.dataDrivenTypes;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Consumer;

public class DDStairBlock extends StairsBlock {

    private Consumer<Entity> entityAction;
    private ConditionFactory<LivingEntity>.Instance condition;

    public DDStairBlock(BlockState state, Settings settings, Consumer<Entity> entityAction, ConditionFactory<LivingEntity>.Instance condition) {
        super(state, settings);
        this.entityAction = entityAction;
        this.condition = condition;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (this.entityAction != null) {
            if (condition != null) {
                if (this.condition.test(player)) {
                    this.entityAction.accept(player);
                    return ActionResult.SUCCESS;
                }
            } else {
                if (this.entityAction != null) {
                    this.entityAction.accept(player);
                    return ActionResult.SUCCESS;
                } else {
                    return ActionResult.FAIL;
                }
            }
        } else {
            return ActionResult.FAIL;
        }
        return ActionResult.FAIL;
    }
}
