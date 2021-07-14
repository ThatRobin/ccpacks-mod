package io.github.ThatRobin.ccpacks;

import io.github.apace100.apoli.power.PlayerAbilityPower;
import io.github.apace100.apoli.power.PowerType;
import net.adriantodt.fallflyinglib.FallFlyingLib;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class CustomElytraFlightPower extends PlayerAbilityPower {

    private final boolean renderElytra;
    private Identifier skin = new Identifier("minecraft", "textures/entity/elytra.png");

    public CustomElytraFlightPower(PowerType<?> type, LivingEntity entity, boolean renderElytra, Identifier skin) {
        super(type, entity, FallFlyingLib.ABILITY);
        this.renderElytra = renderElytra;
        this.skin = skin;
    }

    public boolean shouldRenderElytra() {
        return renderElytra;
    }

    public Identifier getElytraSkin() {
        return this.skin;
    }
}
