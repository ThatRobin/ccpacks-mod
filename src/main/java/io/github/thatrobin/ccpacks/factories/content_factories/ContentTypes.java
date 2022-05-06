package io.github.thatrobin.ccpacks.factories.content_factories;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.thatrobin.ccpacks.CCPacksMain;
import io.github.thatrobin.ccpacks.data_driven_classes.DDSound;
import io.github.thatrobin.ccpacks.data_driven_classes.blocks.DDBlockEntity;
import io.github.thatrobin.ccpacks.data_driven_classes.items.DDDyeableItem;
import io.github.thatrobin.ccpacks.registries.CCPacksRegistries;
import io.github.thatrobin.ccpacks.util.*;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ContentTypes {

    public static Map<Identifier, EntityType<?>> projecitles = Maps.newHashMap();
    public static Map<Identifier, ParticleHolder> particles = Maps.newHashMap();
    public static Map<Identifier, EntityType<?>> entities = Maps.newHashMap();
    public static Map<Identifier, Pair<Block, RenderLayerTypes>> blocks = Maps.newHashMap();
    public static Map<Identifier, DDDyeableItem> dyeItems = Maps.newHashMap();
    public static Map<Identifier, ItemGroupHolder> itemGroups = Maps.newHashMap();

    public ContentTypes(Identifier id, JsonElement je) {
        for (Registry<?> registry : Registry.REGISTRIES) {
            RegistryUtils.unfreeze(registry);
        }
        readPower(id, je, ContentType::new);
    }

    private void readPower(Identifier id, JsonElement je, BiFunction<Identifier, ContentFactory<Supplier<?>>.Instance, ContentType> powerTypeFactory) {
        JsonObject jo = je.getAsJsonObject();
        Identifier factoryId = Identifier.tryParse(JsonHelper.getString(jo, "type"));
        Optional<ContentFactory> contentFactory = CCPacksRegistries.CONTENT_FACTORY.getOrEmpty(factoryId);
        try {
            if (contentFactory.isPresent()) {
                if (CCPacksRegistries.CONTENT_FACTORY.containsId(factoryId)) {
                    contentFactory = CCPacksRegistries.CONTENT_FACTORY.getOrEmpty(factoryId);
                    ContentFactory.Instance factoryInstance = contentFactory.get().read(jo);
                    ContentType type = powerTypeFactory.apply(id, factoryInstance);
                    if (!ContentRegistry.contains(id)) {
                        ContentRegistry.register(id, type);
                    }
                    switch (contentFactory.get().getType()) {
                        case ITEM -> {
                            Item item = type.createItem(type);
                            if (item instanceof DDDyeableItem dyeableItem) {
                                dyeItems.put(id, dyeableItem);
                            }
                            Registry.register(Registry.ITEM, id, item);
                        }
                        case BLOCK -> {
                            Pair<Block, RenderLayerTypes> pair = type.createBlock(type);
                            Registry.register(Registry.BLOCK, id, pair.getLeft());
                            BlockEntityType<DDBlockEntity> DEMO_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create((pos, state) -> new DDBlockEntity(id, pos, state), pair.getLeft()).build(null);
                            if (pair.getLeft() instanceof IDDBlock ddBlock) {
                                ddBlock.setType(DEMO_BLOCK_ENTITY);
                            }
                            blocks.put(id, pair);
                            Registry.register(Registry.BLOCK_ENTITY_TYPE, id, DEMO_BLOCK_ENTITY);
                        }
                        case ENCHANTMENT -> {
                            Enchantment enchantment = type.createEnchant(type);
                            Registry.register(Registry.ENCHANTMENT, id, enchantment);
                        }
                        case EFFECT -> {
                            StatusEffect effect = type.createEffect(type);
                            Registry.register(Registry.STATUS_EFFECT, id, effect);
                        }
                        case PORTAL -> {
                            Portal portal = type.createPortal(type);
                            if (contentFactory.get().getSerializerId().equals(new Identifier("portal", "horizontal"))) {
                                CustomPortalBuilder.beginPortal()
                                        .frameBlock(portal.frameBlock)
                                        .lightWithItem(Registry.ITEM.get(portal.ignitionSource))
                                        .destDimID(portal.dimID)
                                        .tintColor((int) (portal.colourHolder.getRed() * 255), (int) (portal.colourHolder.getGreen() * 255), (int) (portal.colourHolder.getBlue() * 255))
                                        .flatPortal()
                                        .registerPortal();
                            } else {
                                CustomPortalBuilder.beginPortal()
                                        .frameBlock(portal.frameBlock)
                                        .lightWithItem(Registry.ITEM.get(portal.ignitionSource))
                                        .destDimID(portal.dimID)
                                        .tintColor((int) (portal.colourHolder.getRed() * 255), (int) (portal.colourHolder.getGreen() * 255), (int) (portal.colourHolder.getBlue() * 255))
                                        .registerPortal();
                            }
                        }
                        case PROJECTILE -> {
                            EntityType<?> projectile = type.createProjectile(type);
                            projecitles.put(id, projectile);
                            Registry.register(Registry.ENTITY_TYPE, id, projectile);
                        }
                        case SOUND -> {
                            SoundEvent CUSTOM_SOUND = new DDSound(id);
                            Registry.register(Registry.SOUND_EVENT, id, CUSTOM_SOUND);
                        }
                        case PARTICLE -> {
                            ParticleHolder particleHolder = type.createParticle(type);
                            particles.put(id, particleHolder);
                            Registry.register(Registry.PARTICLE_TYPE, id, particleHolder.particleType);
                        }
                        case ENTITY -> {
                            TypeAttributeHolder typeAttributeHolder = type.createEntity(type);
                            EntityType entityType = typeAttributeHolder.getEntityType().build();
                            entities.put(id, entityType);
                            Registry.register(Registry.ENTITY_TYPE, id, entityType);
                            FabricDefaultAttributeRegistry.register(entityType, typeAttributeHolder.getBuilder());
                        }
                        case ITEM_GROUP -> {
                            ItemGroupHolder holder = new ItemGroupHolder(id, jo);
                            if(holder.getItemGroup() instanceof OwoItemGroup owoItemGroup) {
                                owoItemGroup.initialize();
                            }
                            itemGroups.put(id, holder);
                        }
                    }
                }
            }
        } catch (Exception e) {
            CCPacksMain.LOGGER.error("Failed to register item with id: "+id);
            CCPacksMain.LOGGER.error(e.getMessage());
        }
    }

}
