package io.github.ThatRobin.ccpacks.Factories.ContentFactories;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.ThatRobin.ccpacks.DataDrivenClasses.DDSound;
import io.github.ThatRobin.ccpacks.Registries.CCPacksRegistries;
import io.github.ThatRobin.ccpacks.Util.Portal;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.kyrptonaught.customportalapi.CustomPortalApiRegistry;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
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

    public static Map<Identifier, EntityType> projecitles = Maps.newHashMap();
    public static Map<Identifier, DefaultParticleType> particles = Maps.newHashMap();

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
                        CustomPortalApiRegistry.addPortal(portal.frameBlock, portal.ignitionSource, portal.dimID, (int) portal.colourHolder.getRed()*255, (int) portal.colourHolder.getRed()*255, (int) portal.colourHolder.getRed()*255);
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
                }
            }
        }
    }

}
