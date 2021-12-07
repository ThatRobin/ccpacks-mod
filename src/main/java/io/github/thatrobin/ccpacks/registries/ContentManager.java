package io.github.thatrobin.ccpacks.registries;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.github.thatrobin.ccpacks.factories.content_factories.ContentTypes;
import io.github.thatrobin.ccpacks.factories.content_factories.ContentTypesClient;
import io.github.thatrobin.ccpacks.util.DataLoader;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

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
