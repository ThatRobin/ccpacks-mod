package io.github.thatrobin.ccpacks.registries;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.github.thatrobin.ccpacks.factories.task_factories.TaskTypes;
import io.github.thatrobin.ccpacks.util.DataLoader;
import net.minecraft.util.Identifier;

import java.util.Map;

public class TaskManager extends DataLoader {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    public TaskManager() {
        super(GSON, "mob_tasks");
    }

    @Override
    public void apply(Map<Identifier, JsonObject> map) {
        map.forEach(TaskTypes::new);
    }

}
