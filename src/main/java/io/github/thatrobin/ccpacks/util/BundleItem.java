package io.github.thatrobin.ccpacks.util;

public interface BundleItem {
    boolean isBundle();
    void setBundle(boolean value);
    void setBundleMax(int amount);
    int getBundleMax();
}
