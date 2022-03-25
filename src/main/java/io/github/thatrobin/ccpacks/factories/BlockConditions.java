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

        register(new ConditionFactory<>(CCPacksMain.identifier("block_pattern"), new SerializableData()
                .add("pattern", CCPackDataTypes.STRINGS),
                (data, blockPosition) -> {
                    JsonObject jsonObject = data.get("ccpacksJsonTextGetter");
                    List<String> pattern = data.get("pattern");
                    BlockPatternBuilder builder = BlockPatternBuilder.start().aisle(pattern.toArray(new String[0]));
                    List<Character> characters = Lists.newArrayList();
                    for(String layer : pattern) {
                        for(char character : layer.toCharArray()) {
                            if(!characters.contains(character)) {
                                characters.add(character);
                            }
                        }
                    }
                    for(char character : characters) {
                        String name = Character.toString(character);
                        JsonElement element = jsonObject.get(name);
                        if(element.isJsonObject()) {
                            Predicate<CachedBlockPosition> block = new SerializableData().add(name, ApoliDataTypes.BLOCK_CONDITION).read(jsonObject).get(name);
                            builder.where(character, block);
                        } else {
                            Identifier blockId = Identifier.tryParse(jsonObject.get(name).getAsString());
                            Block block = Registry.BLOCK.get(blockId);
                            builder.where(character, CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(block)));
                        }
                    }

                    BlockPattern blockPattern = builder.build();
                    BlockPattern.Result result = blockPattern.searchAround(blockPosition.getWorld(), blockPosition.getBlockPos());
                    if (result != null) {
                        return true;
                    }
                    return false;
                }));
    }

    private static void register(ConditionFactory<CachedBlockPosition> conditionFactory) {
        Registry.register(ApoliRegistries.BLOCK_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }
}
