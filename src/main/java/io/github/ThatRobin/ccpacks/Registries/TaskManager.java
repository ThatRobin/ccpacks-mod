package io.github.ThatRobin.ccpacks.Registries;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.github.ThatRobin.ccpacks.Factories.ContentFactories.ContentTypes;
import io.github.ThatRobin.ccpacks.Factories.ContentFactories.ContentTypesClient;
import io.github.ThatRobin.ccpacks.Factories.TaskFactories.TaskTypes;
import io.github.ThatRobin.ccpacks.Util.DataLoader;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
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
