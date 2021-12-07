package io.github.thatrobin.ccpacks.data_driven_classes.mechanics;

import io.github.thatrobin.ccpacks.factories.mechanic_factories.MechanicType;
import io.github.thatrobin.ccpacks.util.Mechanic;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Triple;

public class DDNeighourUpdateMechanic extends Mechanic {

    public ActionFactory<Triple<World, BlockPos, Direction>>.Instance block_action;
    public ActionFactory<Triple<World, BlockPos, Direction>>.Instance block_action2;
    public Direction dir;

    public DDNeighourUpdateMechanic(MechanicType<?> mechanicType, BlockEntity blockEntity, Direction dir, ActionFactory<Triple<World, BlockPos, Direction>>.Instance block_action, ActionFactory<Triple<World, BlockPos, Direction>>.Instance block_action2) {
        super(mechanicType, blockEntity);
        this.dir = dir;
        this.block_action = block_action;
        this.block_action2 = block_action2;
    }

    @Override
    public void fromTag(NbtElement tag) {

    }

    @Override
    public void executeBlockAction(Triple<World, BlockPos, Direction> data){
        if (this.block_action != null) {
            this.block_action.accept(data);
        }
    }

    public void executeNeighborAction(Triple<World, BlockPos, Direction> data){
        if (this.block_action2 != null) {
            this.block_action2.accept(data);
        }
    }

}
