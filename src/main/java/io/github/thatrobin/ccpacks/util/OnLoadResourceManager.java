package io.github.thatrobin.ccpacks.util;

public interface OnLoadResourceManager {

    static void addSingleListener(DataLoader loader) {
        OnLoadResourceManageImpl.addSingleListener(loader);
    }
}
