package io.github.ThatRobin.ccpacks.DataDrivenClasses.mechanics;

import io.github.ThatRobin.ccpacks.DataDrivenClasses.Blocks.DDBlock;
import io.github.ThatRobin.ccpacks.Factories.MechanicFactories.MechanicType;
import io.github.ThatRobin.ccpacks.Util.Mechanic;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtElement;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Triple;

public class DDConnectionMechanic extends Mechanic {

    private Tag<Block> tag;

    public DDConnectionMechanic(MechanicType<?> mechanicType, BlockEntity blockEntity, Tag<Block> tag) {
        super(mechanicType, blockEntity);
        this.tag = tag;
    }

    @Override
    public void fromTag(NbtElement tag) {

    }

    public boolean canConnect(BlockState state) {
        boolean bl = this.canConnectToFence(state);
        return bl;
    }

    private boolean canConnectToFence(BlockState state) {
        return state.getBlock() instanceof DDBlock;
    }
}
