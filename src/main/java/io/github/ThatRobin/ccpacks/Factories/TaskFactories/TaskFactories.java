package io.github.ThatRobin.ccpacks.Factories.TaskFactories;

import io.github.ThatRobin.ccpacks.DataDrivenClasses.Entities.goals.DDAttackGoal;
import io.github.ThatRobin.ccpacks.Registries.CCPacksRegistries;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.List;

public class TaskFactories {

    public static Identifier identifier(String path) {
        return new Identifier("mob_tasks", path);
    }

    public static void register() {
        register(new TaskFactory<>(identifier("attack"),
                new SerializableData(),
                data ->
                        (identifier, factory) -> {
                            DDAttackGoal attackGoal = new DDAttackGoal(identifier, factory);
                            return attackGoal;
                        }));
    }

    private static void register(TaskFactory serializer) {
        Registry.register(CCPacksRegistries.TASK_FACTORY, serializer.getSerializerId(), serializer);
    }

}
