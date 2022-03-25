package io.github.thatrobin.ccpacks.mixins;

import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.thatrobin.ccpacks.util.AccessFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PowerFactory.Instance.class)
public class PowerFactoryInstanceMixin implements AccessFactory {
    @Shadow @Final private PowerFactory this$0;

    @Override
    public PowerFactory<?> getFactory() {
        return this.this$0;
    }
}
