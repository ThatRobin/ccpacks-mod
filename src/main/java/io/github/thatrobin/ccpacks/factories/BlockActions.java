package io.github.thatrobin.ccpacks.factories;

import io.github.thatrobin.ccpacks.CCPackDataTypes;
import io.github.thatrobin.ccpacks.CCPacksMain;
import io.github.thatrobin.ccpacks.component.BlockMechanicHolder;
import io.github.thatrobin.ccpacks.data_driven_classes.blocks.DDBlockEntity;
import io.github.thatrobin.ccpacks.data_driven_classes.mechanics.DDFindBlockMechanic;
import io.github.thatrobin.ccpacks.factories.mechanic_factories.MechanicType;
import io.github.thatrobin.ccpacks.util.Mechanic;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.apoli.util.ResourceOperation;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Triple;

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
                                    if(bool) {
                                        block.getLeft().setBlockState(block.getMiddle(), state.with((Property<Boolean>) property, data.getBoolean("value")));
                                    }
                                }
                            }
                        });
                    }
                }));
            register(new ActionFactory<>(CCPacksMain.identifier("trigger_search"), new SerializableData(),
                    (data, block) -> {
                        World world = block.getLeft();
                        BlockPos blockPos = block.getMiddle();
                        BlockEntity blockEntity = world.getBlockEntity(blockPos);
                        if(blockEntity instanceof DDBlockEntity ddBlockEntity) {
                            BlockMechanicHolder.KEY.get(ddBlockEntity).getMechanics(DDFindBlockMechanic.class).forEach(ddFindBlockMechanic -> {
                                ddFindBlockMechanic.executeAction(block);
                            });
                        }
                    }));
    }


    private static void register(ActionFactory<Triple<World, BlockPos, Direction>> actionFactory) {
        Registry.register(ApoliRegistries.BLOCK_ACTION, actionFactory.getSerializerId(), actionFactory);
    }
}
