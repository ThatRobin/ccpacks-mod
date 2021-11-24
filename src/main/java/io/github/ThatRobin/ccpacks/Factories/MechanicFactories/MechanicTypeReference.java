package io.github.ThatRobin.ccpacks.Factories.MechanicFactories;

import io.github.ThatRobin.ccpacks.Util.Mechanic;
import net.minecraft.util.Identifier;

public class MechanicTypeReference<T extends Mechanic> extends MechanicType {

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

    public MechanicType getReferencedPowerType() {
        if(isReferenceInvalid()) {
            try {
                referencedMechanicType = null;
                referencedMechanicType = MechanicRegistry.get(getIdentifier());
            } catch(IllegalArgumentException e) {

            }
        }
        return referencedMechanicType;
    }

    private boolean isReferenceInvalid() {
        if(referencedMechanicType != null) {
            if(MechanicRegistry.contains(referencedMechanicType.getIdentifier())) {
                MechanicType type = MechanicRegistry.get(referencedMechanicType.getIdentifier());
                return type != referencedMechanicType;
            }
        }
        return true;
    }
}
