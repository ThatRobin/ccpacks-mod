package io.github.ThatRobin.ccpacks.middleManTypes;

import io.github.ThatRobin.ccpacks.dataDrivenTypes.DDTrinketItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.List;

public class MMTrinket {

    public MMTrinket(Identifier identifier, List<String> lore, Integer max_count) {
        DDTrinketItem EXAMPLE_ITEM = new DDTrinketItem(new FabricItemSettings().maxCount(max_count).group(ItemGroup.MISC), lore);
        Registry.register(Registry.ITEM, identifier, EXAMPLE_ITEM);
    }
}