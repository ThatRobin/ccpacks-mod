package io.github.thatrobin.ccpacks.util;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface DeletableObjectInternal extends DeletableObject {
    void markAsDeleted();
}