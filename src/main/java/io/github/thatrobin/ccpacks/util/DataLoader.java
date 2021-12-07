package io.github.thatrobin.ccpacks.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;

import java.util.*;

public class DataLoader {

    private final Gson gson;
    public final String dataType;

    public DataLoader(Gson gson, String dataType) {
        this.gson = gson;
        this.dataType = dataType;
    }

    public void apply(Map<Identifier, JsonObject> map) {
    }

}
