package io.github.thatrobin.ccpacks.factories;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
import io.github.thatrobin.ccpacks.mixins.SerializableDataMixin;
import io.github.thatrobin.ccpacks.util.Mechanic;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.item.Item;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Predicate;

public class BlockConditions {

    public static void register() {
        register(new ConditionFactory<>(CCPacksMain.identifier("resource"), new SerializableData()
                .add("resource", CCPackDataTypes.MECHANIC_TYPE)
                .add("comparison", ApoliDataTypes.COMPARISON, Comparison.EQUAL)
                .add("compare_to", SerializableDataTypes.INT, 0),
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
