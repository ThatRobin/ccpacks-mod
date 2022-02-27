package io.github.thatrobin.ccpacks.util;

import io.github.thatrobin.ccpacks.data_driven_classes.blocks.DDBlockEntity;
import net.minecraft.block.entity.BlockEntityType;

public interface IDDBlock {

    void setType(BlockEntityType<DDBlockEntity> blockEntityType);
}
