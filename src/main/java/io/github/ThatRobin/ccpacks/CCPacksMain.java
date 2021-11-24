package io.github.ThatRobin.ccpacks;

import dev.onyxstudios.cca.api.v3.block.BlockComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.block.BlockComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import io.github.ThatRobin.ccpacks.Choice.Choice;
import io.github.ThatRobin.ccpacks.Choice.ChoiceLayers;
import io.github.ThatRobin.ccpacks.Choice.ChoiceManager;
import io.github.ThatRobin.ccpacks.Commands.ChoiceCommand;
import io.github.ThatRobin.ccpacks.Commands.ItemActionCommand;
import io.github.ThatRobin.ccpacks.Commands.MechanicCommand;
import io.github.ThatRobin.ccpacks.Commands.SetCommand;
import io.github.ThatRobin.ccpacks.Component.BlockMechanicHolder;
import io.github.ThatRobin.ccpacks.Component.BlockMechanicHolderImpl;
import io.github.ThatRobin.ccpacks.Component.ItemHolderComponent;
import io.github.ThatRobin.ccpacks.Component.ItemHolderComponentImpl;
import io.github.ThatRobin.ccpacks.DataDrivenClasses.Blocks.DDBlockEntity;
import io.github.ThatRobin.ccpacks.DataDrivenClasses.Entities.DDAnimalEntity;
import io.github.ThatRobin.ccpacks.Factories.ContentFactories.*;
import io.github.ThatRobin.ccpacks.Factories.*;
import io.github.ThatRobin.ccpacks.Factories.MechanicFactories.MechanicFactories;
import io.github.ThatRobin.ccpacks.Factories.TaskFactories.TaskFactories;
import io.github.ThatRobin.ccpacks.Networking.CCPacksModPacketC2S;
import io.github.ThatRobin.ccpacks.Power.PowerIconManager;
import io.github.ThatRobin.ccpacks.Registries.CCPacksRegistry;
import io.github.ThatRobin.ccpacks.Registries.ContentManager;
import io.github.ThatRobin.ccpacks.Registries.MechanicManager;
import io.github.ThatRobin.ccpacks.Registries.TaskManager;
import io.github.ThatRobin.ccpacks.Util.DataLoader;
import io.github.ThatRobin.ccpacks.Util.OnLoadResourceManager;
import io.github.ThatRobin.ccpacks.Util.UniversalPowerManager;
import io.github.apace100.apoli.util.NamespaceAlias;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.resource.ModResourcePack;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.impl.resource.loader.ModResourcePackUtil;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.example.EntityUtils;
import software.bernie.geckolib3.GeckoLib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CCPacksMain implements ModInitializer, EntityComponentInitializer {

	public static final Logger LOGGER = LogManager.getLogger(CCPacksMain.class);
	public static final String MODID = "ccpacks";

	public static CCPacksRegistry ccPacksRegistry = new CCPacksRegistry();

	public static PowerIconManager powerIconManager = new PowerIconManager();;

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
		PowerFactories.register();


		MechanicFactories.register();

		// Mob Behaviours

		//TaskFactories.register();

		// Custom Content
		BlockFactories.register();
		EnchantmentFactories.register();
		//EntityFactories.register();
		ItemFactories.register();
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
