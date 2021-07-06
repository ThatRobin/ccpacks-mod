package io.github.connor3001.ccpacks.SerializableData;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeReference;
import io.github.apace100.apoli.power.PowerTypes;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.apace100.calio.mixin.DamageSourceAccessor;
import io.github.connor3001.ccpacks.CCPacksMain;
import io.github.connor3001.ccpacks.Util.BlockNamePos;
import io.github.connor3001.ccpacks.Util.BlockShape;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class CCPackDataTypes {

    public static final SerializableDataType<List<String>> STRINGS =
            SerializableDataType.list(SerializableDataTypes.STRING);

    public static final SerializableDataType<List<PowerTypeReference>> POWER_TYPES =
            SerializableDataType.list(ApoliDataTypes.POWER_TYPE);

    public static final SerializableDataType<BlockNamePos> BLOCK_NAME_POS =
            SerializableDataType.compound(BlockNamePos.class, new SerializableData()
                    .add("block", SerializableDataTypes.BLOCK_TAG)
                    .add("pos_x", SerializableDataTypes.INT)
                    .add("pos_y", SerializableDataTypes.INT)
                    .add("pos_z", SerializableDataTypes.INT),
            (data) -> {
                BlockNamePos blockNamePos = new BlockNamePos(new BlockPos(data.getInt("pos_x"),data.getInt("pos_y"),data.getInt("pos_z")),(Block)data.get("block"));
                return blockNamePos;
            },
                    (data, bnp) -> {
                        SerializableData.Instance inst = data.new Instance();
                        inst.set("block", Blocks.STONE);
                        inst.set("pos_x", 0);
                        inst.set("pos_y", 0);
                        inst.set("pos_z", 0);
                        return inst;
                    });

    public static final SerializableDataType<BlockShape> SHAPE_BLOCK =
            SerializableDataType.compound(BlockShape.class, new SerializableData()
                            .add("start_x", SerializableDataTypes.INT)
                            .add("start_y", SerializableDataTypes.INT)
                            .add("start_z", SerializableDataTypes.INT)
                            .add("end_x", SerializableDataTypes.INT)
                            .add("end_y", SerializableDataTypes.INT)
                            .add("end_z", SerializableDataTypes.INT),
                    (data) -> {
                        BlockShape shape = new BlockShape(data.getInt("start_x"), data.getInt("start_y"), data.getInt("start_z"), data.getInt("end_x"), data.getInt("end_y"), data.getInt("end_z"));
                        return shape;
                    },
                    (data, bnp) -> {
                        SerializableData.Instance inst = data.new Instance();
                        inst.set("start_x", 0);
                        inst.set("start_y", 0);
                        inst.set("start_z", 0);
                        inst.set("end_x", 16);
                        inst.set("end_y", 16);
                        inst.set("end_z", 16);
                        return inst;
                    });

    public static final SerializableDataType<List<BlockNamePos>> STRUCTURE =
            SerializableDataType.list(CCPackDataTypes.BLOCK_NAME_POS);

}