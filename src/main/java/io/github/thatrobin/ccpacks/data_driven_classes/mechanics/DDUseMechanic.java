package io.github.thatrobin.ccpacks.data_driven_classes.mechanics;

import io.github.thatrobin.ccpacks.factories.mechanic_factories.MechanicType;
import io.github.thatrobin.ccpacks.util.Mechanic;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Triple;

public class DDUseMechanic extends Mechanic {

    public ActionFactory<Triple<World, BlockPos, Direction>>.Instance block_action;
    public ConditionFactory<CachedBlockPosition>.Instance block_condition;
    public ActionFactory<Entity>.Instance entity_action;

    public DDUseMechanic(MechanicType<?> mechanicType, BlockEntity blockEntity, ActionFactory<Entity>.Instance entity_action, ActionFactory<Triple<World, BlockPos, Direction>>.Instance block_action, ConditionFactory<CachedBlockPosition>.Instance block_condition) {
        super(mechanicType, blockEntity);
        this.block_action = block_action;
        this.entity_action = entity_action;
        this.block_condition = block_condition;
    }

    @Override
    public void fromTag(NbtElement tag) {
    }

    @Override
    public void executeBlockAction(Triple<World, BlockPos, Direction> data) {
        boolean passed = true;
        if (this.block_condition != null) {
            CachedBlockPosition position = new CachedBlockPosition(data.getLeft(), data.getMiddle(), false);
            if(!this.block_condition.test(position)) {
                passed = false;
            }
        }
        if(passed) {
            if (this.block_action != null) {
                this.block_action.accept(data);
            }
        }
    }

    @Override
    public void executeEntityAction(Entity entity) {
        if (this.entity_action != null) {
            this.entity_action.accept(entity);
        }
    }

}
