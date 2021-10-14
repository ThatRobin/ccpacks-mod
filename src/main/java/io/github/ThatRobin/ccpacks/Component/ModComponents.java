package io.github.ThatRobin.ccpacks.Component;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import io.github.ThatRobin.ccpacks.CCPacksMain;
import net.minecraft.util.Identifier;

public class ModComponents implements EntityComponentInitializer {

    public static final ComponentKey<ChoiceComponent> CHOICE;

    static {
        CHOICE = ComponentRegistry.getOrCreate(new Identifier(CCPacksMain.MODID, "choice"), ChoiceComponent.class);
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(CHOICE, PlayerChoiceComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
    }

}
