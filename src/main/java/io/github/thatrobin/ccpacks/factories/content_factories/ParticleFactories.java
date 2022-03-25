package io.github.thatrobin.ccpacks.factories.content_factories;

import io.github.thatrobin.ccpacks.CCPackDataTypes;
import io.github.thatrobin.ccpacks.registries.CCPacksRegistries;
import io.github.thatrobin.ccpacks.data_driven_classes.particles.DDGlowParticle;
import io.github.thatrobin.ccpacks.data_driven_classes.particles.DDParticle;
import io.github.thatrobin.ccpacks.util.ColourHolder;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.thatrobin.ccpacks.util.ParticleHolder;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

public class ParticleFactories {

    public static Identifier identifier(String string) {
        return new Identifier("particle", string);
    }

    public static void register() {
        register(new ContentFactory<>(identifier("generic"), Types.PARTICLE,
                new SerializableData()
                        .add("colour", CCPackDataTypes.COLOUR, new ColourHolder(1,1,1,1))
                        .add("size", SerializableDataTypes.FLOAT, 0.25f)
                        .add("max_age", SerializableDataTypes.INT, 100)
                        .add("collides_with_world", SerializableDataTypes.BOOLEAN, false),
                data ->
                        (contentType, content) -> {
                            ColourHolder colourHolder = data.get("colour");
                            int max_age = data.getInt("max_age");
                            boolean collides_with_world = data.getBoolean("collides_with_world");
                            float size = data.getFloat("size");
                            DefaultParticleType particleType = FabricParticleTypes.simple();

                            return (Supplier<ParticleHolder>) () -> new ParticleHolder(size, max_age, collides_with_world, colourHolder, particleType);
                        }));

        register(new ContentFactory<>(identifier("emissive"), Types.PARTICLE,
                new SerializableData()
                        .add("colour", CCPackDataTypes.COLOUR, new ColourHolder(1,1,1,1))
                        .add("size", SerializableDataTypes.FLOAT, 0.25f)
                        .add("max_age", SerializableDataTypes.INT, 100)
                        .add("collides_with_world", SerializableDataTypes.BOOLEAN, false),
                data ->
                        (contentType, content) -> {
                            ColourHolder colourHolder = data.get("colour");
                            int max_age = data.getInt("max_age");
                            boolean collides_with_world = data.getBoolean("collides_with_world");
                            float size = data.getFloat("size");
                            DefaultParticleType particleType = FabricParticleTypes.simple();

                            return (Supplier<ParticleHolder>) () -> new ParticleHolder(size, max_age, collides_with_world, colourHolder, particleType);
                        }));
    }

    private static void register(ContentFactory<Supplier<?>> serializer) {
        Registry.register(CCPacksRegistries.CONTENT_FACTORY, serializer.getSerializerId(), serializer);
    }
}
