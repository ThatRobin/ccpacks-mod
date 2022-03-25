package io.github.thatrobin.ccpacks.registries;

import io.github.thatrobin.ccpacks.CCPacksMain;
import io.github.thatrobin.ccpacks.factories.content_factories.ContentFactory;
import io.github.thatrobin.ccpacks.factories.mechanic_factories.MechanicFactory;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.registry.Registry;

public class CCPacksRegistries {

    public static final Registry<ContentFactory> CONTENT_FACTORY;
    public static final Registry<MechanicFactory> MECHANIC_FACTORY;

    static {
        CONTENT_FACTORY = FabricRegistryBuilder.createSimple(ContentFactory.class, CCPacksMain.identifier("content_factory")).buildAndRegister();
        MECHANIC_FACTORY = FabricRegistryBuilder.createSimple(MechanicFactory.class, CCPacksMain.identifier("mechanic_factory")).buildAndRegister();
    }

}
