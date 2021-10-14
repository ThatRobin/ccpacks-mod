package io.github.ThatRobin.ccpacks.Factories.ContentFactories;


import io.github.ThatRobin.ccpacks.Registries.CCPacksRegistries;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.Entities.ProjectileEntities.DDProjectileEntity;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

public class ProjectileFactories {

    public static Identifier identifier(String string) {
        return new Identifier("projectile", string);
    }

    @SuppressWarnings("unchecked")
    public static void register() {
        register(new ContentFactory<>(identifier("item"), Types.PROJECTILE,
                new SerializableData()
                        .add("damage", SerializableDataTypes.INT, 0)
                        .add("height", SerializableDataTypes.FLOAT, 0.25f)
                        .add("width", SerializableDataTypes.FLOAT, 0.25f)
                        .add("base_item", SerializableDataTypes.ITEM)
                        .add("damage_source", SerializableDataTypes.DAMAGE_SOURCE),
                data ->
                        (contentType, content) -> {

                            DDProjectileEntity.damage = data.getInt("damage");
                            DDProjectileEntity.base_item = (Item) data.get("base_item");
                            DDProjectileEntity.damage_source = (DamageSource) data.get("damage_source");

                            return (Supplier<EntityType>) () -> FabricEntityTypeBuilder.<DDProjectileEntity>create(SpawnGroup.MISC, DDProjectileEntity::new).dimensions(EntityDimensions.fixed(data.getFloat("width"), data.getFloat("height"))).trackable(64, 10).build();
                        }));

    }

    private static void register(ContentFactory<Supplier<?>> serializer) {
        Registry.register(CCPacksRegistries.CONTENT_FACTORY, serializer.getSerializerId(), serializer);
    }
}
