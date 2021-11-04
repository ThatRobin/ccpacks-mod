package io.github.ThatRobin.ccpacks.Util;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import io.github.ThatRobin.ccpacks.Registries.CCPacksRegistry;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
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
