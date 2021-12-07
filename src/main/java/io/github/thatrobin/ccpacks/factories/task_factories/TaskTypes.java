package io.github.thatrobin.ccpacks.factories.task_factories;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.thatrobin.ccpacks.registries.CCPacksRegistries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.util.Optional;
import java.util.function.BiFunction;

public class TaskTypes {

    public TaskTypes(Identifier id, JsonElement je) {
        readPower(id, je, TaskType::new);
    }

    private void readPower(Identifier id, JsonElement je, BiFunction<Identifier, TaskFactory<TaskType>.Instance, TaskType> taskFactory) {
        JsonObject jo = je.getAsJsonObject();
        Identifier factoryId = Identifier.tryParse(JsonHelper.getString(jo, "type"));
        Optional<TaskFactory> contentFactory = CCPacksRegistries.TASK_FACTORY.getOrEmpty(factoryId);
        if (contentFactory.isPresent()) {
            if (CCPacksRegistries.TASK_FACTORY.containsId(factoryId)) {
                contentFactory = CCPacksRegistries.TASK_FACTORY.getOrEmpty(factoryId);
                TaskFactory.Instance factoryInstance = contentFactory.get().read(jo);
                TaskType type = taskFactory.apply(id, factoryInstance);
                if (!TaskRegistry.contains(id)) {
                    TaskRegistry.register(id, type);
                }
            }
        }
    }

}
