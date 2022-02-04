package io.github.thatrobin.ccpacks.data_driven_classes.mechanics;

import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.thatrobin.ccpacks.factories.mechanic_factories.MechanicType;
import io.github.thatrobin.ccpacks.util.Mechanic;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Triple;

public class DDTickMechanic extends Mechanic {

    public ActionFactory<Triple<World, BlockPos, Direction>>.Instance action;
    public ConditionFactory<CachedBlockPosition>.Instance block_condition;
    private final int interval;

    public DDTickMechanic(MechanicType<?> mechanicType, BlockEntity blockEntity, int interval, ActionFactory<Triple<World, BlockPos, Direction>>.Instance action, ConditionFactory<CachedBlockPosition>.Instance block_condition) {
        super(mechanicType, blockEntity);
        this.action = action;
        this.interval = interval;
        this.block_condition = block_condition;
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
