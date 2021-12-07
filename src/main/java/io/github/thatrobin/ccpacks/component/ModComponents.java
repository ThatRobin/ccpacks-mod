package io.github.thatrobin.ccpacks.component;

import dev.onyxstudios.cca.api.v3.block.BlockComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.block.BlockComponentInitializer;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import io.github.thatrobin.ccpacks.CCPacksMain;
import io.github.thatrobin.ccpacks.data_driven_classes.blocks.DDBlockEntity;
import net.minecraft.util.Identifier;

public class ModComponents implements EntityComponentInitializer, BlockComponentInitializer {

    public static final ComponentKey<ChoiceComponent> CHOICE;
    public static final ComponentKey<BlockMechanicHolder> MECHANIC;

    static {
        CHOICE = ComponentRegistry.getOrCreate(new Identifier(CCPacksMain.MODID, "choice"), ChoiceComponent.class);
        MECHANIC = ComponentRegistry.getOrCreate(new Identifier(CCPacksMain.MODID, "mechanics"), BlockMechanicHolder.class);
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(CHOICE, PlayerChoiceComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
    }

    @Override
    public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {
        registry.registerFor(DDBlockEntity.class, MECHANIC ,BlockMechanicHolderImpl::new);
    }
}
