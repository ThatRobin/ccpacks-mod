package io.github.ThatRobin.ccpacks.Factories.ContentFactories;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.ThatRobin.ccpacks.DataDrivenClasses.DDSound;
import io.github.ThatRobin.ccpacks.DataDrivenClasses.Items.DDItem;
import io.github.ThatRobin.ccpacks.Registries.CCPacksRegistries;
import io.github.ThatRobin.ccpacks.Util.GameruleHolder;
import io.github.ThatRobin.ccpacks.Util.Portal;
import io.github.ThatRobin.ccpacks.Util.TypeAttributeHolder;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.kyrptonaught.customportalapi.CustomPortalApiRegistry;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.condition.LootConditionManager;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionManager;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameRules;
import software.bernie.example.EntityUtils;
import software.bernie.example.registry.EntityRegistry;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ContentTypes {

    public static Map<Identifier, EntityType> projecitles = Maps.newHashMap();
    public static Map<Identifier, DefaultParticleType> particles = Maps.newHashMap();
    public static Map<Identifier, EntityType> entities = Maps.newHashMap();
    public static Map<Identifier, GameruleHolder> gamerules = Maps.newHashMap();

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
                        boolean make_item = true;
                        int fuel_tick = 0;
                        if (JsonHelper.hasBoolean(jo, "make_block_item")) {
                            make_item = JsonHelper.getBoolean(jo, "make_block_item");
                        }
                        if (JsonHelper.hasNumber(jo, "fuel_tick")) {
                            fuel_tick = JsonHelper.getInt(jo, "fuel_tick");
                        }
                        if (make_item) {
                            FabricItemSettings settings = new FabricItemSettings();
                            settings.group(ItemGroup.BUILDING_BLOCKS);
                            Item item = new BlockItem(block, settings);
                            Registry.register(Registry.ITEM, id, item);
                            if(fuel_tick > 0) {
                                FuelRegistry.INSTANCE.add(item, fuel_tick);
                            }
                        }
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
                        EntityType projectile = type.createProjectile(type);
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
