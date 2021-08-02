package io.github.ThatRobin.ccpacks;

import io.github.ThatRobin.ccpacks.Commands.ItemActionCommand;
import io.github.ThatRobin.ccpacks.Commands.SetAttributeFromScoreboard;
import io.github.ThatRobin.ccpacks.Commands.SetFoodFromScoreboard;
import io.github.ThatRobin.ccpacks.Commands.SetHealthFromScoreboard;
import io.github.ThatRobin.ccpacks.serializableData.CCPackFactory;
import io.github.ThatRobin.ccpacks.util.UniversalPowerManager;
import io.github.apace100.apoli.util.NamespaceAlias;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CCPacksMain implements ModInitializer {

	public static final Logger LOGGER = LogManager.getLogger(CCPacksMain.class);;
	public static final String MODID = "ccpacks";

	public static final ResourcePackSource RESOURCE_PACK_SOURCE = ResourcePackSource.nameAndSource("pack.source.ccpacks");
	public static EntityType EXAMPLE_PROJECTILE;

	@Override
	public void onInitialize() {
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new UniversalPowerManager());
		NamespaceAlias.addAlias(MODID, "apoli");
		NamespaceAlias.addAlias("origins", "apoli");
		CCPackFactory.register();
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			SetAttributeFromScoreboard.register(dispatcher);
			SetFoodFromScoreboard.register(dispatcher);
			SetHealthFromScoreboard.register(dispatcher);
			ItemActionCommand.register(dispatcher);
		});
	}

	public static Identifier identifier(String path) {
		return new Identifier(MODID, path);
	}
}
