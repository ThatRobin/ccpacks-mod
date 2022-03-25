package io.github.thatrobin.ccpacks.factories.content_factories;


import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.thatrobin.ccpacks.data_driven_classes.entities.projectile_entities.DDProjectileEntity;
import io.github.thatrobin.ccpacks.registries.CCPacksRegistries;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

public class ProjectileFactories {

    public static Identifier identifier(String string) {
        return new Identifier("projectile", string);
    }

    public static void register() {
        register(new ContentFactory<>(identifier("item"), Types.PROJECTILE,
                new SerializableData()
                        .add("damage", SerializableDataTypes.INT, 0)
                        .add("height", SerializableDataTypes.FLOAT, 0.25f)
                        .add("width", SerializableDataTypes.FLOAT, 0.25f)
                        .add("gravity", SerializableDataTypes.FLOAT, 0.03f)
                        .add("base_item", SerializableDataTypes.ITEM)
                        .add("damage_source", SerializableDataTypes.DAMAGE_SOURCE),
                data ->
                        (contentType, content) -> {
                            return (Supplier<EntityType<?>>) () -> FabricEntityTypeBuilder.<DDProjectileEntity>create(SpawnGroup.MISC, (world, entity_type) -> new DDProjectileEntity(world, entity_type, data.getInt("damage"), data.get("base_item"), data.get("damage_source"), data.getFloat("gravity"))).dimensions(EntityDimensions.fixed(data.getFloat("width"), data.getFloat("height"))).trackable(64, 10).build();
                        }));

    }

    private static void register(ContentFactory<Supplier<?>> serializer) {
        Registry.register(CCPacksRegistries.CONTENT_FACTORY, serializer.getSerializerId(), serializer);
    }
}
