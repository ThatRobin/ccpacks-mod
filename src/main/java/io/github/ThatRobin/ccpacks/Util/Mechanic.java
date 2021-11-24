package io.github.ThatRobin.ccpacks.Util;

import io.github.ThatRobin.ccpacks.Factories.MechanicFactories.MechanicType;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Triple;

public class Mechanic {

    protected MechanicType<?> type;
    protected BlockEntity blockEntity;
    public NbtCompound element = new NbtCompound();

    public Mechanic(MechanicType<?> type, BlockEntity blockEntity) {
        this.type = type;
        this.blockEntity = blockEntity;
    }

    public void fromTag(NbtElement tag) {
    }

    public MechanicType<?> getType() {
        return type;
    }

    public void executeBlockAction(Triple<World, BlockPos, Direction> data){
    }

    public void executeEntityAction(Entity entity){
    }

    public void setNbt(NbtCompound element) {
        this.element = element;
    }

    public NbtCompound getNbt() {
        return this.element;
    }

}
