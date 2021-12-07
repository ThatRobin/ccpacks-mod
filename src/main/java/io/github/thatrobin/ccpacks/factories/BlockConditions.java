package io.github.thatrobin.ccpacks.factories;

import io.github.thatrobin.ccpacks.CCPackDataTypes;
import io.github.thatrobin.ccpacks.CCPacksMain;
import io.github.thatrobin.ccpacks.component.BlockMechanicHolder;
import io.github.thatrobin.ccpacks.data_driven_classes.blocks.DDBlockEntity;
import io.github.thatrobin.ccpacks.factories.mechanic_factories.MechanicType;
import io.github.thatrobin.ccpacks.util.Mechanic;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.nbt.NbtCompound;
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

        register(new ConditionFactory<>(CCPacksMain.identifier("check_data"), new SerializableData()
                .add("mechanic", CCPackDataTypes.MECHANIC_TYPE, null)
                .add("name", SerializableDataTypes.STRING, "value")
                .add("comparison", ApoliDataTypes.COMPARISON)
                .add("compare_to", SerializableDataTypes.INT),
                (data, block) -> {
                    BlockEntity blockEntity = block.getWorld().getBlockEntity(block.getBlockPos());
                    if (blockEntity instanceof DDBlockEntity ddBlockEntity) {
                        BlockMechanicHolder component = BlockMechanicHolder.KEY.get(ddBlockEntity);
                        if (component.hasMechanic((MechanicType<?>) data.get("mechanic"))) {
                            Mechanic mechanic = component.getMechanic((MechanicType<?>) data.get("mechanic"));
                            NbtCompound compound = mechanic.getNbt();
                            int mechanicValue = compound.getInt(data.getString("name"));
                            CCPacksMain.LOGGER.info(mechanicValue);
                            return ((Comparison)data.get("comparison")).compare(mechanicValue, data.getInt("compare_to"));
                        }
                    }
                    return false;
                }));
    }

    private static void register(ConditionFactory<CachedBlockPosition> conditionFactory) {
        Registry.register(ApoliRegistries.BLOCK_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }
}
