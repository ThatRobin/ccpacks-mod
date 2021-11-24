package io.github.ThatRobin.ccpacks.Util;

import io.github.ThatRobin.ccpacks.Factories.MechanicFactories.MechanicRegistry;
import io.github.ThatRobin.ccpacks.Factories.MechanicFactories.MechanicType;
import net.minecraft.util.Identifier;

import java.util.List;

public class BlockEntityHolder {

    public Identifier id;
    public List<Identifier> mechanics;

    public BlockEntityHolder(Identifier id, List<Identifier> mechanics) {
        this.id = id;
        this.mechanics = mechanics;
    }
}
