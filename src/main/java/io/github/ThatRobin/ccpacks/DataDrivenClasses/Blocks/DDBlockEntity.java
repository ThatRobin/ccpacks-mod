package io.github.ThatRobin.ccpacks.DataDrivenClasses.Blocks;

import com.google.common.collect.Lists;
import io.github.ThatRobin.ccpacks.CCPackDataTypes;
import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.ThatRobin.ccpacks.Component.BlockMechanicHolder;
import io.github.ThatRobin.ccpacks.Component.BlockMechanicHolderImpl;
import io.github.ThatRobin.ccpacks.DataDrivenClasses.mechanics.DDTickMechanic;
import io.github.ThatRobin.ccpacks.Factories.MechanicFactories.MechanicRegistry;
import io.github.ThatRobin.ccpacks.Factories.MechanicFactories.MechanicType;
import io.github.ThatRobin.ccpacks.Util.BlockEntityHolder;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DDBlockEntity extends BlockEntity {

    int totalTicks;
    public Map<String, Integer> ints = Map.of();
    List<String> value = Lists.newArrayList();

    public DDBlockEntity(Identifier id, BlockPos pos, BlockState state) {
        super(Registry.BLOCK_ENTITY_TYPE.get(id), pos, state);
    }

    public DDBlockEntity(BlockEntityType<DDBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        return tag;
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
