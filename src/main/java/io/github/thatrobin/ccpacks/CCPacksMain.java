package io.github.thatrobin.ccpacks;

import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import io.github.apace100.calio.util.ClientTagManagerGetter;
import io.github.thatrobin.ccpacks.choice.Choice;
import io.github.thatrobin.ccpacks.choice.ChoiceLayers;
import io.github.thatrobin.ccpacks.choice.ChoiceManager;
import io.github.thatrobin.ccpacks.commands.ChoiceCommand;
import io.github.thatrobin.ccpacks.commands.ItemActionCommand;
import io.github.thatrobin.ccpacks.commands.MechanicCommand;
import io.github.thatrobin.ccpacks.commands.SetCommand;
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
import io.github.thatrobin.ccpacks.registries.TaskManager;
import io.github.thatrobin.ccpacks.util.ItemGroupHolder;
import io.github.thatrobin.ccpacks.util.OnLoadResourceManager;
import io.github.thatrobin.ccpacks.util.UniversalPowerManager;
import io.github.apace100.apoli.util.NamespaceAlias;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.resource.ResourceType;
import net.minecraft.tag.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

public class CCPacksMain implements ModInitializer, EntityComponentInitializer {

	public static final Logger LOGGER = LogManager.getLogger(CCPacksMain.class);
	public static final String MODID = "ccpacks";

	public static CCPacksRegistry ccPacksRegistry = new CCPacksRegistry();

	public static PowerIconManager powerIconManager = new PowerIconManager();

	@Override
	public void onInitialize() {
		GeckoLib.initialize();
		Choice.init();

		NamespaceAlias.addAlias(MODID, "apoli");
		NamespaceAlias.addAlias("origins", "apoli");
		EntityActions.register();
		EntityConditions.register();
		ItemActions.register();
		ItemConditions.register();
		BlockActions.register();
		BlockConditions.register();
		PowerFactories.register();


		MechanicFactories.register();

		// Mob Behaviours

		//TaskFactories.register();

		// Custom Content
		BlockFactories.register();
		EnchantmentFactories.register();
		//EntityFactories.register();
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

		OnLoadResourceManager.addSingleListener(new TaskManager());
		OnLoadResourceManager.addSingleListener(new ContentManager());

		//ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new CollisionManager());
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new MechanicManager());
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new UniversalPowerManager());
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new ChoiceManager());
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new ChoiceLayers());

		ContentTypes.itemGroups.forEach((identifier, itemGroupHolder) -> {
			itemGroupHolder.entrySet().forEach((entry) -> {
				ItemGroupHolder holder = entry.getValue();
				Identifier name = entry.getKey();
				FabricItemGroupBuilder.create(name)
						.icon(() -> Registry.ITEM.get(holder.icon).getDefaultStack())
						.appendItems((stacks) -> {
							stacks.addAll(holder.getHolder());
						}).build();
			});
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
