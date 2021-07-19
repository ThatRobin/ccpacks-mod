package io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.Entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class DDMushroomCowEntity extends MooshroomEntity {

    public DDMushroomCowEntity(EntityType<? extends MooshroomEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public MooshroomEntity createChild(ServerWorld world, PassiveEntity entity) {
        return (DDMushroomCowEntity)entity;
    }

}
