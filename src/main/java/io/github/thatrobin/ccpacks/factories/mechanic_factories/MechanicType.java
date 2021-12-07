package io.github.thatrobin.ccpacks.factories.mechanic_factories;

import io.github.thatrobin.ccpacks.util.Mechanic;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;

public class MechanicType<T extends Mechanic> {

    private Identifier identifier;
    private MechanicFactory<Mechanic>.Instance factory;

    private String nameTranslationKey;
    private String descriptionTranslationKey;

    public MechanicType(Identifier id, MechanicFactory<Mechanic>.Instance factory) {
        this.identifier = id;
        this.factory = factory;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public MechanicFactory<Mechanic>.Instance getFactory() {
        return factory;
    }

    public T create(BlockEntity blockEntity) {
        return (T) getFactory().apply((MechanicType<Mechanic>) this, blockEntity);
    }

    @Override
    public int hashCode() {
        return this.identifier.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof MechanicType)) {
            return false;
        }
        Identifier id = ((MechanicType)obj).getIdentifier();
        return identifier.equals(id);
    }
}

