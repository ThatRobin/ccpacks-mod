package io.github.connor3001.ccpacks.CustomObjects;

import me.crimsondawn45.fabricshieldlib.lib.event.ShieldEvent;
import me.crimsondawn45.fabricshieldlib.lib.object.FabricShield;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CustomShield {

    public CustomShield(Item.Settings settings, String namespace, String id, int cooldown, int durability, int enchantability, ShieldEvent event, Item item){
        Registry.register(Registry.ITEM, new Identifier(namespace, id), new FabricShield(new Item.Settings().group(ItemGroup.COMBAT), cooldown, durability, enchantability, item));
    }
}
