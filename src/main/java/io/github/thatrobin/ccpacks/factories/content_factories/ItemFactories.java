package io.github.thatrobin.ccpacks.factories.content_factories;

import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.thatrobin.ccpacks.CCPackDataTypes;
import io.github.thatrobin.ccpacks.choice.ChoiceLayer;
import io.github.thatrobin.ccpacks.choice.ChoiceLayers;
import io.github.thatrobin.ccpacks.client.renderer.item.DDShieldItemRenderer;
import io.github.thatrobin.ccpacks.compat.TrinketsCompat;
import io.github.thatrobin.ccpacks.data_driven_classes.items.*;
import io.github.thatrobin.ccpacks.registries.CCPacksRegistries;
import io.github.thatrobin.ccpacks.util.ItemGroups;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.compress.utils.Lists;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

import java.util.List;
import java.util.function.Supplier;

public class ItemFactories {

    public static Identifier identifier(String string) {
        return new Identifier("item", string);
    }

    public static void register() {
        register(new ContentFactory<>(identifier("generic"), Types.ITEM,
                new SerializableData()
                        .add("name", SerializableDataTypes.TEXT, null)
                        .add("lore", CCPackDataTypes.STRINGS, null)
                        .add("item_powers", CCPackDataTypes.STACK_POWERS, null)
                        .add("max_count", SerializableDataTypes.INT, 64)
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroups.MISC)
                        .add("fuel_tick", SerializableDataTypes.INT, 0),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            ItemGroups group = data.get("item_group");
                            if(group != ItemGroups.NONE) {
                                settings.group(group.group);
                            }
                            Supplier<Item> EXAMPLE_ITEM = () -> new DDItem(settings.maxCount(data.getInt("max_count")), data.get("name"), data.get("lore"), data.get("item_powers"));
                            data.ifPresent("fuel_tick", (tick) -> FuelRegistry.INSTANCE.add(EXAMPLE_ITEM.get(), (Integer) tick));
                            return EXAMPLE_ITEM;
                        }));

        register(new ContentFactory<>(identifier("durable"), Types.ITEM,
                new SerializableData()
                        .add("name", SerializableDataTypes.TEXT, null)
                        .add("lore", CCPackDataTypes.STRINGS, null)
                        .add("item_powers", CCPackDataTypes.STACK_POWERS, null)
                        .add("durability", SerializableDataTypes.INT, 100)
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroups.MISC)
                        .add("start_colour", CCPackDataTypes.COLOUR, null)
                        .add("end_colour", CCPackDataTypes.COLOUR, null)
                        .add("fuel_tick", SerializableDataTypes.INT, 0),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            data.ifPresent("durability", settings::maxDamage);
                            ItemGroups group = data.get("item_group");
                            if(group != ItemGroups.NONE) {
                                settings.group(group.group);
                            }
                            Supplier<Item> EXAMPLE_ITEM = () -> new DDItem(settings, data.get("name"), data.get("lore"), data.get("start_colour"), data.get("end_colour"), data.get("item_powers"));
                            data.<Integer>ifPresent("fuel_tick", (tick) -> FuelRegistry.INSTANCE.add(EXAMPLE_ITEM.get(), tick));
                            return EXAMPLE_ITEM;
                        }));

        register(new ContentFactory<>(identifier("dyeable"), Types.ITEM,
                new SerializableData()
                        .add("name", SerializableDataTypes.TEXT, null)
                        .add("lore", CCPackDataTypes.STRINGS, null)
                        .add("item_powers", CCPackDataTypes.STACK_POWERS, null)
                        .add("max_count", SerializableDataTypes.INT, 64)
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroups.MISC)
                        .add("fuel_tick", SerializableDataTypes.INT, 0),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            ItemGroups group = data.get("item_group");
                            settings.group(group.group);
                            Supplier<Item> EXAMPLE_ITEM = () -> new DDDyeableItem(settings.maxCount(data.getInt("max_count")), data.get("name"), data.get("lore"), data.get("item_powers"));
                            data.ifPresent("fuel_tick", (tick) -> FuelRegistry.INSTANCE.add(EXAMPLE_ITEM.get(), (Integer) tick));
                            return EXAMPLE_ITEM;
                        }));

        register(new ContentFactory<>(identifier("trinket"), Types.ITEM,
                new SerializableData()
                        .add("name", SerializableDataTypes.TEXT, null)
                        .add("lore", CCPackDataTypes.STRINGS, null)
                        .add("item_powers", CCPackDataTypes.TRINKET_POWERS, null)
                        .add("durability", SerializableDataTypes.INT, 1)
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroups.MISC)
                        .add("start_colour", CCPackDataTypes.COLOUR, null)
                        .add("end_colour", CCPackDataTypes.COLOUR, null)
                        .add("fuel_tick", SerializableDataTypes.INT, 0),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            data.ifPresent("durability", settings::maxDamage);
                            ItemGroups group = data.get("item_group");
                            if(group != ItemGroups.NONE) {
                                settings.group(group.group);
                            }
                            Supplier<Item> EXAMPLE_ITEM;
                            if(FabricLoader.getInstance().isModLoaded("trinkets")) {
                                EXAMPLE_ITEM = TrinketsCompat.createTrinketItem(settings, data);
                            } else {
                                List<String> loreText = Lists.newArrayList();
                                loreText.add("Trinkets is not installed, this item is not a trinket");
                                EXAMPLE_ITEM = () -> new DDItem(settings, data.get("name"), loreText, data.get("item_powers"));
                            }

                            data.<Integer>ifPresent("fuel_tick", (tick) -> FuelRegistry.INSTANCE.add(EXAMPLE_ITEM.get(), tick));
                            return EXAMPLE_ITEM;
                        }));

        register(new ContentFactory<>(identifier("pullback"), Types.ITEM,
                new SerializableData()
                        .add("name", SerializableDataTypes.TEXT, null)
                        .add("lore", CCPackDataTypes.STRINGS, null)
                        .add("item_powers", CCPackDataTypes.STACK_POWERS, null)
                        .add("durability", SerializableDataTypes.INT, 1)
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroups.MISC)
                        .add("start_colour", CCPackDataTypes.COLOUR, null)
                        .add("end_colour", CCPackDataTypes.COLOUR, null)
                        .add("fuel_tick", SerializableDataTypes.INT, 0)
                        .add("entity_type", SerializableDataTypes.IDENTIFIER, null)
                        .add("max_speed", SerializableDataTypes.FLOAT, 3f),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            ItemGroups group = data.get("item_group");
                            if(group != ItemGroups.NONE) {
                                settings.group(group.group);
                            }
                            Supplier<Item> EXAMPLE_ITEM = () -> new DDPullbackItem(settings.maxDamage(data.getInt("durability")), data.get("name"), data.get("lore"), data.get("item_powers"), data.getId("entity_type"), data.getFloat("max_speed"));
                            data.ifPresent("fuel_tick", (tick) -> FuelRegistry.INSTANCE.add(EXAMPLE_ITEM.get(), (Integer) tick));
                            return EXAMPLE_ITEM;
                        }));

        register(new ContentFactory<>(identifier("sword"), Types.ITEM,
                new SerializableData()
                        .add("name", SerializableDataTypes.TEXT, null)
                        .add("lore", CCPackDataTypes.STRINGS, null)
                        .add("item_powers", CCPackDataTypes.STACK_POWERS, null)
                        .add("durability", SerializableDataTypes.INT, 100)
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroups.MISC)
                        .add("start_colour", CCPackDataTypes.COLOUR, null)
                        .add("end_colour", CCPackDataTypes.COLOUR, null)
                        .add("fuel_tick", SerializableDataTypes.INT, 0)
                        .add("attack_speed",SerializableDataTypes.INT, 0)
                        .add("attack_damage",SerializableDataTypes.INT, 0)
                        .add("enchantability",SerializableDataTypes.INT, 0)
                        .add("repair_item",SerializableDataTypes.ITEM,null),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            ItemGroups group = data.get("item_group");
                            if(group != ItemGroups.NONE) {
                                settings.group(group.group);
                            }
                            Supplier<Item> EXAMPLE_ITEM = () -> new DDSwordItem(new DDToolMaterial(data.getInt("durability"), 0, 0, 0, data.getInt("enchantability"), data.getId("repair_item")), data.getInt("attack_damage"), 0, settings, data.get("lore"), data.get("start_colour"), data.get("end_colour"), data.get("item_powers"), data.get("name"));
                            data.<Integer>ifPresent("fuel_tick", (tick) -> FuelRegistry.INSTANCE.add(EXAMPLE_ITEM.get(), tick));
                            return EXAMPLE_ITEM;
                        }));

        register(new ContentFactory<>(identifier("pickaxe"), Types.ITEM,
                new SerializableData()
                        .add("name", SerializableDataTypes.TEXT, null)
                        .add("lore", CCPackDataTypes.STRINGS, null)
                        .add("item_powers", CCPackDataTypes.STACK_POWERS, null)
                        .add("durability", SerializableDataTypes.INT, 100)
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroups.MISC)
                        .add("start_colour", CCPackDataTypes.COLOUR, null)
                        .add("end_colour", CCPackDataTypes.COLOUR, null)
                        .add("fuel_tick", SerializableDataTypes.INT, 0)
                        .add("mining_speed_multiplier",SerializableDataTypes.FLOAT, 0f)
                        .add("attack_damage",SerializableDataTypes.INT, 0)
                        .add("attack_speed",SerializableDataTypes.INT, 0)
                        .add("mining_level",SerializableDataTypes.INT, 0)
                        .add("enchantability",SerializableDataTypes.INT, 0)
                        .add("repair_item",SerializableDataTypes.ITEM_STACK,null),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            ItemGroups group = data.get("item_group");
                            if(group != ItemGroups.NONE) {
                                settings.group(group.group);
                            }
                            Supplier<Item> EXAMPLE_ITEM = () -> new DDPickaxeItem(new DDToolMaterial(data.getInt("durability"), data.getFloat("mining_speed_multiplier"), data.getInt("attack_damage"), data.getInt("mining_level"), data.getInt("enchantability"), data.getId("repair_item")), 0, 0, settings, data.get("lore"), data.get("start_colour"), data.get("end_colour"), data.get("item_powers"), data.get("name"));
                            data.<Integer>ifPresent("fuel_tick", (tick) -> FuelRegistry.INSTANCE.add(EXAMPLE_ITEM.get(), tick));
                            return EXAMPLE_ITEM;
                        }));

        register(new ContentFactory<>(identifier("axe"), Types.ITEM,
                new SerializableData()
                        .add("name", SerializableDataTypes.TEXT, null)
                        .add("lore", CCPackDataTypes.STRINGS, null)
                        .add("item_powers", CCPackDataTypes.STACK_POWERS, null)
                        .add("durability", SerializableDataTypes.INT, 100)
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroups.MISC)
                        .add("start_colour", CCPackDataTypes.COLOUR, null)
                        .add("end_colour", CCPackDataTypes.COLOUR, null)
                        .add("fuel_tick", SerializableDataTypes.INT, 0)
                        .add("mining_speed_multiplier",SerializableDataTypes.FLOAT, 0f)
                        .add("attack_speed",SerializableDataTypes.INT, 0)
                        .add("mining_level",SerializableDataTypes.INT, 0)
                        .add("enchantability",SerializableDataTypes.INT, 0)
                        .add("attack_damage",SerializableDataTypes.INT, 0)
                        .add("repair_item",SerializableDataTypes.ITEM_STACK,null),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            ItemGroups group = data.get("item_group");
                            if(group != ItemGroups.NONE) {
                                settings.group(group.group);
                            }
                            Supplier<Item> EXAMPLE_ITEM = () -> new DDAxeItem(new DDToolMaterial(data.getInt("durability"), data.getFloat("mining_speed_multiplier"), data.getInt("attack_damage"), data.getInt("mining_level"), data.getInt("enchantability"), data.getId("repair_item")), 0, 0, settings, data.get("lore"), data.get("start_colour"), data.get("end_colour"), data.get("item_powers"), data.get("name"));
                            data.<Integer>ifPresent("fuel_tick", (tick) -> FuelRegistry.INSTANCE.add(EXAMPLE_ITEM.get(), tick));
                            return EXAMPLE_ITEM;
                        }));

        register(new ContentFactory<>(identifier("shovel"), Types.ITEM,
                new SerializableData()
                        .add("name", SerializableDataTypes.TEXT, null)
                        .add("lore", CCPackDataTypes.STRINGS, null)
                        .add("item_powers", CCPackDataTypes.STACK_POWERS, null)
                        .add("durability", SerializableDataTypes.INT, 100)
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroups.MISC)
                        .add("start_colour", CCPackDataTypes.COLOUR, null)
                        .add("end_colour", CCPackDataTypes.COLOUR, null)
                        .add("fuel_tick", SerializableDataTypes.INT, 0)
                        .add("mining_speed_multiplier",SerializableDataTypes.FLOAT, 0f)
                        .add("attack_speed",SerializableDataTypes.INT, 0)
                        .add("mining_level",SerializableDataTypes.INT, 0)
                        .add("enchantability",SerializableDataTypes.INT, 0)
                        .add("attack_damage",SerializableDataTypes.INT, 0)
                        .add("repair_item",SerializableDataTypes.ITEM_STACK,null),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            ItemGroups group = data.get("item_group");
                            if(group != ItemGroups.NONE) {
                                settings.group(group.group);
                            }
                            Supplier<Item> EXAMPLE_ITEM = () -> new DDShovelItem(new DDToolMaterial(data.getInt("durability"), data.getFloat("mining_speed_multiplier"), data.getInt("attack_damage"), data.getInt("mining_level"), data.getInt("enchantability"), data.getId("repair_item")), 0, 0, settings.maxCount(1), data.get("lore"), data.get("start_colour"), data.get("end_colour"), data.get("item_powers"), data.get("name"));
                            data.<Integer>ifPresent("fuel_tick", (tick) -> FuelRegistry.INSTANCE.add(EXAMPLE_ITEM.get(), tick));
                            return EXAMPLE_ITEM;
                        }));

        register(new ContentFactory<>(identifier("hoe"), Types.ITEM,
                new SerializableData()
                        .add("name", SerializableDataTypes.TEXT, null)
                        .add("lore", CCPackDataTypes.STRINGS, null)
                        .add("item_powers", CCPackDataTypes.STACK_POWERS, null)
                        .add("durability", SerializableDataTypes.INT, 100)
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroups.MISC)
                        .add("start_colour", CCPackDataTypes.COLOUR, null)
                        .add("end_colour", CCPackDataTypes.COLOUR, null)
                        .add("fuel_tick", SerializableDataTypes.INT, 0)
                        .add("mining_speed_multiplier",SerializableDataTypes.FLOAT, 0f)
                        .add("attack_speed",SerializableDataTypes.INT, 0)
                        .add("mining_level",SerializableDataTypes.INT, 0)
                        .add("enchantability",SerializableDataTypes.INT, 0)
                        .add("attack_damage",SerializableDataTypes.INT, 0)
                        .add("repair_item",SerializableDataTypes.ITEM_STACK,null),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            ItemGroups group = data.get("item_group");
                            if(group != ItemGroups.NONE) {
                                settings.group(group.group);
                            }
                            Supplier<Item> EXAMPLE_ITEM = () -> new DDHoeItem(new DDToolMaterial(data.getInt("durability"), data.getFloat("mining_speed_multiplier"), data.getInt("attack_damage"), data.getInt("mining_level"), data.getInt("enchantability"), data.getId("repair_item")), 0, 0, settings, data.get("lore"), data.get("start_colour"), data.get("end_colour"), data.get("item_powers"), data.get("name"));
                            data.<Integer>ifPresent("fuel_tick", (tick) -> FuelRegistry.INSTANCE.add(EXAMPLE_ITEM.get(), tick));
                            return EXAMPLE_ITEM;
                        }));

        register(new ContentFactory<>(identifier("food"), Types.ITEM,
                new SerializableData()
                        .add("name", SerializableDataTypes.TEXT, null)
                        .add("lore", CCPackDataTypes.STRINGS, null)
                        .add("item_powers", CCPackDataTypes.STACK_POWERS, null)
                        .add("max_count", SerializableDataTypes.INT, 64)
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroups.MISC)
                        .add("fuel_tick", SerializableDataTypes.INT, 0)
                        .add("hunger",SerializableDataTypes.INT, 4)
                        .add("saturation",SerializableDataTypes.FLOAT, 8f)
                        .add("meat",SerializableDataTypes.BOOLEAN, false)
                        .add("always_edible",SerializableDataTypes.BOOLEAN, false)
                        .add("drinkable", SerializableDataTypes.BOOLEAN, false)
                        .add("sound", SerializableDataTypes.SOUND_EVENT, SoundEvents.ENTITY_GENERIC_EAT)
                        .add("returns", SerializableDataTypes.ITEM, null)
                        .add("eating_time", SerializableDataTypes.INT, 30),
                data ->
                        (contentType,content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            FoodComponent.Builder food = new FoodComponent.Builder().hunger(data.getInt("hunger")).saturationModifier(data.getFloat("saturation"));
                            ItemGroups group = data.get("item_group");
                            if(group != ItemGroups.NONE) {
                                settings.group(group.group);
                            }
                            data.<Boolean>ifPresent("meat", aBoolean -> food.meat());
                            data.ifPresent("always_edible", aBoolean -> food.alwaysEdible());
                            FoodComponent foodComp = food.build();
                            Supplier<Item> EXAMPLE_ITEM = () -> new DDFoodItem(settings.food(foodComp).maxCount(data.getInt("max_count")), data.getBoolean("drinkable"), data.get("sound"), data.get("returns"), data.getInt("eating_time"), data.get("lore"), data.get("item_powers"), data.get("name"));
                            data.<Integer>ifPresent("fuel_tick", (tick) -> FuelRegistry.INSTANCE.add(EXAMPLE_ITEM.get(), tick));
                            return EXAMPLE_ITEM;
                        }));

        register(new ContentFactory<>(identifier("armor"), Types.ITEM,
                new SerializableData()
                        .add("name", SerializableDataTypes.TEXT, null)
                        .add("lore", CCPackDataTypes.STRINGS, null)
                        .add("item_powers", CCPackDataTypes.STACK_POWERS, null)
                        .add("durability", SerializableDataTypes.INT, 10)
                        .add("slot", SerializableDataType.enumValue(EquipmentSlot.class), EquipmentSlot.HEAD)
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroups.MISC)
                        .add("start_colour", CCPackDataTypes.COLOUR, null)
                        .add("end_colour", CCPackDataTypes.COLOUR, null)
                        .add("fuel_tick", SerializableDataTypes.INT, 0)
                        .add("texture", SerializableDataTypes.STRING)
                        .add("protection",SerializableDataTypes.INT, 0)
                        .add("toughness",SerializableDataTypes.INT, 0)
                        .add("knockback_resistance",SerializableDataTypes.INT, 0)
                        .add("enchantability",SerializableDataTypes.INT, 0)
                        .add("repair_item", SerializableDataTypes.ITEM, null),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            ItemGroups group = data.get("item_group");
                            if(group != ItemGroups.NONE) {
                                settings.group(group.group);
                            }
                            DDArmorMaterial CUSTOM_MATERIAL = new DDArmorMaterial(data.getInt("durability"), data.getInt("protection"), data.getInt("enchantability"), data.getInt("toughness"), data.getInt("knockback_resistance"), data.getString("texture"), data.getId("repair_item"));
                            Supplier<Item> EXAMPLE_ITEM = () -> new DDArmorItem(CUSTOM_MATERIAL, data.get("slot"), settings, data.get("lore"), data.get("item_powers"), data.get("name"), data.get("start_colour"), data.get("end_colour"));
                            data.<Integer>ifPresent("fuel_tick", (tick) -> FuelRegistry.INSTANCE.add(EXAMPLE_ITEM.get(), tick));
                            return EXAMPLE_ITEM;
                        }));

        register(new ContentFactory<>(identifier("music_disc"), Types.ITEM,
                new SerializableData()
                        .add("name", SerializableDataTypes.TEXT, null)
                        .add("lore", CCPackDataTypes.STRINGS, null)
                        .add("item_powers", CCPackDataTypes.STACK_POWERS, null)
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroups.MISC)
                        .add("fuel_tick", SerializableDataTypes.INT, 0)
                        .add("comparator_output", SerializableDataTypes.INT, 1)
                        .add("sound", SerializableDataTypes.SOUND_EVENT),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            ItemGroups group = data.get("item_group");
                            if(group != ItemGroups.NONE) {
                                settings.group(group.group);
                            }
                            Supplier<Item> EXAMPLE_ITEM = () -> new DDMusicDiscItem(data.getInt("comparator_output"), data.get("sound"), (settings.maxCount(1)), data.get("item_powers"), data.get("name"), data.get("lore"));
                            data.<Integer>ifPresent("fuel_tick", (tick) -> FuelRegistry.INSTANCE.add(EXAMPLE_ITEM.get(), tick));
                            return EXAMPLE_ITEM;
                        }));

        register(new ContentFactory<>(identifier("shield"), Types.ITEM,
                new SerializableData()
                        .add("name", SerializableDataTypes.TEXT, null)
                        .add("lore", CCPackDataTypes.STRINGS, null)
                        .add("item_powers", CCPackDataTypes.STACK_POWERS, null)
                        .add("durability", SerializableDataTypes.INT, 100)
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroups.MISC)
                        .add("start_colour", CCPackDataTypes.COLOUR, null)
                        .add("end_colour", CCPackDataTypes.COLOUR, null)
                        .add("fuel_tick", SerializableDataTypes.INT, 0)
                        .add("cooldown", SerializableDataTypes.INT, 60)
                        .add("enchantability",SerializableDataTypes.INT, 0)
                        .add("repair_item", SerializableDataTypes.ITEM, null),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            ItemGroups group = data.get("item_group");
                            if(group != ItemGroups.NONE) {
                                settings.group(group.group);
                            }
                            Supplier<Item> EXAMPLE_ITEM = () -> new DDShieldItem(settings, data.getInt("cooldown"), new DDToolMaterial(data.getInt("durability"), 1, 0, 0, data.getInt("enchantability"), data.getId("repair_item")), data.get("item_powers"), data.get("name"), data.get("lore"), data.get("start_colour"), data.get("end_colour"));
                            GeoItemRenderer.registerItemRenderer(EXAMPLE_ITEM.get(), new DDShieldItemRenderer());
                            data.ifPresent("fuel_tick", (tick) -> FuelRegistry.INSTANCE.add(EXAMPLE_ITEM.get(), (Integer) tick));
                            return EXAMPLE_ITEM;
                        }));
    }

    private static void register(ContentFactory<Supplier<?>> serializer) {
        Registry.register(CCPacksRegistries.CONTENT_FACTORY, serializer.getSerializerId(), serializer);
    }
}
