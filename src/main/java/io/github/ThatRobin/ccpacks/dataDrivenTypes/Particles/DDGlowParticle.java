package io.github.ThatRobin.ccpacks.dataDrivenTypes.Particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

public class DDGlowParticle extends SpriteBillboardParticle {
    public static float size;
    public static int max_age;
    public static boolean collides_with_world;
    public static float alpha;
    public static float red;
    public static float green;
    public static float blue;


    protected DDGlowParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, SpriteProvider spriteProvider) {
        super(clientWorld, d, e, f, g, h, i);
        this.scale = size;
        this.maxAge = max_age; //ticks
        int maxHeight = 1;
        this.collidesWithWorld = collides_with_world;
        this.setSpriteForAge(spriteProvider);
        this.colorAlpha = alpha;
        this.setColor(red, green, blue);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_LIT;
    } //this can be PARTICLE_SHEET_TRANSLUCENT , PARTICLE_SHEET_OPAQUE or PARTICLE_SHEET_LIT


    public int getBrightness ( float tint){ //What makes it glow

        float f = ((float) this.age + tint);
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        int i = super.getBrightness(tint);
        int j = i & 255;
        int k = i >> 16 & 255;
        j += (int) (f * 15.0F * 16.0F);
        if (j > 240) {
            j = 240;
        }
        return j | k << 16;
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
            DDGlowParticle testParticle = new DDGlowParticle(clientWorld, d, e, f, g, h, i, spriteProvider);
            testParticle.setSprite(this.spriteProvider);
            return testParticle;
        }
    }

}
