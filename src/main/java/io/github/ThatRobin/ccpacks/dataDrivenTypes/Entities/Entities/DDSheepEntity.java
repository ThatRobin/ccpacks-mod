package io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.Entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class DDSheepEntity extends SheepEntity {

    public DDSheepEntity(EntityType<? extends SheepEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public SheepEntity createChild(ServerWorld world, PassiveEntity entity) {
        return (DDSheepEntity)entity;
    }

}
