package io.github.ThatRobin.ccpacks.Registries;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.ThatRobin.ccpacks.Factories.MechanicFactories.MechanicFactory;
import io.github.ThatRobin.ccpacks.Factories.MechanicFactories.MechanicRegistry;
import io.github.ThatRobin.ccpacks.Factories.MechanicFactories.MechanicType;
import io.github.ThatRobin.ccpacks.Factories.TaskFactories.TaskTypes;
import io.github.ThatRobin.ccpacks.Util.DataLoader;
import io.github.ThatRobin.ccpacks.Util.Mechanic;
import io.github.ThatRobin.ccpacks.Util.UniversalPower;
import io.github.apace100.calio.data.MultiJsonDataLoader;
import io.github.apace100.calio.data.SerializableData;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

public class MechanicManager extends MultiJsonDataLoader implements IdentifiableResourceReloadListener {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    public MechanicManager() {
        super(GSON, "mechanics");
    }

    @Override
    protected void apply(Map<Identifier, List<JsonElement>> prepared, ResourceManager manager, Profiler profiler) {
        MechanicRegistry.reset();
        prepared.forEach((id, jel) -> {
            for (JsonElement je : jel) {
                try {
                    readPower(id, je);
                } catch (Exception e) {
                    CCPacksMain.LOGGER.error("There was a problem reading mechanic file " + id.toString() + " (skipping): " + e.getMessage());
                }
            }
        });
    }

    private void readPower(Identifier id, JsonElement je) {
        readPower(id, je, MechanicType::new);
    }

    private void readPower(Identifier id, JsonElement je, BiFunction<Identifier, MechanicFactory<Mechanic>.Instance, MechanicType> taskFactory) {
        JsonObject jo = je.getAsJsonObject();
        Identifier factoryId = Identifier.tryParse(JsonHelper.getString(jo, "type"));
        Optional<MechanicFactory> contentFactory = CCPacksRegistries.MECHANIC_FACTORY.getOrEmpty(factoryId);
        if (contentFactory.isPresent()) {
            if (CCPacksRegistries.MECHANIC_FACTORY.containsId(factoryId)) {
                contentFactory = CCPacksRegistries.MECHANIC_FACTORY.getOrEmpty(factoryId);
                MechanicFactory.Instance factoryInstance = contentFactory.get().read(jo);
                MechanicType type = taskFactory.apply(id, factoryInstance);
                if (!MechanicRegistry.contains(id)) {
                    MechanicRegistry.register(id, type);
                }
            }
        }
    }

    @Override
    public Identifier getFabricId() {
        return CCPacksMain.identifier("mechanic_factory");
    }
}
