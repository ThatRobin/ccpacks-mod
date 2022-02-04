package io.github.thatrobin.ccpacks.factories.content_factories;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.thatrobin.ccpacks.data_driven_classes.DDSound;
import io.github.thatrobin.ccpacks.data_driven_classes.blocks.DDBlock;
import io.github.thatrobin.ccpacks.data_driven_classes.blocks.DDBlockEntity;
import io.github.thatrobin.ccpacks.data_driven_classes.blocks.DDTransparentBlock;
import io.github.thatrobin.ccpacks.registries.CCPacksRegistries;
import io.github.thatrobin.ccpacks.util.GameruleHolder;
import io.github.thatrobin.ccpacks.util.Portal;
import io.github.thatrobin.ccpacks.util.TypeAttributeHolder;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ContentTypes {

    public static Map<Identifier, EntityType<?>> projecitles = Maps.newHashMap();
    public static Map<Identifier, DefaultParticleType> particles = Maps.newHashMap();
    public static Map<Identifier, EntityType<?>> entities = Maps.newHashMap();
    public static Map<Identifier, GameruleHolder> gamerules = Maps.newHashMap();
    public static Map<Identifier, DDTransparentBlock> blocks = Maps.newHashMap();

    public ContentTypes(Identifier id, JsonElement je) {
        readPower(id, je, ContentType::new);
    }

    private void readPower(Identifier id, JsonElement je, BiFunction<Identifier, ContentFactory<Supplier<?>>.Instance, ContentType> powerTypeFactory) {
        JsonObject jo = je.getAsJsonObject();
        Identifier factoryId = Identifier.tryParse(JsonHelper.getString(jo, "type"));
        Optional<ContentFactory> contentFactory = CCPacksRegistries.CONTENT_FACTORY.getOrEmpty(factoryId);
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
                        Registry.register(Registry.ITEM, id, item);
                    }
                    case BLOCK -> {
                        Block block = type.createBlock(type);
                        Registry.register(Registry.BLOCK, id, block);
                        BlockEntityType<DDBlockEntity> DEMO_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create((pos, state) -> new DDBlockEntity(id, pos, state), block).build(null);
                        if(block instanceof DDBlock ddBlock) {
                            ddBlock.type = DEMO_BLOCK_ENTITY;
                        }
                        if(block instanceof DDTransparentBlock ddTransparentBlock) {
                            ddTransparentBlock.type = DEMO_BLOCK_ENTITY;
                            blocks.put(id, ddTransparentBlock);
                        }
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
                        DefaultParticleType particle = type.createParticle(type);
                        particles.put(id, particle);
                        Registry.register(Registry.PARTICLE_TYPE, id, particle);
                    }
                    case ENTITY -> {
                        TypeAttributeHolder typeAttributeHolder = type.createEntity(type);
                        EntityType entityType = typeAttributeHolder.getEntityType().build();
                        entities.put(id, entityType);
                        Registry.register(Registry.ENTITY_TYPE, id, entityType);
                        FabricDefaultAttributeRegistry.register(entityType, typeAttributeHolder.getBuilder());
                    }
                    case GAMERULE -> {
                        GameruleHolder holder = type.createGamerule(type);
                        gamerules.put(id, holder);
                    }
                }
            }
        }
    }

}
