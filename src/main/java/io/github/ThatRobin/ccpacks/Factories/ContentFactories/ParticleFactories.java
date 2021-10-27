package io.github.ThatRobin.ccpacks.Factories.ContentFactories;

import io.github.ThatRobin.ccpacks.CCPackDataTypes;
import io.github.ThatRobin.ccpacks.Registries.CCPacksRegistries;
import io.github.ThatRobin.ccpacks.DataDrivenClasses.Particles.DDGlowParticle;
import io.github.ThatRobin.ccpacks.DataDrivenClasses.Particles.DDParticle;
import io.github.ThatRobin.ccpacks.Util.ColourHolder;
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

    public static void register() {
        register(new ContentFactory<>(identifier("generic"), Types.PARTICLE,
                new SerializableData()
                        .add("color", CCPackDataTypes.COLOR, new ColourHolder(1,1,1,1))
                        .add("size", SerializableDataTypes.FLOAT, 0.25f)
                        .add("max_age", SerializableDataTypes.INT, 100)
                        .add("collides_with_world", SerializableDataTypes.BOOLEAN, false),
                data ->
                        (contentType, content) -> {
                            ColourHolder colourHolder = (ColourHolder) data.get("color");
                            DDParticle.red = colourHolder.getRed();
                            DDParticle.green = colourHolder.getGreen();
                            DDParticle.blue = colourHolder.getBlue();
                            DDParticle.alpha = colourHolder.getAlpha();
                            DDParticle.max_age = data.getInt("max_age");
                            DDParticle.collides_with_world = data.getBoolean("collides_with_world");
                            DDParticle.size = data.getFloat("size");
                            DefaultParticleType TEST = FabricParticleTypes.simple();

                            return (Supplier<DefaultParticleType>) () -> TEST;
                        }));

        register(new ContentFactory<>(identifier("emissive"), Types.PARTICLE,
                new SerializableData()
                        .add("color", CCPackDataTypes.COLOR, new ColourHolder(1,1,1,1))
                        .add("size", SerializableDataTypes.FLOAT, 0.25f)
                        .add("max_age", SerializableDataTypes.INT, 100)
                        .add("collides_with_world", SerializableDataTypes.BOOLEAN, false)
                        .add("alpha", SerializableDataTypes.FLOAT, 1f),
                data ->
                        (contentType, content) -> {
                            ColourHolder colourHolder = (ColourHolder) data.get("color");
                            DDParticle.red = colourHolder.getRed();
                            DDParticle.green = colourHolder.getGreen();
                            DDParticle.blue = colourHolder.getBlue();
                            DDParticle.alpha = colourHolder.getAlpha();
                            DDGlowParticle.max_age = data.getInt("max_age");
                            DDGlowParticle.collides_with_world = data.getBoolean("collides_with_world");
                            DDGlowParticle.size = data.getFloat("size");
                            DefaultParticleType TEST = FabricParticleTypes.simple();

                            return (Supplier<DefaultParticleType>) () -> TEST;
                        }));
    }

    private static void register(ContentFactory<Supplier<?>> serializer) {
        Registry.register(CCPacksRegistries.CONTENT_FACTORY, serializer.getSerializerId(), serializer);
    }
}
