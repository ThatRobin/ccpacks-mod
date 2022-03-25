package io.github.thatrobin.ccpacks.util;

import net.minecraft.particle.DefaultParticleType;

public class ParticleHolder {

    public float size;
    public int max_age;
    public boolean collides_with_world;
    public ColourHolder colourHolder;
    public DefaultParticleType particleType;

    public ParticleHolder(float size, int max_age, boolean collides_with_world, ColourHolder colourHolder, DefaultParticleType particleType) {
        this.size = size;
        this.collides_with_world = collides_with_world;
        this.colourHolder = colourHolder;
        this.max_age = max_age;
        this.particleType = particleType;
    }
}
