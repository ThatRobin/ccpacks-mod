package io.github.ThatRobin.ccpacks.Mixins;

import io.github.ThatRobin.ccpacks.Power.ActionOnProjectileLand;
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProjectileEntity.class)
public abstract class ProjectileEntityMixin {

    @Shadow @Nullable public abstract Entity getOwner();

    @Inject(method = "onCollision", at = @At(value = "HEAD"), cancellable = true)
    public void getTexture(HitResult hitResult, CallbackInfo ci) {
        PowerHolderComponent.withPower(this.getOwner(), ActionOnProjectileLand.class, null, actionOnProjectileLand -> {
            BlockPos pos = new BlockPos(hitResult.getPos().x,hitResult.getPos().y,hitResult.getPos().z);
            if(actionOnProjectileLand.doesApply(pos)) {
                actionOnProjectileLand.executeActions(pos, Direction.UP);
            }
        });
    }
}
