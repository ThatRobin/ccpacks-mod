package io.github.connor3001.ccpacks;

import com.google.common.collect.ImmutableList;
import dev.emi.trinkets.api.client.TrinketRenderer;
import dev.emi.trinkets.data.SlotLoader;
import io.github.apace100.apoli.power.PowerTypeReference;
import io.github.connor3001.ccpacks.SerializableData.CCPackFactory;
import io.github.connor3001.ccpacks.customContent.DirCCPackRegistry;
import io.github.connor3001.ccpacks.customContent.ZipCCPackRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.world.WorldTickCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
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
				if(fileArray[i].getName().endsWith(".zip")) {
					new ZipCCPackRegistry(fileArray[i], new ZipFile(fileArray[i]));
				} else if(fileArray[i].isDirectory()) {
					new DirCCPackRegistry(fileArray[i]);
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
