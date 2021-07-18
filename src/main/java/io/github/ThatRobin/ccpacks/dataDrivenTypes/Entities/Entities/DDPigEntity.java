package io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.Entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class DDPigEntity extends PigEntity {

    public DDPigEntity(EntityType<? extends PigEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public PigEntity createChild(ServerWorld world, PassiveEntity entity) {
        return (DDPigEntity)entity;
    }

    public Object getSize() {
        return 1f;
    }
}
