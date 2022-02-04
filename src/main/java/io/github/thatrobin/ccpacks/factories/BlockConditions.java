package io.github.thatrobin.ccpacks.factories;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.thatrobin.ccpacks.CCPackDataTypes;
import io.github.thatrobin.ccpacks.CCPacksMain;
import io.github.thatrobin.ccpacks.component.BlockMechanicHolder;
import io.github.thatrobin.ccpacks.component.BlockMechanicHolderImpl;
import io.github.thatrobin.ccpacks.data_driven_classes.blocks.DDBlockEntity;
import io.github.thatrobin.ccpacks.data_driven_classes.mechanics.DDResourceMechanic;
import io.github.thatrobin.ccpacks.factories.mechanic_factories.MechanicType;
import io.github.thatrobin.ccpacks.util.Mechanic;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class BlockConditions {

    public static void register() {
        register(new ConditionFactory<>(CCPacksMain.identifier("exposed_to_sun"), new SerializableData(),
                (data, blockPosition) -> {
                    if (((World)blockPosition.getWorld()).isDay()) {
                        float f = blockPosition.getWorld().getLightLevel(blockPosition.getBlockPos());
                        return f > 0.5F && blockPosition.getWorld().isSkyVisible(blockPosition.getBlockPos());
                    }
                    return false;
                }));

        register(new ConditionFactory<>(CCPacksMain.identifier("resource"), new SerializableData()
                .add("resource", CCPackDataTypes.MECHANIC_TYPE)
                .add("comparison", ApoliDataTypes.COMPARISON)
                .add("compare_to", SerializableDataTypes.INT),
                (data, blockPosition) -> {
                    BlockEntity blockEntity = blockPosition.getBlockEntity();
                    if (blockEntity instanceof DDBlockEntity ddBlockEntity) {
                        int resourceValue = 0;
                        BlockMechanicHolder component = BlockMechanicHolderImpl.KEY.get(ddBlockEntity);
                        Mechanic mechanic = component.getMechanic((MechanicType<?>) data.get("resource"));
                        if (mechanic instanceof DDResourceMechanic) {
                            resourceValue = ((DDResourceMechanic) mechanic).getValue();
                        }
                        return ((Comparison) data.get("comparison")).compare(resourceValue, data.getInt("compare_to"));
                    }
                    return false;
                }));
    }

    private static void register(ConditionFactory<CachedBlockPosition> conditionFactory) {
        Registry.register(ApoliRegistries.BLOCK_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }
}
