package io.github.thatrobin.ccpacks.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.thatrobin.ccpacks.CCPacksMain;
import io.wispforest.owo.itemgroup.Icon;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import org.apache.commons.compress.utils.Sets;
import org.spongepowered.include.com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ItemGroupHolder {

    public JsonObject data;
    public Identifier id;

    public ItemGroupHolder(Identifier id, JsonObject data) {
        this.data = data;
        this.id = id;
    }

    public ItemGroup getItemGroup() {
        if(data.get("type").getAsString().equals("item_group:generic")) {
            Set<Item> items = getItemsFromJson(data);

            return FabricItemGroupBuilder.create(id)
                    .icon(() -> new ItemStack(Registry.ITEM.get(Identifier.tryParse(data.get("icon").getAsString()))))
                    .appendItems(itemStacks -> {
                        items.forEach(item -> {
                            itemStacks.add(item.getDefaultStack());
                        });
                    })
                    .build();
        } else if (data.get("type").getAsString().equals("item_group:tabbed")) {
            return new OwoItemGroup(id) {
                @Override
                public ItemStack createIcon() {
                    return new ItemStack(Registry.ITEM.get(Identifier.tryParse(data.get("icon").getAsString())));
                }

                @Override
                protected void setup() {
                    data.entrySet().forEach(stringJsonElementEntry -> {
                        String field = stringJsonElementEntry.getKey();
                        JsonElement je = stringJsonElementEntry.getValue();
                        if(je.isJsonObject()) {
                            JsonObject jo = je.getAsJsonObject();
                            Set<Item> items = getItemsFromJson(jo);
                            addTab(Icon.of(Registry.ITEM.get(Identifier.tryParse(jo.get("icon").getAsString()))),field,Tag.of(items));
                        }
                    });
                }
            };
        }
        return null;
    }


    public Set<Item> getItemsFromJson(JsonObject jo){
        Set<Item> items = Sets.newHashSet();

        JsonArray array = jo.get("items").getAsJsonArray();
        array.forEach(stackData -> {
            ItemStack stack = null;
            if(stackData.isJsonObject()) {
                try {
                    stack = SerializableDataTypes.ITEM_STACK.read(stackData);
                } catch (Exception e) {
                    CCPacksMain.LOGGER.error("ItemStack field \"item\" is invalid, is the item registered?");
                }

            } else {
                try {
                    stack = SerializableDataTypes.ITEM.read(stackData).getDefaultStack();
                } catch (Exception e) {
                    CCPacksMain.LOGGER.error("\"item\" field is invalid, is the item registered?");
                }
            }
            if(stack != null) {
                items.add(stack.getItem());
            }
        });
        return items;
    }

}
