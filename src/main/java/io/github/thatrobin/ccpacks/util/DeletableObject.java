package io.github.thatrobin.ccpacks.util;

public interface DeletableObject {
    default boolean wasDeleted() {
        throw new UnsupportedOperationException("Method wasn't implemented!");
    }
}