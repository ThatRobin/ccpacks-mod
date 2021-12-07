package io.github.thatrobin.ccpacks.choice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import io.github.thatrobin.ccpacks.CCPacksMain;
import io.github.apace100.calio.data.MultiJsonDataLoader;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.util.List;
import java.util.Map;

public class ChoiceManager extends MultiJsonDataLoader implements IdentifiableResourceReloadListener {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    public ChoiceManager() {
        super(GSON, "choices");
    }

    @Override
    protected void apply(Map<Identifier, List<JsonElement>> loader, ResourceManager manager, Profiler profiler) {
        ChoiceRegistry.reset();
        loader.forEach((id, jel) -> jel.forEach(je -> {
            try {
                Choice choice = Choice.fromJson(id, je.getAsJsonObject());
                if(!ChoiceRegistry.contains(id)) {
                    ChoiceRegistry.register(id, choice);
                }
            } catch(Exception e) {
                CCPacksMain.LOGGER.error("There was a problem reading Choice file " + id.toString() + " (skipping): " + e.getMessage());
            }
        }));
        CCPacksMain.LOGGER.info("Finished loading choices from data files. Registry contains " + ChoiceRegistry.size() + " choices.");
    }

    @Override
    public Identifier getFabricId() {
        return new Identifier(CCPacksMain.MODID, "ccpacks");
    }
}