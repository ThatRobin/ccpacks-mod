package io.github.thatrobin.ccpacks.factories.content_factories;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.apace100.apoli.ApoliClient;
import io.github.thatrobin.ccpacks.CCPacksMain;
import io.github.thatrobin.ccpacks.data_driven_classes.items.DDDyeableItem;
import io.github.thatrobin.ccpacks.data_driven_classes.particles.DDGlowParticle;
import io.github.thatrobin.ccpacks.data_driven_classes.particles.DDParticle;
import io.github.thatrobin.ccpacks.registries.CCPacksRegistries;
import io.github.thatrobin.ccpacks.util.ParticleHolder;
import io.github.thatrobin.ccpacks.util.RegistryUtils;
import io.github.thatrobin.ccpacks.util.RenderLayerTypes;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.block.Block;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.item.DyeableItem;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ContentTypesClient {

    public ContentTypesClient(Identifier id, JsonElement je) {
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
                    ContentFactory.Instance factoryInstance = contentFactory.get().read(jo);

                    ContentType type = powerTypeFactory.apply(id, factoryInstance);
                    if (!ContentRegistry.contains(id)) {
                        ContentRegistry.register(id, type);
                    }
                    if (contentFactory.get().getType() == Types.KEYBIND) {
                        KeyBinding key = type.createKeybind(type);
                        ApoliClient.registerPowerKeybinding(key.getTranslationKey(), key);
                        KeyBindingHelper.registerKeyBinding(key);
                    }
                }
                if(ContentTypes.particles.containsKey(id)) {
                    registerParticle(id, contentFactory, ContentTypes.particles.get(id));
                }
                if(ContentTypes.projecitles.containsKey(id)) {
                    EntityRendererRegistry.INSTANCE.register(ContentTypes.projecitles.get(id), (context) -> new FlyingItemEntityRenderer(context));
                }
                if(ContentTypes.entities.containsKey(id)) {
                    //EntityRendererRegistry.INSTANCE.register(ContentTypes.entities.get(id), (context) -> new DDGeoRenderer(context));
                }
                if(ContentTypes.blocks.containsKey(id)) {
                    Pair<Block, RenderLayerTypes> pair = ContentTypes.blocks.get(id);
                    setRenderLayer(pair.getLeft(), pair.getRight());
                }
                if(ContentTypes.dyeItems.containsKey(id)) {
                    ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
                        return tintIndex > 0 ? -1 : ((DyeableItem)stack.getItem()).getColor(stack);
                    }, ContentTypes.dyeItems.get(id));
                }
            }
        } catch (Exception e) {
            CCPacksMain.LOGGER.error(e.getMessage());
        }
    }

    private static void setRenderLayer(Block block, RenderLayerTypes renderLayer) {
        RenderLayer layer = RenderLayer.getSolid();
        switch (renderLayer) {
            case CUTOUT:
                layer = RenderLayer.getCutout();
            case TRANSLUCENT:
                layer = RenderLayer.getTranslucent();
        }
        BlockRenderLayerMap.INSTANCE.putBlock(block, layer);
    }

    public void registerParticle(Identifier id, Optional<ContentFactory> itemFactory, ParticleHolder particleHolder) {
        ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register(((atlasTexture, registry) -> registry.register(id)));
        if(itemFactory.isPresent()) {
            if (itemFactory.get().getSerializerId().toString().contains("generic")) {
                ParticleFactoryRegistry.getInstance().register(particleHolder.particleType, (test) -> new DDParticle.Factory(test, particleHolder.size, particleHolder.max_age, particleHolder.collides_with_world, particleHolder.colourHolder));
            } else if (itemFactory.get().getSerializerId().toString().contains("emissive")) {
                ParticleFactoryRegistry.getInstance().register(particleHolder.particleType, (test) -> new DDGlowParticle.Factory(test, particleHolder.size, particleHolder.max_age, particleHolder.collides_with_world, particleHolder.colourHolder));
            }
        }
    }

}
