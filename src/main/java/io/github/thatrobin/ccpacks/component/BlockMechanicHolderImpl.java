package io.github.thatrobin.ccpacks.component;

import io.github.thatrobin.ccpacks.CCPacksMain;
import io.github.thatrobin.ccpacks.data_driven_classes.blocks.DDBlockEntity;
import io.github.thatrobin.ccpacks.factories.mechanic_factories.MechanicRegistry;
import io.github.thatrobin.ccpacks.factories.mechanic_factories.MechanicType;
import io.github.thatrobin.ccpacks.factories.mechanic_factories.MechanicTypeReference;
import io.github.thatrobin.ccpacks.util.Mechanic;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BlockMechanicHolderImpl implements BlockMechanicHolder {

    private final DDBlockEntity ddBlockEntity;
    private final ConcurrentHashMap<MechanicType<?>, Mechanic> mechanics = new ConcurrentHashMap<>();

    public BlockMechanicHolderImpl(DDBlockEntity ddBlockEntity) {
        this.ddBlockEntity = ddBlockEntity;
    }

    @Override
    public void serverTick() {
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag) {
        this.fromTag(tag);
    }

    private void fromTag(NbtCompound tag) {
        mechanics.clear();
        NbtList powerList = (NbtList) tag.get("Mechanics");
        if (powerList != null) {
            for (int i = 0; i < powerList.size(); i++) {
                NbtCompound powerTag = powerList.getCompound(i);
                Identifier powerTypeId = Identifier.tryParse(powerTag.getString("Type"));
                MechanicType<?> type = MechanicRegistry.get(powerTypeId);
                try {
                    NbtElement data = powerTag.get("Data");
                    Mechanic mechanic = type.create(ddBlockEntity);
                    try {
                        mechanic.fromTag(data);
                    } catch (ClassCastException e) {
                        // Occurs when power was overriden by data pack since last world load
                        // to be a power type which uses different data class.
                        CCPacksMain.LOGGER.warn("Data type of \"" + powerTypeId + "\" changed, skipping data for that mechanic on block entity " + ddBlockEntity.getType().toString());
                    }
                    this.mechanics.put(type, mechanic);
                } catch (IllegalArgumentException e) {
                    CCPacksMain.LOGGER.warn("Mechanic data of unregistered mechanic \"" + powerTypeId + "\" found on block entity, skipping...");
                }
            }
        }
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag) {
        NbtList powerList = new NbtList();
        for(Map.Entry<MechanicType<?>, Mechanic> mechanicEntry : mechanics.entrySet()) {
            Mechanic mechanic = mechanicEntry.getValue();
            MechanicType<?> mechanicType = mechanicEntry.getKey();
            NbtCompound powerTag = new NbtCompound();
            powerTag.putString("Type", MechanicRegistry.getId(mechanicType).toString());
            powerTag.put("Data", mechanic.toTag());
            powerList.add(powerTag);
        }
        tag.put("Mechanics", powerList);
    }


    @Override
    public void removeMechanic(MechanicType<?> mechanicType) {
        mechanics.remove(mechanicType);
    }

    @Override
    public int removeAllMechanics() {
        mechanics.clear();
        return 0;
    }

    @Override
    public boolean addMechanic(MechanicType<?> mechanicType) {
        if(mechanicType instanceof MechanicTypeReference mechanicTypeReference) {
            mechanicType = mechanicTypeReference.getReferencedPowerType();
        }
        Mechanic mechanic = mechanicType.create(ddBlockEntity);
        mechanics.put(mechanicType, mechanic);
        return true;
    }

    @Override
    public boolean hasMechanic(MechanicType<?> mechanicType) {
        return mechanics.containsKey(mechanicType);
    }

    @Override
    public <T extends Mechanic> List<T> getMechanics(Class<T> mechanicClass) {
        List<T> list = new LinkedList<>();
        for(Mechanic mechanic : mechanics.values()) {
            if(mechanicClass.isAssignableFrom(mechanic.getClass())) {
                list.add((T)mechanic);
            }
        }
        return list;
    }

    @Override
    public <T extends Mechanic> T getMechanic(Identifier id) {
        if(mechanics.get(id) != null){
            return (T)mechanics.get(id);
        }
        return null;
    }

    @Override
    public <T extends Mechanic> T getMechanic(MechanicType<?> mechanicType) {
        if(mechanics.get(mechanicType) != null){
            return (T)mechanics.get(mechanicType);
        }
        return null;
    }

    @Override
    public List<Mechanic> getMechanics() {
        return new LinkedList<>(mechanics.values());
    }

    @Override
    public void sync() {
        BlockMechanicHolder.sync(this.ddBlockEntity);
    }
}
