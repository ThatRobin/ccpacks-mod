package io.github.ThatRobin.ccpacks;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.ThatRobin.ccpacks.SerializableData.SerializableObjects;
import io.github.ThatRobin.ccpacks.Util.TestParticle;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.DDEntity;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.DDEntityRenderer;
import io.github.apace100.calio.data.SerializableData;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.render.entity.SlimeEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Environment(EnvType.CLIENT)
public class CCPacksClient implements ClientModInitializer {

    private List<Pair<String, JsonObject>> list = new ArrayList<>();

    //public static DefaultParticleType TEST;

    @Override
    public void onInitializeClient() {
        //TEST = Registry.register(Registry.PARTICLE_TYPE, "atg:test", FabricParticleTypes.simple(true));
        //ParticleFactoryRegistry.getInstance().register(TEST, TestParticle.Factory::new);
        try {
            File[] fileArray = CCPacksMain.DATAPACKS_PATH.toFile().listFiles();
            for(int i = 0; i < fileArray.length; i++){
                if(fileArray[i].isDirectory()) {
                    readFromDir(fileArray[i], null);
                } else if(fileArray[i].getName().endsWith(".zip")) {
                    readFromZip(fileArray[i], new ZipFile(fileArray[i]));
                } else {
                    continue;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        register(list);
    }

    private void register(List<Pair<String, JsonObject>> list){
        for(int i = 0; i < list.size(); i++){
            String type = list.get(i).getLeft();
            JsonObject jsonObject = list.get(i).getRight();
            SerializableData.Instance instance2;
            if(type.equals("ccpacks:animal_entity")) {
                instance2 = SerializableObjects.animalEntityData.read(jsonObject);

                EntityRendererRegistry.INSTANCE.register((EntityType<DDEntity>)(Registry.ENTITY_TYPE.get(instance2.getId("identifier"))), (context) -> new DDEntityRenderer(context, instance2.getId("texture")));
            }
        }
    }

    public void registerElements(JsonObject jsonObject, SerializableData.Instance instance){
        Pair<String, JsonObject> pair = new Pair<>(instance.getString("type"), jsonObject);
        list.add(pair);
    }

    public void readFromDir(File base, ZipFile zipFile) throws IOException {
        String string2 = "ccdata/";
        File pack = new File(base, "ccdata");
        try (Stream<Path> paths = Files.walk(Paths.get(pack.getPath()))) {
            paths.forEach((file) -> {
                String string3 = file.toString();
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
                        jsonObject = (JsonObject)jsonParser.parse(new InputStreamReader(stream, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    SerializableData.Instance instance;
                    instance = SerializableObjects.getItemType.read(jsonObject);
                    registerElements(jsonObject, instance);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ZipFile getZipFile(File base, ZipFile zipFile) throws IOException {
        if (zipFile == null) {
            zipFile = new ZipFile(base);
        }
        return zipFile;
    }

    public void readFromZip(File base, ZipFile zipFile) throws IOException {
        ZipFile zipFile2 = this.getZipFile(base, zipFile);
        Enumeration<? extends ZipEntry> enumeration = zipFile2.entries();
        String string2 = "ccdata/";
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
                    SerializableData.Instance instance = SerializableObjects.getItemType.read(jsonObject);
                    registerElements(jsonObject, instance);
                }
            }
        }
    }
}
