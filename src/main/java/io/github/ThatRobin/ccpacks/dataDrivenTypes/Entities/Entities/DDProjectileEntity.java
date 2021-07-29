package io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.Entities;

import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class DDProjectileEntity extends ThrownItemEntity {
    public static int damage;
    public static Item base_item;
    public static DamageSource damage_source;
    public static ActionFactory<Entity>.Instance hit_action;
    public static ActionFactory<Entity>.Instance collision_action;

    public DDProjectileEntity(EntityType<? extends DDProjectileEntity> entityType, World world, int damage) {
        super(entityType, world);
        this.damage = damage;
        this.base_item = base_item;
        this.damage_source = damage_source;
        this.hit_action = hit_action;
        this.collision_action = collision_action;
    }

    public DDProjectileEntity(World world, LivingEntity owner) { //null will be changed into the entity type once it has been registered. Same for the constructor below
        super(CCPacksMain.EXAMPLE_PROJECTILE, owner, world);
    }

    @Environment(EnvType.CLIENT)
    public DDProjectileEntity(World world, double x, double y, double z) {
        super(CCPacksMain.EXAMPLE_PROJECTILE, x, y, z, world);
    }

    public DDProjectileEntity(EntityType<DDProjectileEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected Item getDefaultItem() {
        return base_item;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        entity.damage(damage_source, (float) damage);
        this.hit_action.accept(entity);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        this.collision_action.accept(this);
        if(!this.world.isClient) {
            this.discard();
        }
    }
}
