package io.github.ThatRobin.ccpacks.Util;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.ThatRobin.ccpacks.Screen.LoadingOverlay;
import io.github.ThatRobin.ccpacks.Screen.WriteFabricDataScreen;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.util.Util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ZipPack {

    public Path MODS_PATH = FabricLoader.getInstance().getGameDirectory().toPath().resolve("mods");
    private CCPackInfo selectedEntry;
    private MinecraftClient minecraftClient;
    private TitleScreen titleScreen;
    private File file;

    ZipUtility zipUtility = new ZipUtility();
    int total = 0;

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void zipPack(TitleScreen screen, MinecraftClient client) {
        minecraftClient = client;
        titleScreen = screen;
        try {
            File[] fileArray = MODS_PATH.toFile().listFiles();
            if (fileArray != null) {
                for (File value : fileArray) {
                    if (value.isDirectory()) {
                        zipFromDir(value);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void zipFromDir(File base) throws IOException {
        this.file = base;
        if (base.exists()) {
            try {
                boolean found = false;
                List<Path> paths = Files.list(Paths.get(base.getPath())).toList();
                for (Path path : paths) {
                    if (path.toFile().getName().contains("pack.mcmeta")) {
                        found = true;
                    }
                }
                if (found) {
                    selectedEntry = new CCPackInfo("Example Name", "0.0.1", "example_id", "Author", "Example Description", "All Rights Reserved");
                    if(!Files.exists(Path.of(this.file.getPath()+"/fabric.mod.json"))) {
                        minecraftClient.setScreen(new WriteFabricDataScreen(titleScreen, this::editEntry, selectedEntry, base.getName()));
                    } else {
                        compress(minecraftClient, titleScreen, this.file);
                    }
                }
            } catch (Exception e) {
                CCPacksMain.LOGGER.error(e);
            }
        }
    }

    public void compress(MinecraftClient client, TitleScreen titleScreen, File dirPath) throws IOException {
        final Path sourceDir = Paths.get(dirPath.getPath());
        LoadingOverlay overlay = new LoadingOverlay(client, true);

        Files.walk(sourceDir).forEach(path -> {
            if(!path.toFile().isDirectory()){
                total++;
            }
        });

        client.setOverlay(overlay);
        Util.getMainWorkerExecutor().execute(() -> {
            String zipFileName = dirPath.getPath().concat(".jar");
            try {
                List<File> files = Lists.newArrayList();
                Arrays.stream(sourceDir.toFile().listFiles()).toList().forEach(path -> {
                    files.add(path);
                });
                zipUtility.zip(files, zipFileName, overlay, total);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void editEntry(boolean confirmedAction) {
        if (confirmedAction) {
            try {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                Map<String, Object> submap = new HashMap<>();
                map.put("schemaVersion", 1);
                map.put("environment", "*");
                submap.put("fabric-api-base", "*");
                submap.put("fabric", "*");
                submap.put("minecraft", ">=1.17");
                submap.put("ccpacks", ">=1.0.0");
                map.put("depends", submap);
                map.put("authors", new String[]{selectedEntry.author});
                map.put("name", selectedEntry.name);
                map.put("id", selectedEntry.id);
                map.put("version", selectedEntry.version);
                map.put("description", selectedEntry.description);
                map.put("license", selectedEntry.license);

                FileWriter writer = new FileWriter(this.file.getPath() + "/fabric.mod.json");
                gson.toJson(map, writer);
                writer.close();
                compress(minecraftClient, titleScreen, this.file);
            } catch (IOException e) {
                CCPacksMain.LOGGER.error(e.getMessage());
            }
        }
        minecraftClient.setScreen(titleScreen);
    }
}