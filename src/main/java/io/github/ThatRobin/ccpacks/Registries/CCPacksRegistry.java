package io.github.ThatRobin.ccpacks.Registries;

import com.google.common.collect.Maps;
import com.google.gson.*;
import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.apace100.calio.data.SerializableData;
import net.fabricmc.fabric.api.resource.ModResourcePack;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.resource.*;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Pair;
import net.minecraft.util.profiler.Profiler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class CCPacksRegistry {

    public Map<Identifier, JsonObject> map = Maps.newHashMap();
    public static final Path DATAPACKS_PATH = FabricLoader.getInstance().getGameDirectory().toPath().resolve("resourcepacks");
    private static final int FILE_SUFFIX_LENGTH = ".json".length();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private final HashMap<Identifier, Integer> loadingPriorities = new HashMap<>();


    public void registerResources() {
        try {
            File[] fileArray = DATAPACKS_PATH.toFile().listFiles();
            CCPacksMain.LOGGER.info("Types to Register:");
            if(fileArray != null) {
                for (int i = 0; i < fileArray.length; i++) {
                    if (fileArray[i].isDirectory()) {
                        readFromDir(fileArray[i], null);
                    } else if (fileArray[i].getName().endsWith(".zip")) {
                        readFromZip(fileArray[i], new ZipFile(fileArray[i]));
                    } else {
                        continue;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerMods(ModResourcePack modResourcePack, Collection<Identifier> identifiers) throws IOException {
        identifiers.forEach(file -> {
            try {
                Identifier id = new Identifier(file.toString().split(":")[0], file.toString().split(":")[1]);
                if (id.getPath().startsWith("content")) {
                    InputStream s = modResourcePack.open(ResourceType.SERVER_DATA, id);
                    JsonParser jsonParser = null;
                    jsonParser = new JsonParser();
                    JsonObject jsonObject = null;
                    try {
                        jsonObject = (JsonObject) jsonParser.parse(new InputStreamReader(s, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String namespace = file.getNamespace();
                    String path = file.getPath().replace("\\","/").split("/")[-1];
                    Identifier id2 = new Identifier(namespace, path);
                    map.put(id2, jsonObject);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void readFromDir(File base, ZipFile zipFile) throws IOException {
        File pack = new File(base, "data");
        if(pack.exists()) {
            try (Stream<Path> paths = Files.walk(Paths.get(pack.getPath()))) {
                paths.forEach((file) -> {
                    String string3 = file.toString();
                    boolean isFound = string3.indexOf("content") !=-1? true: false;
                    if(isFound) {
                        if (string3.endsWith(".json")) {
                            InputStream stream = null;
                            try {
                                stream = new FileInputStream(string3);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            JsonParser jsonParser = null;
                            jsonParser = new JsonParser();
                            JsonObject jsonObject = null;
                            try {
                                jsonObject = (JsonObject) jsonParser.parse(new InputStreamReader(stream, "UTF-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            String namespace = string3.split("content")[0].split("data")[1].replace("\\", "");
                            String path = file.getFileName().toString().split(".json")[0];
                            Identifier id = new Identifier(namespace, path);
                            map.put(id, jsonObject);
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private ZipFile getZipFile(File base, ZipFile zipFile) throws IOException {
        if (zipFile == null) {
            zipFile = new ZipFile(base);
        }
        return zipFile;
    }

    public Map<Identifier,JsonObject> getMap() {
        return this.map;
    }

    public void readFromZip(File base, ZipFile zipFile) throws IOException {
        ZipFile zipFile2 = this.getZipFile(base, zipFile);
        Enumeration<? extends ZipEntry> enumeration = zipFile2.entries();
        String string2 = "data/";
        while(enumeration.hasMoreElements()) {
            ZipEntry zipEntry = enumeration.nextElement();
            if (!zipEntry.isDirectory()) {
                String string3 = zipEntry.getName();
                if (string3.endsWith(".json") && string3.startsWith(string2)) {
                    InputStream stream = zipFile2.getInputStream(zipEntry);
                    JsonParser jsonParser = new JsonParser();
                    JsonObject jsonObject = null;
                    try {
                        jsonObject = (JsonObject)jsonParser.parse(new InputStreamReader(stream, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    CCPacksMain.LOGGER.info(string3);
                    String namespace = string3.split("content")[0].split("data")[1].replace("\\","");
                    String path = string3.split(".json")[0];
                    Identifier id = new Identifier(namespace, path);
                    map.put(id, jsonObject);
                }
            }
        }
    }

}
