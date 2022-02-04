package io.github.thatrobin.ccpacks.factories.task_factories;

import io.github.thatrobin.ccpacks.data_driven_classes.entities.goals.DDAttackGoal;
import io.github.thatrobin.ccpacks.registries.CCPacksRegistries;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TaskFactories {

    public static Identifier identifier(String path) {
        return new Identifier("mob_tasks", path);
    }

    public static void register() {
        register(new TaskFactory<>(identifier("attack"),
                new SerializableData(),
                data ->
                        DDAttackGoal::new));
    }

    private static void register(TaskFactory<?> serializer) {
        Registry.register(CCPacksRegistries.TASK_FACTORY, serializer.getSerializerId(), serializer);
    }

}
