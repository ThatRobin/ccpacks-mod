package io.github.ThatRobin.ccpacks.Registries;

import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.ThatRobin.ccpacks.Factories.ContentFactories.ContentFactory;
import io.github.ThatRobin.ccpacks.Factories.TaskFactories.TaskFactory;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.registry.Registry;

public class CCPacksRegistries {

    public static final Registry<ContentFactory> CONTENT_FACTORY;
    public static final Registry<TaskFactory> TASK_FACTORY;

    static {
        CONTENT_FACTORY = FabricRegistryBuilder.createSimple(ContentFactory.class, CCPacksMain.identifier("content_factory")).buildAndRegister();
        TASK_FACTORY = FabricRegistryBuilder.createSimple(TaskFactory.class, CCPacksMain.identifier("task_factory")).buildAndRegister();
    }

}
