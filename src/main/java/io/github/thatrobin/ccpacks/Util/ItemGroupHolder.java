package io.github.thatrobin.ccpacks.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import org.spongepowered.include.com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

public class ItemGroupHolder {

    public ItemGroupHolder(JsonObject data) {
        this.data = data;
        this.icon = Identifier.tryParse(data.get("icon").getAsString());
    }

    public List<ItemStack> getHolder(){
        this.items.clear();

        JsonArray array = data.get("items").getAsJsonArray();
        array.forEach(stackData -> {
            if(stackData.isJsonObject()) {
                this.items.add(SerializableDataTypes.ITEM_STACK.read(stackData));
            } else {
                this.items.add(SerializableDataTypes.ITEM.read(stackData).getDefaultStack());
            }
        });
        return this.items;
    }

    public JsonObject data;
    public List<ItemStack> items = Lists.newArrayList();
    public Identifier icon = new Identifier("grass_block");

}
