package io.github.thatrobin.ccpacks.data_driven_classes.entities.projectile_entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class DDProjectileEntity extends ThrownItemEntity {

    public int damage;
    public Item base_item;
    public DamageSource damage_source;
    public float gravity;

    public DDProjectileEntity(EntityType<? extends DDProjectileEntity> entityType, World world, int damage, Item base_item, DamageSource damage_source, float gravity) {
        super(entityType, world);
        this.damage = damage;
        this.damage_source = damage_source;
        this.base_item = base_item;
        this.gravity = gravity;
    }

    @Override
    protected Item getDefaultItem() {
        return base_item;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        entity.damage(DamageSource.thrownProjectile(this, this.getOwner()), (float) damage);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if(!this.world.isClient) {
            this.discard();
        }
    }

    @Override
    protected float getGravity() {
        return gravity;
    }

}
