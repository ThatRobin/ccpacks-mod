package io.github.thatrobin.ccpacks.util;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

import java.util.List;

public class BlockItemHolder {

    public FabricItemSettings settings;
    public LiteralText name;
    public List<String> lore;
    public List<StackPowerExpansion> item_powers;
    public int fuel_tick;

    public BlockItemHolder(FabricItemSettings settings, LiteralText name, List<String> lore, List<StackPowerExpansion> item_powers, int fuel_tick) {
        this.settings = settings;
        this.name = name;
        this.lore = lore;
        this.item_powers = item_powers;
        this.fuel_tick = fuel_tick;
    }
}
