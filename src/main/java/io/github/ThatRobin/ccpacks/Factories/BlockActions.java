package io.github.ThatRobin.ccpacks.Factories;

import io.github.ThatRobin.ccpacks.CCPackDataTypes;
import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.ThatRobin.ccpacks.Component.BlockMechanicHolder;
import io.github.ThatRobin.ccpacks.Component.BlockMechanicHolderImpl;
import io.github.ThatRobin.ccpacks.DataDrivenClasses.Blocks.DDBlockEntity;
import io.github.ThatRobin.ccpacks.Factories.MechanicFactories.MechanicRegistry;
import io.github.ThatRobin.ccpacks.Factories.MechanicFactories.MechanicType;
import io.github.ThatRobin.ccpacks.Factories.MechanicFactories.MechanicTypeReference;
import io.github.ThatRobin.ccpacks.Util.Mechanic;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.apoli.util.ResourceOperation;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.state.property.Property;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Collection;
import java.util.Optional;

public class BlockActions {

    public static void register() {
        register(new ActionFactory<>(CCPacksMain.identifier("change_data"), new SerializableData()
                .add("mechanic", CCPackDataTypes.MECHANIC_TYPE, null)
                .add("name", SerializableDataTypes.STRING, "value")
                .add("change", SerializableDataTypes.INT, 0)
                .add("operation", ApoliDataTypes.RESOURCE_OPERATION, ResourceOperation.ADD),
                (data, block) -> {
                    if(!block.getLeft().isClient()) {
                        BlockEntity blockEntity = block.getLeft().getBlockEntity(block.getMiddle());
                        if (blockEntity instanceof DDBlockEntity ddBlockEntity) {
                            BlockMechanicHolder component = BlockMechanicHolder.KEY.get(ddBlockEntity);
                            ResourceOperation operation = (ResourceOperation) data.get("operation");
                            if(component.hasMechanic((MechanicType<?>)data.get("mechanic"))) {
                                Mechanic mechanic = component.getMechanic((MechanicType<?>)data.get("mechanic"));
                                if (operation == ResourceOperation.ADD) {
                                    NbtCompound compound = mechanic.getNbt();
                                    int newValue = compound.getInt(data.getString("name")) + data.getInt("change");
                                    compound.putInt(data.getString("name"), newValue);
                                    mechanic.setNbt(compound);
                                } else if (operation == ResourceOperation.SET) {
                                    NbtCompound compound = new NbtCompound();
                                    compound.putInt(data.getString("name"), data.getInt("change"));
                                    mechanic.setNbt(compound);
                                }

                            }
                            component.sync();
                        }
                    }
                }));

        register(new ActionFactory<>(CCPacksMain.identifier("change_state"), new SerializableData()
                .add("property", SerializableDataTypes.STRING)
                .add("value", SerializableDataTypes.BOOLEAN, null),
                (data, block) -> {
                    BlockEntity blockEntity = block.getLeft().getBlockEntity(block.getMiddle());
                    if (blockEntity instanceof DDBlockEntity ddBlockEntity) {
                        BlockState state = ddBlockEntity.getCachedState();
                        ddBlockEntity.getCachedState().getProperties().forEach(property -> {
                            if(property.getName().equals(data.getString("property"))) {
                                if(data.isPresent("value") && (Object)state.get(property) instanceof Boolean bool) {
                                    CCPacksMain.LOGGER.info("property is boolean");
                                    if(bool) {
                                        block.getLeft().setBlockState(block.getMiddle(), state.with((Property<Boolean>) property, data.getBoolean("value")));
                                    }
                                }
                            }
                        });
                    }
                }));
    }

    private static void register(ActionFactory<Triple<World, BlockPos, Direction>> actionFactory) {
        Registry.register(ApoliRegistries.BLOCK_ACTION, actionFactory.getSerializerId(), actionFactory);
    }
}
