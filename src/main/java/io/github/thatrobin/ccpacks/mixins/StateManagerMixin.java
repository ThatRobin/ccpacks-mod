package io.github.thatrobin.ccpacks.mixins;

import io.github.thatrobin.ccpacks.data_driven_classes.blocks.DDBlock;
import io.github.thatrobin.ccpacks.util.VoxelInfo;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class StateManagerMixin {

    @Mutable
    @Shadow @Final protected StateManager<Block, BlockState> stateManager;

    @Shadow public abstract BlockState getDefaultState();

    @Inject(method = "appendProperties", at = @At("HEAD"))
    private void test(StateManager.Builder<Block, BlockState> builder, CallbackInfo ci) {
        if((Block)(Object)this instanceof DDBlock block) {
            if(block.voxelInfoList != null) {
                for (VoxelInfo voxelInfo : block.voxelInfoList) {
                    builder.add(voxelInfo.property);
                }
            }
            this.stateManager = builder.build(Block::getDefaultState, BlockState::new);
        }

    }
}
