package io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.ProjectileEntities;

import io.github.ThatRobin.ccpacks.registries.CCPackClientRegistry;
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
    private int damage;

    public DDProjectileEntity(EntityType<? extends DDProjectileEntity> entityType, World world, int damage) {
        super(entityType, world);
        this.damage = damage;
    }

    public DDProjectileEntity(World world, LivingEntity owner) { //null will be changed into the entity type once it has been registered. Same for the constructor below
        super(null, owner, world);
    }

    public DDProjectileEntity(World world, double x, double y, double z) {
        super(null, x, y, z, world);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.DIAMOND;
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
}
