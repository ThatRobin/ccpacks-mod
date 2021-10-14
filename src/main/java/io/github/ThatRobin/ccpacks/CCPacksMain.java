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
import io.github.ThatRobin.ccpacks.Util.StairBlock;
import io.github.ThatRobin.ccpacks.Util.UniversalPowerManager;
import io.github.apace100.apoli.util.NamespaceAlias;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.resource.ModResourcePack;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.impl.resource.loader.ModResourcePackUtil;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CCPacksMain implements ModInitializer, EntityComponentInitializer {

	private String dataType = "content";

	public static final Logger LOGGER = LogManager.getLogger(CCPacksMain.class);;
	public static final String MODID = "ccpacks";

	public static Item FALLBACK_ITEM = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static Item FALLBACK_FOOD = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static Item FALLBACK_ARMOR_ITEM = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static Item FALLBACK_SWORD = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static Item FALLBACK_PICKAXE = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static Item FALLBACK_AXE = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static Item FALLBACK_SHOVEL = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static Item FALLBACK_HOE = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static Block FALLBACK_BLOCK = new Block(FabricBlockSettings.of(Material.WOOD));
	public static Block FALLBACK_FALLING_BLOCK = new FallingBlock(FabricBlockSettings.of(Material.WOOD));
	public static Block FALLBACK_STAIR = new StairBlock(Blocks.OAK_WOOD.getDefaultState(), FabricBlockSettings.of(Material.WOOD));
	public static Block FALLBACK_HSLAB = new SlabBlock(FabricBlockSettings.of(Material.WOOD));
	public static Block FALLBACK_WALL = new WallBlock(FabricBlockSettings.of(Material.WOOD));
	public static Block FALLBACK_FENCE = new FenceBlock(FabricBlockSettings.of(Material.WOOD));
	public static Block FALLBACK_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.of(Material.WOOD));

	public static final ResourcePackSource RESOURCE_PACK_SOURCE = ResourcePackSource.nameAndSource("pack.source.ccpacks");

	CCPacksRegistry ccPacksRegistry = new CCPacksRegistry();

	public static PowerIconManager powerIconManager;

	@Override
	public void onInitialize() {
		GeckoLib.initialize();
		Choice.init();
		powerIconManager = new PowerIconManager();
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
		ModResourcePackUtil.appendModResourcePacks(packs, ResourceType.CLIENT_RESOURCES, null);
		packs.forEach(modResourcePack -> {
			try {
				ccPacksRegistry.registerMods(modResourcePack, modResourcePack.findResources(ResourceType.SERVER_DATA, "ccpacks", "", 60, Objects::nonNull));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		ccPacksRegistry.getMap().forEach(ContentTypes::new);

		Registry.register(Registry.ITEM, identifier("fallback_item"), FALLBACK_ITEM);
		Registry.register(Registry.ITEM, identifier( "fallback_food"), FALLBACK_FOOD);
		Registry.register(Registry.ITEM, identifier( "fallback_sword"), FALLBACK_SWORD);
		Registry.register(Registry.ITEM, identifier( "fallback_pickaxe"), FALLBACK_PICKAXE);
		Registry.register(Registry.ITEM, identifier( "fallback_axe"), FALLBACK_AXE);
		Registry.register(Registry.ITEM, identifier( "fallback_shovel"), FALLBACK_SHOVEL);
		Registry.register(Registry.ITEM, identifier( "fallback_hoe"), FALLBACK_HOE);
		Registry.register(Registry.ITEM, identifier( "fallback_armor"), FALLBACK_ARMOR_ITEM);
		Registry.register(Registry.BLOCK, identifier( "fallback_block"), FALLBACK_BLOCK);
		Registry.register(Registry.ITEM, identifier( "fallback_block"), new BlockItem(FALLBACK_BLOCK, new FabricItemSettings().group(ItemGroup.MISC)));
		Registry.register(Registry.BLOCK, identifier( "fallback_falling_block"), FALLBACK_FALLING_BLOCK);
		Registry.register(Registry.ITEM, identifier( "fallback_falling_block"), new BlockItem(FALLBACK_FALLING_BLOCK, new FabricItemSettings().group(ItemGroup.MISC)));
		Registry.register(Registry.BLOCK, identifier( "fallback_stairs"), FALLBACK_STAIR);
		Registry.register(Registry.ITEM, identifier( "fallback_stairs"), new BlockItem(FALLBACK_STAIR, new FabricItemSettings().group(ItemGroup.MISC)));
		Registry.register(Registry.BLOCK, identifier( "fallback_hslab"), FALLBACK_HSLAB);
		Registry.register(Registry.ITEM, identifier( "fallback_hslab"), new BlockItem(FALLBACK_HSLAB, new FabricItemSettings().group(ItemGroup.MISC)));
		Registry.register(Registry.BLOCK, identifier( "fallback_wall"), FALLBACK_WALL);
		Registry.register(Registry.ITEM, identifier( "fallback_wall"), new BlockItem(FALLBACK_WALL, new FabricItemSettings().group(ItemGroup.MISC)));
		Registry.register(Registry.BLOCK, identifier( "fallback_fence"), FALLBACK_FENCE);
		Registry.register(Registry.ITEM, identifier( "fallback_fence"), new BlockItem(FALLBACK_FENCE, new FabricItemSettings().group(ItemGroup.MISC)));
		Registry.register(Registry.BLOCK, identifier( "fallback_fence_gate"), FALLBACK_FENCE_GATE);
		Registry.register(Registry.ITEM, identifier( "fallback_fence_gate"), new BlockItem(FALLBACK_FENCE_GATE, new FabricItemSettings().group(ItemGroup.MISC)));

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
