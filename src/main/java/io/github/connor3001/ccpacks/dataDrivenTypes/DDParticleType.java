package io.github.connor3001.ccpacks.dataDrivenTypes;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

public class DDParticleType extends SpriteBillboardParticle {

    protected DDParticleType(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, SpriteProvider spriteProvider) {
        super(clientWorld, d, e, f, g, h, i);
        this.scale = 1f; //about a block tall
        this.maxAge = 300; //ticks
        int maxHeight = 1;
        this.collidesWithWorld = false;
        this.setSpriteForAge(spriteProvider);
        this.colorAlpha = 1f;
        this.setColor(1,0.988f,0.160f);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_LIT;
    } //this can be PARTICLE_SHEET_TRANSLUCENT , PARTICLE_SHEET_OPAQUE or PARTICLE_SHEET_LIT

    public int getBrightness(float tint) { //What makes it glow
        float f = ((float)this.age + tint);
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        int i = super.getBrightness(tint);
        int j = i & 255;
        int k = i >> 16 & 255;
        j += (int)(f * 15.0F * 16.0F);
        if (j > 240) {
            j = 240;
        }
        return j | k << 16;
    }

    public void move(double dx, double dy, double dz) { //not currently sure but might be movement?
        this.setBoundingBox(this.getBoundingBox().offset(dx, dy, dz));
        this.repositionFromBoundingBox();
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {

        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }


        @Nullable
        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            DDParticleType testParticle = new DDParticleType(clientWorld, d, e, f, g, h, i, spriteProvider);
            testParticle.setSprite(this.spriteProvider);
            return testParticle;
        }
    }
}
