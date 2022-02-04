package io.github.thatrobin.ccpacks.factories;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.apoli.util.ResourceOperation;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.thatrobin.ccpacks.CCPackDataTypes;
import io.github.thatrobin.ccpacks.CCPacksMain;
import io.github.thatrobin.ccpacks.component.BlockMechanicHolder;
import io.github.thatrobin.ccpacks.data_driven_classes.blocks.DDBlockEntity;
import io.github.thatrobin.ccpacks.data_driven_classes.mechanics.DDFindBlockMechanic;
import io.github.thatrobin.ccpacks.data_driven_classes.mechanics.DDResourceMechanic;
import io.github.thatrobin.ccpacks.factories.mechanic_factories.MechanicType;
import io.github.thatrobin.ccpacks.util.Mechanic;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Triple;

public class BlockActions {

    public static void register() {
        register(new ActionFactory<>(CCPacksMain.identifier("change_resource"), new SerializableData()
                .add("resource", CCPackDataTypes.MECHANIC_TYPE, null)
                .add("change", SerializableDataTypes.INT, 0)
                .add("operation", ApoliDataTypes.RESOURCE_OPERATION, ResourceOperation.ADD),
                (data, block) -> {
                    BlockEntity blockEntity = block.getLeft().getBlockEntity(block.getMiddle());
                    if (blockEntity instanceof DDBlockEntity ddBlockEntity) {
                        BlockMechanicHolder component = BlockMechanicHolder.KEY.get(ddBlockEntity);
                        ResourceOperation operation = data.get("operation");
                        if(component.hasMechanic(data.get("resource"))) {
                            Mechanic mechanic = component.getMechanic((MechanicType<?>)data.get("resource"));
                            int change = data.getInt("change");
                            if(mechanic instanceof DDResourceMechanic resourceMechanic) {
                                if (operation == ResourceOperation.ADD) {
                                    int newValue = resourceMechanic.getValue() + change;
                                    resourceMechanic.setValue(newValue);
                                } else if (operation == ResourceOperation.SET) {
                                    resourceMechanic.setValue(change);
                                }

                            }
                        }
                        BlockMechanicHolder.sync(blockEntity);
                        component.sync();
                        BlockMechanicHolder.sync(blockEntity);
                        component.sync();
                    }
                }));

            register(new ActionFactory<>(CCPacksMain.identifier("trigger_search"), new SerializableData(),
                    (data, block) -> {
                        World world = block.getLeft();
                        BlockPos blockPos = block.getMiddle();
                        BlockEntity blockEntity = world.getBlockEntity(blockPos);
                        if(blockEntity instanceof DDBlockEntity ddBlockEntity) {
                            BlockMechanicHolder.KEY.get(ddBlockEntity).getMechanics(DDFindBlockMechanic.class).forEach(ddFindBlockMechanic -> ddFindBlockMechanic.executeAction(block));
                        }
                    }));
    }


    private static void register(ActionFactory<Triple<World, BlockPos, Direction>> actionFactory) {
        Registry.register(ApoliRegistries.BLOCK_ACTION, actionFactory.getSerializerId(), actionFactory);
    }
}
