package io.github.thatrobin.ccpacks.factories.content_factories;

import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class ContentTypeReference extends ContentType {

    private ContentType referencedPowerType;
    private int cooldown = 0;

    public ContentTypeReference(Identifier id) {
        super(id, null);
    }

    @Override
    public ContentFactory<Supplier<?>>.Instance getFactory() {
        getReferencedPowerType();
        if(referencedPowerType == null) {
            return null;
        }
        return referencedPowerType.getFactory();
    }

    public ContentType getReferencedPowerType() {
        if(isReferenceInvalid()) {
            if(cooldown > 0) {
                cooldown--;
                return null;
            }
            try {
                referencedPowerType = null;
                referencedPowerType = ContentRegistry.get(getIdentifier());
            } catch(IllegalArgumentException e) {
                cooldown = 600;
            }
        }
        return referencedPowerType;
    }

    private boolean isReferenceInvalid() {
        if(referencedPowerType != null) {
            if(ContentRegistry.contains(referencedPowerType.getIdentifier())) {
                ContentType type = ContentRegistry.get(referencedPowerType.getIdentifier());
                return type != referencedPowerType;
            }
        }
        return true;
    }
}
