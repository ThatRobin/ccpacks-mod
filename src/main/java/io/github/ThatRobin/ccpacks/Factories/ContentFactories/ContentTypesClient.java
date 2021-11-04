package io.github.ThatRobin.ccpacks.Factories.ContentFactories;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.ThatRobin.ccpacks.DataDrivenClasses.Entities.client.renderer.DDGeoRenderer;
import io.github.ThatRobin.ccpacks.DataDrivenClasses.Particles.DDGlowParticle;
import io.github.ThatRobin.ccpacks.DataDrivenClasses.Particles.DDParticle;
import io.github.ThatRobin.ccpacks.Registries.CCPacksRegistries;
import io.github.apace100.apoli.ApoliClient;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ContentTypesClient {

    private Optional<ContentFactory> contentFactory;

    public ContentTypesClient(Identifier id, JsonElement je) {
        readPower(id, je, ContentType::new);
    }

    private void readPower(Identifier id, JsonElement je, BiFunction<Identifier, ContentFactory<Supplier<?>>.Instance, ContentType> powerTypeFactory) {
        JsonObject jo = je.getAsJsonObject();
        Identifier factoryId = Identifier.tryParse(JsonHelper.getString(jo, "type"));
        contentFactory = CCPacksRegistries.CONTENT_FACTORY.getOrEmpty(factoryId);
        try {
            if (contentFactory.isPresent()) {
                if (CCPacksRegistries.CONTENT_FACTORY.containsId(factoryId)) {
                    if(contentFactory.isPresent()) {
                        ContentFactory.Instance factoryInstance = contentFactory.get().read(jo);

                        ContentType type = powerTypeFactory.apply(id, factoryInstance);
                        if (!ContentRegistry.contains(id)) {
                            ContentRegistry.register(id, type);
                        }
                        switch (contentFactory.get().getType()) {
                            case KEYBIND -> {
                                KeyBinding key = type.createKeybind(type);
                                ApoliClient.registerPowerKeybinding(key.getTranslationKey(), key);
                                KeyBindingHelper.registerKeyBinding(key);
                            }
                        }
                    }
                }
                if(ContentTypes.particles.containsKey(id)) {
                    registerParticle(id, contentFactory, ContentTypes.particles.get(id));
                }
                if(ContentTypes.projecitles.containsKey(id)) {
                    EntityRendererRegistry.INSTANCE.register(ContentTypes.projecitles.get(id), (context) -> new FlyingItemEntityRenderer(context));
                }
                if(ContentTypes.entities.containsKey(id)) {
                    EntityRendererRegistry.INSTANCE.register(ContentTypes.entities.get(id), (context) -> new DDGeoRenderer(context));
                }
            }
        } catch (Exception e) {
            CCPacksMain.LOGGER.error(e.getMessage());
        }
    }

    public void registerParticle(Identifier id, Optional<ContentFactory> itemFactory, DefaultParticleType particle) {
        ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register(((atlasTexture, registry) -> registry.register(id)));
        if(itemFactory.isPresent()) {
            if (itemFactory.get().getSerializerId().toString().contains("generic")) {
                ParticleFactoryRegistry.getInstance().register(particle, DDParticle.Factory::new);
            } else if (itemFactory.get().getSerializerId().toString().contains("emissive")) {
                ParticleFactoryRegistry.getInstance().register(particle, DDGlowParticle.Factory::new);
            }
        }
    }

}
