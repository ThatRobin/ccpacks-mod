package io.github.thatrobin.ccpacks;

import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import io.github.apace100.calio.util.OrderedResourceListeners;
import io.github.thatrobin.ccpacks.choice.Choice;
import io.github.thatrobin.ccpacks.choice.ChoiceLayers;
import io.github.thatrobin.ccpacks.choice.ChoiceManager;
import io.github.thatrobin.ccpacks.commands.*;
import io.github.thatrobin.ccpacks.component.ItemHolderComponent;
import io.github.thatrobin.ccpacks.component.ItemHolderComponentImpl;
import io.github.thatrobin.ccpacks.factories.content_factories.*;
import io.github.thatrobin.ccpacks.factories.*;
import io.github.thatrobin.ccpacks.factories.mechanic_factories.MechanicFactories;
import io.github.thatrobin.ccpacks.networking.CCPacksModPacketC2S;
import io.github.thatrobin.ccpacks.power.PowerIconManager;
import io.github.thatrobin.ccpacks.registries.CCPacksRegistry;
import io.github.thatrobin.ccpacks.registries.ContentManager;
import io.github.thatrobin.ccpacks.registries.MechanicManager;
import io.github.thatrobin.ccpacks.util.ItemGroupHolder;
import io.github.thatrobin.ccpacks.util.OnLoadResourceManager;
import io.github.thatrobin.ccpacks.util.UniversalPowerManager;
import io.github.apace100.apoli.util.NamespaceAlias;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

public class CCPacksMain implements ModInitializer, EntityComponentInitializer {

	public static int[] SEMVER;
	public static String VERSION = "";
	public static final Logger LOGGER = LogManager.getLogger(CCPacksMain.class);
	public static final String MODID = "ccpacks";

	public static CCPacksRegistry ccPacksRegistry = new CCPacksRegistry();

	public static PowerIconManager powerIconManager = new PowerIconManager();

	@Override
	public void onInitialize() {
		FabricLoader.getInstance().getModContainer(MODID).ifPresent(modContainer -> {
			VERSION = modContainer.getMetadata().getVersion().getFriendlyString();
			if(VERSION.contains("+")) {
				VERSION = VERSION.split("\\+")[0];
			}
			if(VERSION.contains("-")) {
				VERSION = VERSION.split("-")[0];
			}
			String[] splitVersion = VERSION.split("\\.");
			SEMVER = new int[splitVersion.length];
			for(int i = 0; i < SEMVER.length; i++) {
				SEMVER[i] = Integer.parseInt(splitVersion[i]);
			}
		});
		GeckoLib.initialize();
		Choice.init();

		NamespaceAlias.addAlias(MODID, "apoli");
		NamespaceAlias.addAlias("origins", "apoli");
		EntityActions.register();
		EntityConditions.register();
		ItemConditions.register();
		BlockActions.register();
		BlockConditions.register();
		PowerFactories.register();


		MechanicFactories.register();

		// Custom Content
		BlockFactories.register();
		EnchantmentFactories.register();
		ItemFactories.register();
		ItemGroupFactories.register();
		FeatureFactories.register();
		KeybindFactories.register();
		ParticleFactories.register();
		PortalFactories.register();
		ProjectileFactories.register();
		SoundEventFactories.register();
		StatusEffectFactories.register();

		CCPacksModPacketC2S.register();

		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			MechanicCommand.register(dispatcher);
			SetCommand.register(dispatcher);
			ItemActionCommand.register(dispatcher);
			ChoiceCommand.register(dispatcher);
		});

		OnLoadResourceManager.addSingleListener(new ContentManager());

		//ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new CollisionManager());
		OrderedResourceListeners.register(new UniversalPowerManager()).after(new Identifier("apoli","powers")).complete();
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new MechanicManager());
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new ChoiceManager());
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new ChoiceLayers());

		ArgumentTypes.register("ccpacks:choice_layer", LayerArgument.class, new ConstantArgumentSerializer<>(LayerArgument::layer));
		ArgumentTypes.register("ccpacks:mechanic", MechanicArgument.class, new ConstantArgumentSerializer<>(MechanicArgument::mechanic));

		ContentTypes.itemGroups.forEach((identifier, itemGroupHolder) -> {
			if(itemGroupHolder.getItemGroup() instanceof OwoItemGroup owoItemGroup) {
				owoItemGroup.initialize();
			}
		});
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
