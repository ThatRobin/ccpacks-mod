package io.github.ThatRobin.ccpacks.Util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.apace100.apoli.power.MultiplePowerType;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.EntityType;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Identifier;

import java.util.LinkedList;
import java.util.List;

public class VoxelInfo {

    public BooleanProperty property;
    public Boolean base;
    public List<Float> from;
    public List<Float> to;
}
