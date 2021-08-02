package io.github.ThatRobin.ccpacks.Registry;

import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.power.factory.PowerFactory;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;

public class CCPacksRegistry {
    public static final Registry<ItemGroup> ITEM_GROUPS = FabricRegistryBuilder.createSimple(ItemGroup.class, CCPacksMain.identifier("item_groups")).buildAndRegister();
}
