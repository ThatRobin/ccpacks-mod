package io.github.thatrobin.ccpacks.data_driven_classes.blocks;

import io.github.thatrobin.ccpacks.component.BlockMechanicHolder;
import io.github.thatrobin.ccpacks.data_driven_classes.mechanics.DDTickMechanic;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Triple;

public class DDBlockEntity extends BlockEntity {

    int totalTicks;

    public DDBlockEntity(Identifier id, BlockPos pos, BlockState state) {
        super(Registry.BLOCK_ENTITY_TYPE.get(id), pos, state);
    }

    public DDBlockEntity(BlockEntityType<DDBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
    }

    public static void tick(World world, BlockPos pos, BlockState state, DDBlockEntity be) {
        be.totalTicks++;
        Triple data = Triple.of(world, pos, Direction.UP);
        BlockMechanicHolder.KEY.get(be).getMechanics(DDTickMechanic.class).forEach(tickMechanic -> {
            tickMechanic.tick(data, be.totalTicks);
        });
    }

}
