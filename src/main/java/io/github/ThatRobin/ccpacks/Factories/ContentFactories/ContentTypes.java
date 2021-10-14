package io.github.ThatRobin.ccpacks.Factories.ContentFactories;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.ThatRobin.ccpacks.Registries.CCPacksRegistries;
import io.github.ThatRobin.ccpacks.Util.Portal;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.DDSound;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.Particles.DDGlowParticle;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.Particles.DDParticle;
import io.github.apace100.apoli.ApoliClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.loader.api.FabricLoader;
import net.kyrptonaught.customportalapi.CustomPortalApiRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ContentTypes {

    public ContentTypes(Identifier id, JsonElement je) {
        readPower(id, je, ContentType::new);
    }

    private void readPower(Identifier id, JsonElement je, BiFunction<Identifier, ContentFactory<Supplier<?>>.Instance, ContentType> powerTypeFactory) {
        JsonObject jo = je.getAsJsonObject();
        Identifier factoryId = Identifier.tryParse(JsonHelper.getString(jo, "type"));
        Optional<ContentFactory> itemFactory = CCPacksRegistries.CONTENT_FACTORY.getOrEmpty(factoryId);
        if (itemFactory.isPresent()) {
            if (CCPacksRegistries.CONTENT_FACTORY.containsId(factoryId)) {
                itemFactory = CCPacksRegistries.CONTENT_FACTORY.getOrEmpty(factoryId);
                ContentFactory.Instance factoryInstance = itemFactory.get().read(jo);
                ContentType type = powerTypeFactory.apply(id, factoryInstance);
                if (!ContentRegistry.contains(id)) {
                    ContentRegistry.register(id, type);
                }

                if(itemFactory.get().getType() == Types.ITEM) {
                    Item item = type.createItem(type);
                    Registry.register(Registry.ITEM, id, item);
                } else if(itemFactory.get().getType() == Types.BLOCK) {
                    Block block = type.createBlock(type);
                    Registry.register(Registry.BLOCK, id, block);
                    boolean make_item = true;
                    if(JsonHelper.hasBoolean(jo,"make_block_item")) {
                        make_item = JsonHelper.getBoolean(jo, "make_block_item");
                    }
                    if(make_item) {
                        FabricItemSettings settings = new FabricItemSettings();
                        settings.group(ItemGroup.BUILDING_BLOCKS);
                        Item item = new BlockItem(block, settings);
                        Registry.register(Registry.ITEM, id, item);
                    }
                } else if(itemFactory.get().getType() == Types.ENCHANTMENT) {
                    Enchantment enchantment = type.createEnchant(type);
                    Registry.register(Registry.ENCHANTMENT, id, enchantment);
                } else if(itemFactory.get().getType() == Types.KEYBIND) {
                    KeyBinding key = type.createKeybind(type);
                    ApoliClient.registerPowerKeybinding(key.getTranslationKey(), key);
                    KeyBindingHelper.registerKeyBinding(key);
                } else if(itemFactory.get().getType() == Types.EFFECT) {
                    StatusEffect effect = type.createEffect(type);
                    Registry.register(Registry.STATUS_EFFECT, id, effect);
                } else if(itemFactory.get().getType() == Types.PORTAL) {
                    Portal portal = type.createPortal(type);
                    CustomPortalApiRegistry.addPortal(portal.frameBlock, portal.dimID, portal.r, portal.b, portal.r);
                } else if(itemFactory.get().getType() == Types.PROJECTILE) {
                    EntityType projectile = type.createProjectile(type);
                    Registry.register(Registry.ENTITY_TYPE, id, projectile);
                    if(FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
                        EntityRendererRegistry.INSTANCE.register(projectile, (context) -> new FlyingItemEntityRenderer(context));
                    }
                } else if(itemFactory.get().getType() == Types.SOUND) {
                    SoundEvent CUSTOM_SOUND = new DDSound(id);
                    Registry.register(Registry.SOUND_EVENT, id, CUSTOM_SOUND);
                } else if(itemFactory.get().getType() == Types.PARTICLE) {
                    DefaultParticleType particle = type.createParticle(type);
                    Registry.register(Registry.PARTICLE_TYPE, id, particle);
                    if(FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
                        ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register(((atlasTexture, registry) -> {
                            registry.register(id);
                        }));
                        if(itemFactory.get().getSerializerId().toString().contains("generic")) {
                            ParticleFactoryRegistry.getInstance().register(particle, DDParticle.Factory::new);
                        } else if (itemFactory.get().getSerializerId().toString().contains("emissive")) {
                            ParticleFactoryRegistry.getInstance().register(particle, DDGlowParticle.Factory::new);
                        }
                    }
                }
            }
        }
    }

}
