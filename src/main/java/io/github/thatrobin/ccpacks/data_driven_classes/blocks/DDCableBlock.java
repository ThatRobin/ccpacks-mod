package io.github.thatrobin.ccpacks.data_driven_classes.blocks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.github.thatrobin.ccpacks.component.BlockMechanicHolder;
import io.github.thatrobin.ccpacks.data_driven_classes.mechanics.DDFallMechanic;
import io.github.thatrobin.ccpacks.data_driven_classes.mechanics.DDStepMechanic;
import io.github.thatrobin.ccpacks.data_driven_classes.mechanics.DDUseMechanic;
import io.github.thatrobin.ccpacks.factories.mechanic_factories.MechanicTypeReference;
import io.github.thatrobin.ccpacks.util.VoxelInfo;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Property;
import net.minecraft.tag.Tag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
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
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class DDCableBlock extends BlockWithEntity {

    public BlockEntityType<DDBlockEntity> type;
    public Tag<Block> blockTag;
    public List<VoxelInfo> voxelInfoList;
    public static final BooleanProperty NORTH = BooleanProperty.of("north");
    public static final BooleanProperty EAST = BooleanProperty.of("east");
    public static final BooleanProperty SOUTH = BooleanProperty.of("south");
    public static final BooleanProperty WEST = BooleanProperty.of("west");
    public static final BooleanProperty UP = BooleanProperty.of("up");
    public static final BooleanProperty DOWN = BooleanProperty.of("down");
    public static final Map<Direction, BooleanProperty> FACING_PROPERTIES = ImmutableMap.copyOf((Map)Util.make(Maps.newEnumMap(Direction.class), (directions) -> {
        directions.put(Direction.NORTH, NORTH);
        directions.put(Direction.EAST, EAST);
        directions.put(Direction.SOUTH, SOUTH);
        directions.put(Direction.WEST, WEST);
        directions.put(Direction.UP, UP);
        directions.put(Direction.DOWN, DOWN);
    }));

    public List<MechanicTypeReference> mechanicTypeReferences = Lists.newArrayList();
    private BlockMechanicHolder component;

    public DDCableBlock(Settings settings, List<MechanicTypeReference> mechanicTypeReferences, List<VoxelInfo> voxelInfoList, Tag<Block> blockTag) {
        super(settings);
        this.voxelInfoList = voxelInfoList;
        this.blockTag = blockTag;
        this.mechanicTypeReferences = mechanicTypeReferences;
        StateManager.Builder<Block, BlockState> builder = new StateManager.Builder(this);
        this.appendProperties(builder);
        setDefaultState(getNewBlockState());
    }

    public BlockState getNewBlockState(){
        BlockState state = getStateManager().getDefaultState();
        state.with(NORTH, false);
        state.with(SOUTH, false);
        state.with(EAST, false);
        state.with(WEST, false);
        state.with(UP, false);
        state.with(DOWN, false);
        return state;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        DDBlockEntity be = new DDBlockEntity(this.type, pos, getNewBlockState());
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

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockView blockView = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();
        BlockPos blockPos2 = blockPos.north();
        BlockPos blockPos3 = blockPos.east();
        BlockPos blockPos4 = blockPos.south();
        BlockPos blockPos5 = blockPos.west();
        BlockPos blockPos6 = blockPos.up();
        BlockPos blockPos7 = blockPos.down();
        BlockState blockState2 = blockView.getBlockState(blockPos2);
        BlockState blockState3 = blockView.getBlockState(blockPos3);
        BlockState blockState4 = blockView.getBlockState(blockPos4);
        BlockState blockState5 = blockView.getBlockState(blockPos5);
        BlockState blockState6 = blockView.getBlockState(blockPos6);
        BlockState blockState7 = blockView.getBlockState(blockPos7);
        return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)super.getPlacementState(ctx).with(NORTH, this.canConnect(blockState2))).with(EAST, this.canConnect(blockState3))).with(SOUTH, this.canConnect(blockState4))).with(WEST, this.canConnect(blockState5)).with(UP, this.canConnect(blockState6)).with(DOWN,this.canConnect(blockState7)));
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
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(NORTH,SOUTH,EAST,WEST,UP,DOWN);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, type, (world1, pos, state1, be) -> DDBlockEntity.tick(world1, pos, state1, (DDBlockEntity) be));
        //return checkType(type, type, (world1, pos, state1, be) -> DDBlockEntity.tick(world1, pos, state1, (DDBlockEntity) be));
    }

    public boolean canConnect(BlockState state) {
        boolean bl = this.canConnectToFence(state);
        return bl;
    }

    private boolean canConnectToFence(BlockState state) {
        return state.isIn(blockTag);
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return (BlockState)state.with((Property)FACING_PROPERTIES.get(direction), this.canConnect(neighborState));
    }

    @Override
    @Deprecated
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        try {
            List<VoxelShape> voxelShapes = Lists.newArrayList();
            VoxelShape base = VoxelShapes.cuboid(6d/16d, 6d/16d, 6d/16d, 10d/16d, 10d/16d, 10d/16d);
            if (state.get(NORTH).booleanValue()) {
                voxelShapes.add(VoxelShapes.cuboid(6d/16d, 6d/16d, 0d/16d, 10d/16d, 10d/16d, 10d/16d));
            }
            if (state.get(SOUTH).booleanValue()) {
                voxelShapes.add(VoxelShapes.cuboid(6d/16d, 6d/16d, 6d/16d, 10d/16d, 10d/16d, 1.0d));
            }
            if (state.get(WEST).booleanValue()) {
                voxelShapes.add(VoxelShapes.cuboid(0d/16d, 6d/16d, 6d/16d, 10d/16d, 10d/16d, 10d/16));
            }
            if (state.get(EAST).booleanValue()) {
                voxelShapes.add(VoxelShapes.cuboid(6d / 16d, 6d / 16d, 6d / 16d, 1.0d, 10d / 16d, 10d / 16d));
            }
            if (state.get(UP).booleanValue()) {
                voxelShapes.add(VoxelShapes.cuboid(6d/16d, 6d/16d, 6d/16d, 10d/16d, 1.0d, 10d/16d));
            }
            if (state.get(DOWN).booleanValue()) {
                voxelShapes.add(VoxelShapes.cuboid(6d/16d, 0d/16d, 6d/16d, 10d/16d, 10d/16d, 10d/16d));
            }

            for(VoxelShape shape : voxelShapes) {
                base = VoxelShapes.union(base,shape);
            }

            return base;
        } catch (Exception e) {
            return VoxelShapes.fullCube();
        }
    }
}
