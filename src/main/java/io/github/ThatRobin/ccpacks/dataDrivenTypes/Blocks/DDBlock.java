package io.github.ThatRobin.ccpacks.dataDrivenTypes.Blocks;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Consumer;

public class DDBlock extends Block {

    public DDBlock(Settings settings) {
        super(settings);
    }

}
