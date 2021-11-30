package io.github.ThatRobin.ccpacks.Component;

import com.google.common.collect.Lists;
import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.ThatRobin.ccpacks.DataDrivenClasses.Blocks.DDBlockEntity;
import io.github.ThatRobin.ccpacks.Factories.MechanicFactories.MechanicRegistry;
import io.github.ThatRobin.ccpacks.Factories.MechanicFactories.MechanicType;
import io.github.ThatRobin.ccpacks.Factories.MechanicFactories.MechanicTypeReference;
import io.github.ThatRobin.ccpacks.Util.Mechanic;
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.*;
import net.minecraft.util.Identifier;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BlockMechanicHolderImpl implements BlockMechanicHolder {

    private DDBlockEntity ddBlockEntity;
    private final ConcurrentHashMap<MechanicType<?>, Mechanic> mechanics = new ConcurrentHashMap<>();

    public BlockMechanicHolderImpl(DDBlockEntity ddBlockEntity) {
        this.ddBlockEntity = ddBlockEntity;
    }

    @Override
    public void serverTick() {
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
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
    public void writeToNbt(NbtCompound tag) {
        NbtList powerList = new NbtList();
        for(Map.Entry<MechanicType<?>, Mechanic> mechanicEntry : mechanics.entrySet()) {
            NbtCompound powerTag = new NbtCompound();
            powerTag.putString("Type", MechanicRegistry.getId(mechanicEntry.getKey()).toString());
            powerTag.put("Data", mechanicEntry.getValue().getNbt());
            powerList.add(powerTag);
        }
        tag.put("Mechanics", powerList);
    }


    @Override
    public void removeMechanic(MechanicType mechanicType) {
        mechanics.remove(mechanicType);
    }

    @Override
    public int removeAllMechanics() {
        mechanics.clear();
        return mechanics.size();
    }

    @Override
    public boolean addMechanic(MechanicType mechanicType) {
        if(mechanicType instanceof MechanicTypeReference<?> mechanicTypeReference) {
            mechanicType = mechanicTypeReference.getReferencedPowerType();
        }
        Mechanic mechanic = mechanicType.create(ddBlockEntity);
        mechanics.put(mechanicType, mechanic);
        return true;
    }

    @Override
    public boolean hasMechanic(MechanicType mechanicType) {
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
    public <T extends Mechanic> T getMechanic(MechanicType mechanicType) {
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
