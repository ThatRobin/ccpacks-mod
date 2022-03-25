package io.github.thatrobin.ccpacks.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import io.github.thatrobin.ccpacks.CCPacksMain;
import io.github.thatrobin.ccpacks.registries.UniversalPowerRegistry;
import io.github.apace100.calio.data.MultiJsonDataLoader;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.util.List;
import java.util.Map;

public class UniversalPowerManager extends MultiJsonDataLoader implements IdentifiableResourceReloadListener {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    public UniversalPowerManager() {
        super(GSON, "universal_powers");
    }

    @Override
    public Identifier getFabricId() {
        return new Identifier(CCPacksMain.MODID,"universal_powers");
    }

    @Override
    protected void apply(Map<Identifier, List<JsonElement>> prepared, ResourceManager manager, Profiler profiler) {
        UniversalPowerRegistry.reset();
        prepared.forEach((id, jel) -> jel.forEach(je -> {
            try {
                UniversalPower universalPower = UniversalPower.fromJson(id, je.getAsJsonObject());
                if(!UniversalPowerRegistry.contains(id)) {
                    UniversalPowerRegistry.register(id, universalPower);
                }
            } catch(Exception e) {
                CCPacksMain.LOGGER.error("There was a problem reading a Universal Powers file: " + id.toString() + " (skipping): " + e.getMessage());
            }
        }));
        CCPacksMain.LOGGER.info("Finished loading powers from data files. Registry contains " + UniversalPowerRegistry.size() + " Universal Power files.");
    }
}
