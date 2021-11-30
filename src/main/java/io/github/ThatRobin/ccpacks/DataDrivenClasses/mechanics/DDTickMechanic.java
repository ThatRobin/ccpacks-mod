package io.github.ThatRobin.ccpacks.DataDrivenClasses.mechanics;

import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.ThatRobin.ccpacks.DataDrivenClasses.Blocks.DDBlockEntity;
import io.github.ThatRobin.ccpacks.Factories.MechanicFactories.MechanicFactory;
import io.github.ThatRobin.ccpacks.Factories.MechanicFactories.MechanicType;
import io.github.ThatRobin.ccpacks.Util.Mechanic;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtInt;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Triple;

import java.util.function.Consumer;

public class DDTickMechanic extends Mechanic {

    public ActionFactory<Triple<World, BlockPos, Direction>>.Instance action;
    public ConditionFactory<CachedBlockPosition>.Instance block_condition;
    private int interval;

    public DDTickMechanic(MechanicType<?> mechanicType, BlockEntity blockEntity, int interval, ActionFactory<Triple<World, BlockPos, Direction>>.Instance action, ConditionFactory<CachedBlockPosition>.Instance block_condition) {
        super(mechanicType, blockEntity);
        this.action = action;
        this.interval = interval;
        this.block_condition = block_condition;
    }

    @Override
    public void fromTag(NbtElement tag) {

    }

    public void tick(Triple<World, BlockPos, Direction> data, int totalTicks){
        if(totalTicks % this.interval == 0) {
            if (this.action != null) {
                if (this.block_condition != null) {
                    if (this.block_condition.test(new CachedBlockPosition(data.getLeft(), data.getMiddle(), false))) {
                        this.action.accept(data);
                    }
                } else {
                    this.action.accept(data);
                }
            }
        }
    }

}
