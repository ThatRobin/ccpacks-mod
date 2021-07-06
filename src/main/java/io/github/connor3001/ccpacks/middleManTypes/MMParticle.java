package io.github.connor3001.ccpacks.middleManTypes;

import io.github.connor3001.ccpacks.dataDrivenTypes.DDParticleType;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MMParticle {

    public MMParticle(Identifier identifier){
        DefaultParticleType TEST = Registry.register(Registry.PARTICLE_TYPE, identifier, FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(TEST, DDParticleType.Factory::new);
    }
}
