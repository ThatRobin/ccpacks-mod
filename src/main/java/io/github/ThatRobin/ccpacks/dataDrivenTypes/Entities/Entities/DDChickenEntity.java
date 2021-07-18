package io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.Entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class DDChickenEntity extends ChickenEntity {

    public DDChickenEntity(EntityType<? extends ChickenEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public ChickenEntity createChild(ServerWorld world, PassiveEntity entity) {
        return (DDChickenEntity)entity;
    }

    public Object getSize() {
        return 1f;
    }
}
