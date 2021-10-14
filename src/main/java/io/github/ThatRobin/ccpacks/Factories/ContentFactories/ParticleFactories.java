package io.github.ThatRobin.ccpacks.Factories.ContentFactories;

import io.github.ThatRobin.ccpacks.Registries.CCPacksRegistries;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.Particles.DDGlowParticle;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.Particles.DDParticle;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

public class ParticleFactories {

    public static Identifier identifier(String string) {
        return new Identifier("particle", string);
    }

    @SuppressWarnings("unchecked")
    public static void register() {
        register(new ContentFactory<>(identifier("generic"), Types.PARTICLE,
                new SerializableData()
                        .add("red", SerializableDataTypes.FLOAT, null)
                        .add("green", SerializableDataTypes.FLOAT, null)
                        .add("blue", SerializableDataTypes.FLOAT, null)
                        .add("size", SerializableDataTypes.FLOAT, 0.25f)
                        .add("max_age", SerializableDataTypes.INT, 100)
                        .add("collides_with_world", SerializableDataTypes.BOOLEAN, false)
                        .add("alpha", SerializableDataTypes.FLOAT, 1f),
                data ->
                        (contentType, content) -> {
                            DDParticle.red = data.getFloat("red");
                            DDParticle.green = data.getFloat("green");
                            DDParticle.blue = data.getFloat("blue");
                            DDParticle.alpha = data.getFloat("alpha");
                            DDParticle.max_age = data.getInt("max_age");
                            DDParticle.collides_with_world = data.getBoolean("collides_with_world");
                            DDParticle.size = data.getFloat("size");
                            DefaultParticleType TEST = FabricParticleTypes.simple();
                            Supplier<DefaultParticleType> EXAMPLE_PARTICLE = () -> TEST;

                            return EXAMPLE_PARTICLE;
                        }));

        register(new ContentFactory<>(identifier("emissive"), Types.PARTICLE,
                new SerializableData()
                        .add("red", SerializableDataTypes.FLOAT, null)
                        .add("green", SerializableDataTypes.FLOAT, null)
                        .add("blue", SerializableDataTypes.FLOAT, null)
                        .add("size", SerializableDataTypes.FLOAT, 0.25f)
                        .add("max_age", SerializableDataTypes.INT, 100)
                        .add("collides_with_world", SerializableDataTypes.BOOLEAN, false)
                        .add("alpha", SerializableDataTypes.FLOAT, 1f),
                data ->
                        (contentType, content) -> {
                            DDGlowParticle.red = data.getFloat("red");
                            DDGlowParticle.green = data.getFloat("green");
                            DDGlowParticle.blue = data.getFloat("blue");
                            DDGlowParticle.alpha = data.getFloat("alpha");
                            DDGlowParticle.max_age = data.getInt("max_age");
                            DDGlowParticle.collides_with_world = data.getBoolean("collides_with_world");
                            DDGlowParticle.size = data.getFloat("size");
                            DefaultParticleType TEST = FabricParticleTypes.simple();
                            Supplier<DefaultParticleType> EXAMPLE_PARTICLE = () -> TEST;

                            return EXAMPLE_PARTICLE;
                        }));
    }

    private static void register(ContentFactory<Supplier<?>> serializer) {
        Registry.register(CCPacksRegistries.CONTENT_FACTORY, serializer.getSerializerId(), serializer);
    }
}
