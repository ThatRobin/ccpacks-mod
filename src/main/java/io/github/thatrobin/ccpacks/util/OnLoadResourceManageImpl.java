package io.github.thatrobin.ccpacks.util;

import io.github.thatrobin.ccpacks.CCPacksMain;

public class OnLoadResourceManageImpl implements OnLoadResourceManager {

    public static void addSingleListener(DataLoader loader) {
        CCPacksMain.ccPacksRegistry.registerResources(loader, loader.dataType);
    }
}
