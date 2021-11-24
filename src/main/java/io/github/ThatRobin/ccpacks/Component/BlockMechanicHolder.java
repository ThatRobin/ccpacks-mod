package io.github.ThatRobin.ccpacks.Component;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.ThatRobin.ccpacks.Factories.MechanicFactories.MechanicType;
import io.github.ThatRobin.ccpacks.Util.Mechanic;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;

import java.util.List;

public interface BlockMechanicHolder extends AutoSyncedComponent, ServerTickingComponent {

    ComponentKey<BlockMechanicHolder> KEY = ComponentRegistry.getOrCreate(CCPacksMain.identifier("mechanics"), BlockMechanicHolder.class);

    void removeMechanic(MechanicType<?> mechanic);

    int removeAllMechanics();

    boolean addMechanic(MechanicType<?> mechanicType);

    boolean hasMechanic(MechanicType<?> mechanicType);

    <T extends Mechanic> List<T> getMechanics(Class<T> powerClass);

    <T extends Mechanic> T getMechanic(Identifier id);

    <T extends Mechanic> T getMechanic(MechanicType mechanicType);

    List<Mechanic> getMechanics();

    void sync();

    static void sync(BlockEntity blockEntity) {
        KEY.sync(blockEntity);
    }
}
