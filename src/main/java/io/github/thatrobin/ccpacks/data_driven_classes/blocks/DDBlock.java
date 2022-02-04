package io.github.thatrobin.ccpacks.data_driven_classes.blocks;

import io.github.thatrobin.ccpacks.component.BlockMechanicHolder;
import io.github.thatrobin.ccpacks.data_driven_classes.mechanics.DDFallMechanic;
import io.github.thatrobin.ccpacks.data_driven_classes.mechanics.DDNeighourUpdateMechanic;
import io.github.thatrobin.ccpacks.data_driven_classes.mechanics.DDStepMechanic;
import io.github.thatrobin.ccpacks.data_driven_classes.mechanics.DDUseMechanic;
import io.github.thatrobin.ccpacks.factories.mechanic_factories.MechanicTypeReference;
import io.github.thatrobin.ccpacks.util.VoxelInfo;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class DDBlock extends Block implements BlockEntityProvider {

    public BlockEntityType<DDBlockEntity> type;
    public List<VoxelInfo> voxelInfoList;
    public List<MechanicTypeReference> mechanicTypeReferences;
    private BlockMechanicHolder component;

    public DDBlock(Settings settings, List<MechanicTypeReference> mechanicTypeReferences, List<VoxelInfo> voxelInfoList) {
        super(settings);
        this.voxelInfoList = voxelInfoList;
        this.mechanicTypeReferences = mechanicTypeReferences;
        StateManager.Builder<Block, BlockState> builder = new StateManager.Builder<>(this);
        this.appendProperties(builder);
        BlockState blockState = this.stateManager.getDefaultState();
        if(voxelInfoList != null) {
            for (VoxelInfo voxelInfo : voxelInfoList) {
                blockState.with(voxelInfo.property, voxelInfo.base);
            }
        }
        this.setDefaultState(blockState);
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
            this.mechanicTypeReferences.forEach(mechanicTypeReference -> this.component.addMechanic(mechanicTypeReference.getReferencedPowerType()));
        }
        return be;
    }


    @Override
    @Deprecated
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        Triple<World, BlockPos, Direction> data = Triple.of(world, pos, Direction.UP);
        for(DDUseMechanic ddUseMechanic : BlockMechanicHolder.KEY.get(Objects.requireNonNull(this.type.get(world, pos))).getMechanics(DDUseMechanic.class)) {
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
        Triple<World, BlockPos, Direction> data = Triple.of(world, pos, Direction.UP);
        AtomicReference<Float> damage = new AtomicReference<>(1.0f);
        BlockMechanicHolder.KEY.get(Objects.requireNonNull(this.type.get(world, pos))).getMechanics(DDFallMechanic.class).forEach(ddFallMechanic -> {
            damage.set(ddFallMechanic.damage_multipler);
            ddFallMechanic.executeBlockAction(data);
            ddFallMechanic.executeEntityAction(entity);
        });
        entity.handleFallDamage(fallDistance, damage.get(), DamageSource.FALL);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        Triple<World, BlockPos, Direction> data = Triple.of(world, pos, Direction.UP);
        BlockMechanicHolder.KEY.get(Objects.requireNonNull(this.type.get(world, pos))).getMechanics(DDStepMechanic.class).forEach(ddStepMechanic -> {
            ddStepMechanic.executeBlockAction(data);
            ddStepMechanic.executeEntityAction(entity);
        });
    }

    @Override
    @Deprecated
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        BlockMechanicHolder.KEY.get(Objects.requireNonNull(this.type.get(world, pos))).getMechanics(DDNeighourUpdateMechanic.class).forEach(ddNeighourUpdateMechanic -> {
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
        return checkType(type, type, (world1, pos, state1, be) -> DDBlockEntity.tick(world1, pos, (DDBlockEntity) be));
        //return checkType(type, type, (world1, pos, state1, be) -> DDBlockEntity.tick(world1, pos, state1, (DDBlockEntity) be));
    }

    public boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
        super.onSyncedBlockEvent(state, world, pos, type, data);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity != null && blockEntity.onSyncedBlockEvent(type, data);
    }

    @Nullable
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity instanceof NamedScreenHandlerFactory ? (NamedScreenHandlerFactory)blockEntity : null;
    }

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> checkType(BlockEntityType<A> givenType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
        return expectedType == givenType ? (BlockEntityTicker<A>) ticker : null;
    }
}
