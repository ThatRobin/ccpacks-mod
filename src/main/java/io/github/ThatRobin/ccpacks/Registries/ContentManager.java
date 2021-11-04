package io.github.ThatRobin.ccpacks.Registries;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.ThatRobin.ccpacks.Factories.ContentFactories.ContentTypes;
import io.github.ThatRobin.ccpacks.Factories.ContentFactories.ContentTypesClient;
import io.github.ThatRobin.ccpacks.Util.DataLoader;
import io.github.ThatRobin.ccpacks.Util.SingleTimeResourceLoader;
import io.github.ThatRobin.ccpacks.Util.UniversalPower;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.util.List;
import java.util.Map;

public class ContentManager extends DataLoader {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    public ContentManager() {
        super(GSON, "content");
    }

    @Override
    public void apply(Map<Identifier, JsonObject> map) {
        map.forEach(ContentTypes::new);
        if(FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            map.forEach(ContentTypesClient::new);
        }
    }

}
