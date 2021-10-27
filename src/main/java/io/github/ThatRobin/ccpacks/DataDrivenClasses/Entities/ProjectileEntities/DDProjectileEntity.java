package io.github.ThatRobin.ccpacks.DataDrivenClasses.Entities.ProjectileEntities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

//public class DDProjectileEntity extends ThrownItemEntity implements VirtualEntity {
public class DDProjectileEntity extends ThrownItemEntity {

    public static int damage;
    public static Item base_item;
    public static DamageSource damage_source;

    public DDProjectileEntity(EntityType<? extends DDProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public DDProjectileEntity(World world, LivingEntity owner) { //null will be changed into the entity type once it has been registered. Same for the constructor below
        super(null, owner, world);
    }

    public DDProjectileEntity(World world, double x, double y, double z) {
        super(null, x, y, z, world);
    }

    @Override
    protected Item getDefaultItem() {
        return this.base_item;
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

    //@Override
    public EntityType<?> getVirtualEntityType() {
        return EntityType.SNOWBALL;
    }
}
