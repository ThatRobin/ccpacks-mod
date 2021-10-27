package io.github.ThatRobin.ccpacks;

import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import io.github.ThatRobin.ccpacks.Choice.Choice;
import io.github.ThatRobin.ccpacks.Choice.ChoiceLayers;
import io.github.ThatRobin.ccpacks.Choice.ChoiceManager;
import io.github.ThatRobin.ccpacks.Commands.ChoiceCommand;
import io.github.ThatRobin.ccpacks.Commands.ItemActionCommand;
import io.github.ThatRobin.ccpacks.Commands.SetCommand;
import io.github.ThatRobin.ccpacks.Component.ItemHolderComponent;
import io.github.ThatRobin.ccpacks.Component.ItemHolderComponentImpl;
import io.github.ThatRobin.ccpacks.Factories.ContentFactories.*;
import io.github.ThatRobin.ccpacks.Factories.*;
import io.github.ThatRobin.ccpacks.Networking.CCPacksModPacketC2S;
import io.github.ThatRobin.ccpacks.Power.PowerIconManager;
import io.github.ThatRobin.ccpacks.Registries.CCPacksRegistry;
import io.github.ThatRobin.ccpacks.Util.UniversalPowerManager;
import io.github.apace100.apoli.util.NamespaceAlias;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.resource.ModResourcePack;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.impl.resource.loader.ModResourcePackUtil;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CCPacksMain implements ModInitializer, EntityComponentInitializer {

	public static final Logger LOGGER = LogManager.getLogger(CCPacksMain.class);
	public static final String MODID = "ccpacks";

	public static CCPacksRegistry ccPacksRegistry = new CCPacksRegistry();

	public static PowerIconManager powerIconManager = new PowerIconManager();;

	@Override
	public void onInitialize() {
		Choice.init();

		NamespaceAlias.addAlias(MODID, "apoli");
		NamespaceAlias.addAlias("origins", "apoli");

		EntityActions.register();
		EntityConditions.register();
		ItemActions.register();
		ItemConditions.register();
		PowerFactories.register();

		BlockFactories.register();
		EnchantmentFactories.register();
		ItemFactories.register();
		KeybindFactories.register();
		ParticleFactories.register();
		PortalFactories.register();
		ProjectileFactories.register();
		SoundEventFactories.register();
		StatusEffectFactories.register();

		ccPacksRegistry.registerResources();

		List<ModResourcePack> packs = new ArrayList<>();
		ModResourcePackUtil.appendModResourcePacks(packs, ResourceType.SERVER_DATA, null);
		packs.forEach(modResourcePack -> modResourcePack.getNamespaces(ResourceType.SERVER_DATA).forEach(s -> {
			try {
				ccPacksRegistry.registerMods(modResourcePack, modResourcePack.findResources(ResourceType.SERVER_DATA, s, "", 60, Objects::nonNull));
			} catch (IOException e) {
				LOGGER.error(e.getMessage());
			}
		}));

		ccPacksRegistry.getMap().forEach(ContentTypes::new);
		if(FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			CCPacksMain.ccPacksRegistry.getMap().forEach(ContentTypesClient::new);
		}

		CCPacksModPacketC2S.register();
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			SetCommand.register(dispatcher);
			ItemActionCommand.register(dispatcher);
			ChoiceCommand.register(dispatcher);
		});

		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new UniversalPowerManager());
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new ChoiceManager());
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new ChoiceLayers());
	}

	public static Identifier identifier(String path) {
		return new Identifier(MODID, path);
	}

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.beginRegistration(LivingEntity.class, ItemHolderComponent.KEY)
				.impl(ItemHolderComponentImpl.class)
				.respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY)
				.end(ItemHolderComponentImpl::new);
	}
}
