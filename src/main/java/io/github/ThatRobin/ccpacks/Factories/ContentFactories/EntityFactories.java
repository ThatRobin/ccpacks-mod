package io.github.ThatRobin.ccpacks.Factories.ContentFactories;

import io.github.ThatRobin.ccpacks.CCPackDataTypes;
import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.ThatRobin.ccpacks.DataDrivenClasses.DDEnchantment;
import io.github.ThatRobin.ccpacks.DataDrivenClasses.Entities.DDAnimalEntity;
import io.github.ThatRobin.ccpacks.Factories.TaskFactories.TaskRegistry;
import io.github.ThatRobin.ccpacks.Registries.CCPacksRegistries;
import io.github.ThatRobin.ccpacks.Util.GoalMap;
import io.github.ThatRobin.ccpacks.Util.TypeAttributeHolder;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import software.bernie.example.registry.EntityRegistryBuilder;

import java.util.List;
import java.util.function.Supplier;

public class EntityFactories {

    public static Identifier identifier(String string) {
        return new Identifier("entity", string);
    }

    public static void register() {
        register(new ContentFactory<>(identifier("generic"), Types.ENTITY,
                new SerializableData()
                        .add("spawn_group", CCPackDataTypes.SPAWN_GROUP, SpawnGroup.CREATURE)
                        .add("attributes", CCPackDataTypes.ENTITY_ATTRIBUTES, DefaultAttributeContainer.builder())
                        .add("tasks", SerializableDataTypes.IDENTIFIERS, null)
                        .add("width", SerializableDataTypes.FLOAT, 1f)
                        .add("height", SerializableDataTypes.FLOAT, 1f),
                data ->
                        (contentType, content) -> {
                            List<Identifier> tasks = (List<Identifier>)data.get("tasks");
                            FabricEntityTypeBuilder entity = FabricEntityTypeBuilder.create((SpawnGroup) data.get("spawn_group"), DDAnimalEntity::new).dimensions(new EntityDimensions(data.getFloat("width"), data.getFloat("height"), true));
                            GoalMap.goals.clear();
                            TypeAttributeHolder holder = new TypeAttributeHolder(entity, (DefaultAttributeContainer.Builder)data.get("attributes"));
                            return () -> holder;
        }));

    }

    private static void register(ContentFactory<Supplier<?>> serializer) {
        Registry.register(CCPacksRegistries.CONTENT_FACTORY, serializer.getSerializerId(), serializer);
    }
}
