package io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.Entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class DDCowEntity extends CowEntity {

    public DDCowEntity(EntityType<? extends CowEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public CowEntity createChild(ServerWorld world, PassiveEntity entity) {
        return (DDCowEntity)entity;
    }

    public Object getSize() {
        return 1f;
    }
}
