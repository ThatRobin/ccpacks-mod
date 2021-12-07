package io.github.thatrobin.ccpacks.choice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.thatrobin.ccpacks.CCPacksMain;
import io.github.apace100.calio.data.MultiJsonDataLoader;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

import java.util.*;
import java.util.stream.Collectors;

public class ChoiceLayers extends MultiJsonDataLoader implements IdentifiableResourceReloadListener {

    private static final HashMap<Identifier, ChoiceLayer> layers = new HashMap<>();
    private static int minLayerPriority = Integer.MIN_VALUE;

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    public ChoiceLayers() {
        super(GSON, "choice_layers");
    }

    @Override
    protected void apply(Map<Identifier, List<JsonElement>> loader, ResourceManager manager, Profiler profiler) {
        clear();
        HashMap<Identifier, HashMap<Integer, List<JsonObject>>> layers = new HashMap<>();

        loader.forEach((id, jel) -> {
            minLayerPriority = Integer.MIN_VALUE;
            jel.forEach(je -> {
                try {
                    CCPacksMain.LOGGER.info("Trying to read layer file: " + id);
                    JsonObject jo = je.getAsJsonObject();
                    boolean replace = JsonHelper.getBoolean(jo, "replace", false);
                    int priority = JsonHelper.getInt(jo, "loading_priority", 0);
                    if(priority >= minLayerPriority) {
                        HashMap<Integer, List<JsonObject>> inner = layers.computeIfAbsent(id, ident -> new HashMap<>());
                        List<JsonObject> layerList = inner.computeIfAbsent(priority, prio -> new LinkedList<>());
                        if(replace) {
                            layerList.clear();
                            minLayerPriority = priority + 1;
                        }
                        layerList.add(jo);
                    }
                } catch (Exception e) {
                    CCPacksMain.LOGGER.error("There was a problem reading Choice layer file " + id.toString() + " (skipping): " + e.getMessage());
                }
            });
        });
        // Merge phase
        for (Map.Entry<Identifier, HashMap<Integer, List<JsonObject>>> layerToLoad : layers.entrySet()) {
            Identifier layerId = layerToLoad.getKey();
            List<Integer> keys = layerToLoad.getValue().keySet().stream().sorted().collect(Collectors.toList());
            ChoiceLayer layer = null;
            for(Integer key : keys) {
                for(JsonObject jo : layerToLoad.getValue().get(key)) {
                    if(layer == null) {
                        layer = ChoiceLayer.createFromData(layerId, ChoiceLayer.DATA.read(jo));
                    } else {
                        layer.merge(ChoiceLayer.DATA.read(jo));
                    }
                }
            }
            add(layer);
        }
        CCPacksMain.LOGGER.info("Finished loading choice layers from data files. Read " + layers.size() + " layers.");
    }

    public static ChoiceLayer getLayer(Identifier id) {
        return layers.get(id);
    }

    public static List<ChoiceLayer> getLayers() {
        return layers.values().stream().toList();
    }

    public static int size() {
        return layers.size();
    }

    public static void clear() {
        layers.clear();
    }

    public static void add(ChoiceLayer layer) {
        layers.put(layer.getIdentifier(), layer);
    }

    @Override
    public Identifier getFabricId() {
        return new Identifier(CCPacksMain.MODID, "choice_layers");
    }
}
