package io.github.thatrobin.ccpacks.registries;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.thatrobin.ccpacks.CCPacksMain;
import io.github.thatrobin.ccpacks.util.DataLoader;
import net.fabricmc.fabric.api.resource.ModResourcePack;
import net.fabricmc.fabric.impl.resource.loader.ModResourcePackUtil;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

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


    public static Path MODS_PATH = FabricLoader.getInstance().getGameDirectory().toPath().resolve("mods");
    private String path;
    public Map<Identifier, JsonObject> map;


    public void registerResources(DataLoader loader, String path) {
        this.path = path;
        map = Maps.newHashMap();
        try {
            File[] fileArray = MODS_PATH.toFile().listFiles();
            if(fileArray != null) {
                for (File file : fileArray) {
                    if (file.isDirectory()) {
                        readFromDir(file);
                    }
                }
            }
            List<ModResourcePack> packs = new ArrayList<>();
            ModResourcePackUtil.appendModResourcePacks(packs, ResourceType.SERVER_DATA, null);
            packs.forEach(modResourcePack -> modResourcePack.getNamespaces(ResourceType.SERVER_DATA).forEach(s -> {
                try {
                    registerMods(modResourcePack, modResourcePack.findResources(ResourceType.SERVER_DATA, s, "", 60, Objects::nonNull));
                } catch (IOException e) {
                    CCPacksMain.LOGGER.error(e.getMessage());
                }
            }));
        } catch (IOException e) {
            e.printStackTrace();
        }
        loader.apply(map);
    }

    public void registerMods(ModResourcePack modResourcePack, Collection<Identifier> identifiers) throws IOException {
        identifiers.forEach(file -> {
            try {
                Identifier id = new Identifier(file.toString().split(":")[0], file.toString().split(":")[1]);
                if (id.getPath().startsWith(this.path)) {
                    InputStream s = modResourcePack.open(ResourceType.SERVER_DATA, id);
                    JsonObject jsonObject = (JsonObject) JsonParser.parseReader(new InputStreamReader(s, StandardCharsets.UTF_8));
                    String namespace = file.getNamespace();
                    int index = file.getPath().replace("\\","/").split("/").length;
                    String path = (file.getPath().replace("\\","/").split("/")[index-1]).replace(".json","");
                    Identifier id2 = new Identifier(namespace, path);
                    map.put(id2, jsonObject);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void readFromDir(File base) throws IOException {
        File pack = new File(base, "data");
        if(pack.exists()) {
            try {
                Stream<Path> paths = Files.walk(Paths.get(pack.getPath()));
                paths.forEach((file) -> {
                    String string3 = file.toString();
                    boolean isFound = string3.contains(this.path);
                    if(isFound) {
                        if (string3.endsWith(".json")) {
                            InputStream stream = null;
                            try {
                                stream = new FileInputStream(string3);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            assert stream != null;
                            JsonObject jsonObject = (JsonObject) JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
                            String namespace = string3.split(this.path)[0].split("data")[1].replace("\\", "");
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

}
