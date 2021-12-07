package io.github.thatrobin.ccpacks.data_driven_classes.mechanics;

import com.google.common.collect.Lists;
import io.github.thatrobin.ccpacks.CCPacksMain;
import io.github.thatrobin.ccpacks.component.BlockMechanicHolder;
import io.github.thatrobin.ccpacks.data_driven_classes.blocks.DDBlockEntity;
import io.github.thatrobin.ccpacks.factories.mechanic_factories.MechanicType;
import io.github.thatrobin.ccpacks.util.Mechanic;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.nbt.NbtElement;
import net.minecraft.tag.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;

public class DDFindBlockMechanic extends Mechanic {

    public ActionFactory<Triple<World, BlockPos, Direction>>.Instance action;
    public ActionFactory<Triple<World, BlockPos, Direction>>.Instance found_action;
    public ActionFactory<Triple<World, BlockPos, Direction>>.Instance continue_action;
    public Tag<Block> continue_tag;
    public Tag<Block> execute_tag;
    public ConditionFactory<CachedBlockPosition>.Instance block_condition;
    boolean visited = false;

    public DDFindBlockMechanic(MechanicType<?> mechanicType, BlockEntity blockEntity, Tag<Block> continue_tag, Tag<Block> execute_tag, ActionFactory<Triple<World, BlockPos, Direction>>.Instance action, ActionFactory<Triple<World, BlockPos, Direction>>.Instance continue_action, ActionFactory<Triple<World, BlockPos, Direction>>.Instance found_action, ConditionFactory<CachedBlockPosition>.Instance block_condition) {
        super(mechanicType, blockEntity);
        this.action = action;
        this.continue_action = continue_action;
        this.found_action = found_action;
        this.continue_tag = continue_tag;
        this.execute_tag = execute_tag;
        this.block_condition = block_condition;
    }

    @Override
    public void fromTag(NbtElement tag) {

    }

    public void executeAction(Triple<World, BlockPos, Direction> data) {
        World world = data.getLeft();
        BlockPos blockPos = data.getMiddle();
        BlockState blockState = world.getBlockState(blockPos);
        List<Direction> directions = Lists.newArrayList();
        blockState.getProperties().forEach(property -> {
            if(property.getName().equals("north")){
               directions.add(Direction.NORTH);
            } if(property.getName().equals("south")){
                directions.add(Direction.SOUTH);
            } if(property.getName().equals("east")){
                directions.add(Direction.EAST);
            } if(property.getName().equals("west")){
                directions.add(Direction.WEST);
            } if(property.getName().equals("up")){
                directions.add(Direction.UP);
            } if(property.getName().equals("down")){
                directions.add(Direction.DOWN);
            }
        });
        for (Direction direction : directions) {
            BlockPos blockPos2 = blockPos.offset(direction);
            BlockEntity blockEntity = world.getBlockEntity(blockPos2);
            if (blockEntity instanceof DDBlockEntity ddBlockEntity) {
                boolean cont = true;
                for(DDFindBlockMechanic mechanic : BlockMechanicHolder.KEY.get(ddBlockEntity).getMechanics(DDFindBlockMechanic.class)) {
                    if(mechanic.visited) {
                        cont = false;
                    }
                }
                if(!cont) {
                    break;
                }
                CCPacksMain.LOGGER.info(cont);

                Triple data2 = Triple.of(world, blockPos2, direction);
                if (world.getBlockState(blockPos2).isIn(this.execute_tag)) {
                    CCPacksMain.LOGGER.info("execute");
                    this.visited = true;
                    if (this.action != null) {
                        this.action.accept(data);
                    }
                    if (this.found_action != null) {
                        this.found_action.accept(data2);
                    }
                }
                if (world.getBlockState(blockPos2).isIn(this.continue_tag)) {
                    CCPacksMain.LOGGER.info("continue");
                    this.visited = true;
                    this.continue_action.accept(data2);
                }
            }
            this.visited = false;
        }

    }

}
