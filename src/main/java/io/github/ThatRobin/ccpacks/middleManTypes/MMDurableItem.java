package io.github.ThatRobin.ccpacks.middleManTypes;

import io.github.ThatRobin.ccpacks.dataDrivenTypes.DDItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.List;

public class MMDurableItem {

    public MMDurableItem(Identifier identifier, List<String> lore, Integer durability) {
        Item EXAMPLE_ITEM = new DDItem(new FabricItemSettings().maxDamage(durability).group(ItemGroup.MISC), lore);
        Registry.register(Registry.ITEM, identifier, EXAMPLE_ITEM);
    }

}
