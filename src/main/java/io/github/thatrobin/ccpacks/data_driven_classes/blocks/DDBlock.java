package io.github.thatrobin.ccpacks.data_driven_classes.blocks;

import com.google.common.collect.Lists;
import io.github.thatrobin.ccpacks.component.BlockMechanicHolder;
import io.github.thatrobin.ccpacks.data_driven_classes.mechanics.*;
import io.github.thatrobin.ccpacks.factories.mechanic_factories.MechanicTypeReference;
import io.github.thatrobin.ccpacks.util.VoxelInfo;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class DDBlock extends BlockWithEntity {

    public BlockEntityType<DDBlockEntity> type;
    public List<VoxelInfo> voxelInfoList;
    public List<MechanicTypeReference> mechanicTypeReferences = Lists.newArrayList();
    private BlockMechanicHolder component;

    public DDBlock(Settings settings, List<MechanicTypeReference> mechanicTypeReferences, List<VoxelInfo> voxelInfoList) {
        super(settings);
        this.voxelInfoList = voxelInfoList;
        this.mechanicTypeReferences = mechanicTypeReferences;
        //getNewBlockState();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        if(this.voxelInfoList != null) {
            for (VoxelInfo voxelInfo : this.voxelInfoList) {
                stateManager.add(voxelInfo.property);
            }
        }
    }

    public void getNewBlockState(){
        BlockState blockState = getStateManager().getDefaultState();
        if(this.voxelInfoList != null) {
            for (VoxelInfo voxelInfo : this.voxelInfoList) {
                blockState.with(voxelInfo.property, voxelInfo.base);
            }
        }
        setDefaultState(blockState);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        DDBlockEntity be = new DDBlockEntity(this.type, pos, getDefaultState());
        this.component = BlockMechanicHolder.KEY.get(be);
        if(this.mechanicTypeReferences != null) {
            this.mechanicTypeReferences.forEach(mechanicTypeReference -> {
                this.component.addMechanic(mechanicTypeReference.getReferencedPowerType());
            });
        }
        return be;
    }


    @Override
    @Deprecated
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        Triple data = Triple.of(world, pos, Direction.UP);
        for(DDUseMechanic ddUseMechanic : BlockMechanicHolder.KEY.get(this.type.get(world, pos)).getMechanics(DDUseMechanic.class)) {
            if (world.isClient) {
                return ActionResult.SUCCESS;
            } else {
                ddUseMechanic.executeBlockAction(data);
                ddUseMechanic.executeEntityAction(player);
                return ActionResult.CONSUME;
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        Triple data = Triple.of(world, pos, Direction.UP);
        AtomicReference<Float> damage = new AtomicReference<>(1.0f);
        BlockMechanicHolder.KEY.get(this.type.get(world, pos)).getMechanics(DDFallMechanic.class).forEach(ddFallMechanic -> {
            damage.set(ddFallMechanic.damage_multipler);
            ddFallMechanic.executeBlockAction(data);
            ddFallMechanic.executeEntityAction(entity);
        });
        entity.handleFallDamage(fallDistance, damage.get(), DamageSource.FALL);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        Triple data = Triple.of(world, pos, Direction.UP);
        BlockMechanicHolder.KEY.get(this.type.get(world, pos)).getMechanics(DDStepMechanic.class).forEach(ddStepMechanic -> {
            ddStepMechanic.executeBlockAction(data);
            ddStepMechanic.executeEntityAction(entity);
        });
    }

    @Override
    @Deprecated
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        BlockMechanicHolder.KEY.get(this.type.get(world, pos)).getMechanics(DDNeighourUpdateMechanic.class).forEach(ddNeighourUpdateMechanic -> {
            //if(direction == ddNeighourUpdateMechanic.dir) {
                Triple data = Triple.of(world, pos, Direction.UP);
                Triple data2 = Triple.of(world, neighborPos, Direction.UP);
                if(ddNeighourUpdateMechanic.block_action != null) {
                    ddNeighourUpdateMechanic.executeBlockAction(data);
                }
                if(ddNeighourUpdateMechanic.block_action2 != null) {
                    ddNeighourUpdateMechanic.executeNeighborAction(data2);
                }
            //}
        });
        return state;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, type, (world1, pos, state1, be) -> DDBlockEntity.tick(world1, pos, state1, (DDBlockEntity) be));
        //return checkType(type, type, (world1, pos, state1, be) -> DDBlockEntity.tick(world1, pos, state1, (DDBlockEntity) be));
    }

    @Override
    @Deprecated
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (this.voxelInfoList != null) {
            try {
                List<VoxelShape> shapes = Lists.newArrayList();
                for (Property<?> property : state.getProperties()) {
                    Object value = state.get(property);
                    for (VoxelInfo voxelInfo : this.voxelInfoList) {
                        if (property.getName().equals(voxelInfo.property.getName())) {
                            if (value instanceof Boolean bool) {
                                if (bool) {
                                    shapes.add(VoxelShapes.cuboid(voxelInfo.from.get(0) / 16, voxelInfo.from.get(1) / 16, voxelInfo.from.get(2) / 16, voxelInfo.to.get(0) / 16, voxelInfo.to.get(1) / 16, voxelInfo.to.get(2) / 16));
                                }
                            }
                        }
                    }
                }
                VoxelShape[] voxelShapes = new VoxelShape[shapes.size() - 1];

                VoxelShape first = null;
                for (int i = 0; i < shapes.size(); i++) {
                    if (i == 0) {
                        first = shapes.get(i);
                    } else {
                        voxelShapes[i - 1] = shapes.get(i);
                    }
                }

                return VoxelShapes.union(first, voxelShapes);
            } catch (Exception e) {
                return VoxelShapes.fullCube();
            }
        } else {
            return VoxelShapes.fullCube();
        }
    }
}
