package io.github.thatrobin.ccpacks.data_driven_classes.mechanics;

import io.github.thatrobin.ccpacks.factories.mechanic_factories.MechanicType;
import io.github.thatrobin.ccpacks.util.Mechanic;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtInt;

public class DDResourceMechanic extends Mechanic {

    protected final int min, max;
    protected int currentValue;
    private final String name;

    public DDResourceMechanic(MechanicType<?> mechanicType, BlockEntity blockEntity, int startValue, int min, int max) {
        super(mechanicType, blockEntity);
        this.name = mechanicType.getIdentifier().getPath();
        this.currentValue = startValue;
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getValue() {
        return currentValue;
    }

    public String getName() {
        return name;
    }

    public int setValue(int newValue) {
        if(newValue > getMax())
            newValue = getMax();
        if(newValue < getMin())
            newValue = getMin();
        return currentValue = newValue;
    }

    public int increment() {
        return setValue(getValue() + 1);
    }

    public int decrement() {
        return setValue(getValue() - 1);
    }

    @Override
    public NbtElement toTag() {
        return NbtInt.of(currentValue);
    }

    @Override
    public void fromTag(NbtElement tag) {
        currentValue = ((NbtInt)tag).intValue();
    }

}
