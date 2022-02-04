package io.github.thatrobin.ccpacks.factories;

import io.github.thatrobin.ccpacks.CCPacksMain;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class ItemActions {

    public static void register() {
        register(new ActionFactory<>(CCPacksMain.identifier("change_name"), new SerializableData()
                .add("name", SerializableDataTypes.STRING),
                (data, stack) -> stack.getRight().setCustomName(new TranslatableText(data.getString("name")))));
    }

    private static void register(ActionFactory<Pair<World, ItemStack>> actionFactory) {
        Registry.register(ApoliRegistries.ITEM_ACTION, actionFactory.getSerializerId(), actionFactory);
    }
}
