package io.github.connor3001.ccpacks;

import io.github.connor3001.ccpacks.SerializableData.CCPackFactory;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.ZipFile;

public class CCPacksMain implements ModInitializer {

	public static final Logger LOGGER = LogManager.getLogger(CCPacksMain.class);;
	public static final String MODID = "ccpacks";

	public static final ResourcePackSource RESOURCE_PACK_SOURCE = ResourcePackSource.nameAndSource("pack.source.ccpacks");
	public static final Path DATAPACKS_PATH = MinecraftClient.getInstance().runDirectory.toPath().resolve("resourcepacks");

	@Override
	public void onInitialize() {
		CCPackFactory.register();
		try {
			File[] fileArray = DATAPACKS_PATH.toFile().listFiles();
			for(int i = 0; i < fileArray.length; i++){
				if(fileArray[i].isDirectory()) {
					new CCPackRegistry(true, fileArray[i], null);
				} else if(fileArray[i].getName().endsWith(".zip")) {
					new CCPackRegistry(false, fileArray[i], new ZipFile(fileArray[i]));
				} else {
					continue;
                }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Identifier identifier(String path) {
		return new Identifier(MODID, path);
	}
}
