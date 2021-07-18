package io.github.ThatRobin.ccpacks.dataDrivenTypes.Particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import org.jetbrains.annotations.Nullable;

public class DDParticle extends SpriteBillboardParticle {

    protected DDParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, SpriteProvider spriteProvider) {
        super(clientWorld, d, e, f, g, h, i);
        this.scale = 0.25f; //about a block tall
        this.maxAge = 300; //ticks
        int maxHeight = 1;
        this.collidesWithWorld = false;
        this.setSpriteForAge(spriteProvider);
        this.colorAlpha = 1f;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_LIT;
    } //this can be PARTICLE_SHEET_TRANSLUCENT , PARTICLE_SHEET_OPAQUE or PARTICLE_SHEET_LIT


    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {

        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }


        @Nullable
        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            DDParticle testParticle = new DDParticle(clientWorld, d, e, f, g, h, i, spriteProvider);
            testParticle.setSprite(this.spriteProvider);
            return testParticle;
        }
    }

}