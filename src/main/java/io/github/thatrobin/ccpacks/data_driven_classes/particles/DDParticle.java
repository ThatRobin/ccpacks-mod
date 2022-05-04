package io.github.thatrobin.ccpacks.data_driven_classes.particles;

import io.github.thatrobin.ccpacks.util.ColourHolder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import org.jetbrains.annotations.Nullable;

public class DDParticle extends SpriteBillboardParticle {



    public DDParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, SpriteProvider spriteProvider, float size, int max_age, boolean collides_with_world, ColourHolder colourHolder) {
        super(clientWorld, d, e, f, g, h, i);
        this.scale = size;
        this.maxAge = max_age; //ticks
        this.collidesWithWorld = collides_with_world;
        this.setSpriteForAge(spriteProvider);
        this.setAlpha(colourHolder.getAlpha());
        this.setColor(colourHolder.getRed(), colourHolder.getGreen(), colourHolder.getBlue());
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_LIT;
    } //this can be PARTICLE_SHEET_TRANSLUCENT , PARTICLE_SHEET_OPAQUE or PARTICLE_SHEET_LIT

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {

        private final SpriteProvider spriteProvider;
        public float size;
        public int max_age;
        public boolean collides_with_world;
        public ColourHolder colourHolder;

        public Factory(SpriteProvider spriteProvider, float size, int max_age, boolean collides_with_world, ColourHolder colourHolder) {
            this.spriteProvider = spriteProvider;
            this.size = size;
            this.max_age = max_age;
            this.collides_with_world = collides_with_world;
            this.colourHolder = colourHolder;
        }

        @Nullable
        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            DDParticle testParticle = new DDParticle(clientWorld, d, e, f, g, h, i, spriteProvider, this.size, this.max_age, this.collides_with_world, this.colourHolder);
            testParticle.setSprite(this.spriteProvider);
            return testParticle;
        }
    }

}