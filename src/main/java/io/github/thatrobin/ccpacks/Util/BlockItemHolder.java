package io.github.thatrobin.ccpacks.util;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

import java.util.List;

public class BlockItemHolder {

    public FabricItemSettings settings;
    public LiteralText name;
    public List<String> lore;
    public List<Identifier> item_modifiers;
    public int fuel_tick;

    public BlockItemHolder(FabricItemSettings settings, LiteralText name, List<String> lore, List<Identifier> item_modifiers, int fuel_tick) {
        this.settings = settings;
        this.name = name;
        this.lore = lore;
        this.item_modifiers = item_modifiers;
        this.fuel_tick = fuel_tick;
    }
}
