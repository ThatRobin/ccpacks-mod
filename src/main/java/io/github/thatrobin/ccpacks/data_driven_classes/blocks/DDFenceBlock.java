package io.github.thatrobin.ccpacks.data_driven_classes.blocks;

import io.github.thatrobin.ccpacks.component.BlockMechanicHolder;
import io.github.thatrobin.ccpacks.data_driven_classes.mechanics.DDFallMechanic;
import io.github.thatrobin.ccpacks.data_driven_classes.mechanics.DDNeighourUpdateMechanic;
import io.github.thatrobin.ccpacks.data_driven_classes.mechanics.DDStepMechanic;
import io.github.thatrobin.ccpacks.data_driven_classes.mechanics.DDUseMechanic;
import io.github.thatrobin.ccpacks.factories.mechanic_factories.MechanicTypeReference;
import io.github.thatrobin.ccpacks.util.IDDBlock;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class DDFenceBlock extends FenceBlock implements BlockEntityProvider, IDDBlock {

    public BlockEntityType<DDBlockEntity> type;
    public List<MechanicTypeReference> mechanicTypeReferences;
    private BlockMechanicHolder component;

    public DDFenceBlock(Settings settings, List<MechanicTypeReference> mechanicTypeReferences) {
        super(settings);
        this.mechanicTypeReferences = mechanicTypeReferences;
        StateManager.Builder<Block, BlockState> builder = new StateManager.Builder<>(this);
        this.appendProperties(builder);
    }

    @Override
    public boolean canConnect(BlockState state, boolean neighborIsFullSquare, Direction dir) {
        Block block = state.getBlock();
        boolean bl = this.canConnectToFence(state);
        boolean bl2 = block instanceof FenceGateBlock && FenceGateBlock.canWallConnect(state, dir);
        return !cannotConnect(state) && neighborIsFullSquare || bl || bl2;
    }

    private boolean canConnectToFence(BlockState state) {
        return state.isIn(BlockTags.FENCES) && state.isIn(BlockTags.WOODEN_FENCES) == this.getDefaultState().isIn(BlockTags.WOODEN_FENCES) || state.isOf(this);
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
        return super.onUse(state, world, pos, player, hand, hit);
    }

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

    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        Triple<World, BlockPos, Direction> data = Triple.of(world, pos, Direction.UP);
        BlockMechanicHolder.KEY.get(Objects.requireNonNull(this.type.get(world, pos))).getMechanics(DDStepMechanic.class).forEach(ddStepMechanic -> {
            ddStepMechanic.executeBlockAction(data);
            ddStepMechanic.executeEntityAction(entity);
        });
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        BlockMechanicHolder.KEY.get(Objects.requireNonNull(this.type.get(world, pos))).getMechanics(DDNeighourUpdateMechanic.class).forEach(ddNeighourUpdateMechanic -> {
            Triple data = Triple.of(world, pos, Direction.UP);
            Triple data2 = Triple.of(world, neighborPos, Direction.UP);
            ddNeighourUpdateMechanic.executeBlockAction(data);
            ddNeighourUpdateMechanic.executeNeighborAction(data2);
        });
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, type, (world1, pos, state1, be) -> DDBlockEntity.tick(world1, pos, (DDBlockEntity) be));
        //return checkType(type, type, (world1, pos, state1, be) -> DDBlockEntity.tick(world1, pos, state1, (DDBlockEntity) be));
    }

    @Override
    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
        return true;
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

    @Override
    public void setType(BlockEntityType<DDBlockEntity> blockEntityType) {
        this.type = blockEntityType;
    }
}
