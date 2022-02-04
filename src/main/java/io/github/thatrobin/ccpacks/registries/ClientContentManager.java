package io.github.thatrobin.ccpacks.registries;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.github.thatrobin.ccpacks.factories.content_factories.ContentTypesClient;
import io.github.thatrobin.ccpacks.util.DataLoader;
import net.minecraft.util.Identifier;

import java.util.Map;

public class ClientContentManager extends DataLoader {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    public ClientContentManager() {
        super(GSON, "content");
    }

    @Override
    public void apply(Map<Identifier, JsonObject> map) {
        map.forEach(ContentTypesClient::new);
    }

}
