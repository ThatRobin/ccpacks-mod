package io.github.ThatRobin.ccpacks.Util;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.impl.resource.loader.ResourceManagerHelperImpl;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.util.Map;

public interface OnLoadResourceManager {

    static void addSingleListener(DataLoader loader) {
        OnLoadResourceManageImpl.addSingleListener(loader);
    }
}
