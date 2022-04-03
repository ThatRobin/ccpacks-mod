package io.github.thatrobin.ccpacks.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.thatrobin.ccpacks.CCPacksMain;
import io.github.thatrobin.ccpacks.item_groups.Icon;
import io.github.thatrobin.ccpacks.item_groups.TabbedItemGroup;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.compress.utils.Sets;

import java.util.Set;

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
            return new TabbedItemGroup(id) {
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
                            TagKey<Item> items = getItemTagFromJson(jo);
                            addTab(Icon.of(Registry.ITEM.get(Identifier.tryParse(jo.get("icon").getAsString()))),field, items);
                        }
                    });
                }
            };
        }
        return null;
    }

    public TagKey<Item> getItemTagFromJson(JsonObject jo) {
        return SerializableDataTypes.ITEM_TAG.read(jo.get("items"));
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
