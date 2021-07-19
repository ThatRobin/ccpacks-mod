package io.github.ThatRobin.ccpacks;

import io.github.ThatRobin.ccpacks.SerializableData.CCPackFactory;
import io.github.apace100.apoli.util.NamespaceAlias;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CCPacksMain implements ModInitializer {

	public static final Logger LOGGER = LogManager.getLogger(CCPacksMain.class);;
	public static final String MODID = "ccpacks";

	public static final ResourcePackSource RESOURCE_PACK_SOURCE = ResourcePackSource.nameAndSource("pack.source.ccpacks");

	@Override
	public void onInitialize() {
		NamespaceAlias.addAlias(MODID, "apoli");
		NamespaceAlias.addAlias("origins", "apoli");
		CCPackFactory.register();
	}

	public static Identifier identifier(String path) {
		return new Identifier(MODID, path);
	}
}
