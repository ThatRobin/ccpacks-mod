package io.github.ThatRobin.ccpacks.Util;

import com.google.gson.JsonObject;
import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.ThatRobin.ccpacks.Registries.CCPacksRegistry;
import net.fabricmc.fabric.impl.resource.loader.ResourceManagerHelperImpl;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class OnLoadResourceManageImpl implements OnLoadResourceManager {



    public static void addSingleListener(DataLoader loader) {
        CCPacksMain.ccPacksRegistry.registerResources(loader, loader.dataType);
    }
}
