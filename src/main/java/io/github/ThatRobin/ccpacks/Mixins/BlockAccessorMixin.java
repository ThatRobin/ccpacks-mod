package io.github.ThatRobin.ccpacks.Mixins;

import io.github.ThatRobin.ccpacks.DataDrivenClasses.Blocks.DDBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.state.StateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(Block.class)
public class BlockAccessorMixin {
    @Shadow
    @Mutable
    protected StateManager<Block, BlockState> stateManager;

    @Inject(method = "appendProperties", at = @At("HEAD"))
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder, CallbackInfo ci) {
        if((Block)(Object)this instanceof DDBlock ddBlock) {
            if(ddBlock.voxelInfoList != null) {
                ddBlock.voxelInfoList.forEach(voxelInfo -> {
                    builder.add(voxelInfo.property);
                });
            }
        }
        stateManager = builder.build(Block::getDefaultState, BlockState::new);
    }
}