package io.github.thatrobin.ccpacks.data_driven_classes.mechanics;

import io.github.thatrobin.ccpacks.factories.mechanic_factories.MechanicType;
import io.github.thatrobin.ccpacks.util.Mechanic;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Triple;

public class DDFallMechanic extends Mechanic {

    public ActionFactory<Triple<World, BlockPos, Direction>>.Instance block_action;
    public ActionFactory<Entity>.Instance entity_action;
    public float damage_multipler;

    public DDFallMechanic(MechanicType<?> mechanicType, BlockEntity blockEntity, float damage_multipler, ActionFactory<Entity>.Instance entity_action, ActionFactory<Triple<World, BlockPos, Direction>>.Instance block_action) {
        super(mechanicType, blockEntity);
        this.damage_multipler = damage_multipler;
        this.block_action = block_action;
        this.entity_action = entity_action;
    }

    @Override
    public void executeBlockAction(Triple<World, BlockPos, Direction> data){
        if (this.block_action == null)
            return;
        this.block_action.accept(data);

    }

    @Override
    public void executeEntityAction(Entity entity) {
        if (this.entity_action == null)
            return;
        this.entity_action.accept(entity);
    }

}
