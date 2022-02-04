package io.github.thatrobin.ccpacks.factories.mechanic_factories;

import io.github.thatrobin.ccpacks.util.Mechanic;
import net.minecraft.util.Identifier;

public class MechanicTypeReference extends MechanicType {

    private MechanicType<?> referencedMechanicType;

    public MechanicTypeReference(Identifier id) {
        super(id, null);
    }

    @Override
    public MechanicFactory<Mechanic>.Instance getFactory() {
        getReferencedPowerType();
        if(referencedMechanicType == null) {
            return null;
        }
        return referencedMechanicType.getFactory();
    }

    public MechanicType<?> getReferencedPowerType() {
        if(isReferenceInvalid()) {
            try {
                referencedMechanicType = null;
                referencedMechanicType = MechanicRegistry.get(getIdentifier());
            } catch(IllegalArgumentException ignored) {

            }
        }
        return referencedMechanicType;
    }

    private boolean isReferenceInvalid() {
        if(referencedMechanicType != null) {
            if(MechanicRegistry.contains(referencedMechanicType.getIdentifier())) {
                MechanicType<?> type = MechanicRegistry.get(referencedMechanicType.getIdentifier());
                return type != referencedMechanicType;
            }
        }
        return true;
    }
}
