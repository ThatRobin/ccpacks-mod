package io.github.thatrobin.ccpacks.util;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.attribute.DefaultAttributeContainer;

public class TypeAttributeHolder {

    private final FabricEntityTypeBuilder<?> entityType;
    private final DefaultAttributeContainer.Builder builder;

    public TypeAttributeHolder(FabricEntityTypeBuilder<?> entityType, DefaultAttributeContainer.Builder builder) {
        this.entityType = entityType;
        this.builder = builder;
    }

    public FabricEntityTypeBuilder<?> getEntityType() {
        return this.entityType;
    }

    public DefaultAttributeContainer.Builder getBuilder() {
        return this.builder;
    }
}
