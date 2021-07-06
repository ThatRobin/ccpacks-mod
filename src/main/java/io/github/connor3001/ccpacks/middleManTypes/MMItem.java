package io.github.connor3001.ccpacks.middleManTypes;

import io.github.apace100.apoli.power.PowerTypeReference;
import io.github.connor3001.ccpacks.dataDrivenTypes.DDItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.List;

public class MMItem {

    public MMItem(Identifier identifier, List<String> lore, Integer max_count, List<PowerTypeReference> power) {
        Item EXAMPLE_ITEM = new DDItem(new FabricItemSettings().maxCount(max_count).group(ItemGroup.MISC), identifier, power, lore);
        Registry.register(Registry.ITEM, identifier, EXAMPLE_ITEM);
    }
}