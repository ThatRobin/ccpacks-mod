package io.github.ThatRobin.ccpacks.Factories.ContentFactories;

import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.ThatRobin.ccpacks.Registries.CCPacksRegistries;
import io.github.ThatRobin.ccpacks.Util.ColourHolder;
import io.github.ThatRobin.ccpacks.dataDrivenTypes.Items.*;
import io.github.ThatRobin.ccpacks.serializableData.CCPackDataTypes;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import me.crimsondawn45.fabricshieldlib.lib.object.FabricShield;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.List;
import java.util.function.Supplier;

public class ItemFactories {

    public static Identifier identifier(String string) {
        return new Identifier("item", string);
    }

    @SuppressWarnings("unchecked")
    public static void register() {
        register(new ContentFactory<>(CCPacksMain.identifier("generic"), Types.ITEM,
                new SerializableData()
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroup.FOOD)
                        .add("lore", CCPackDataTypes.STRINGS, null)
                        .add("fuel_tick", SerializableDataTypes.INT, 0)
                        .add("max_count", SerializableDataTypes.INT, 64),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            data.ifPresent("item_group", settings::group);
                            Supplier<Item> EXAMPLE_ITEM = () -> new DDItem(settings.maxCount(data.getInt("max_count")), (List<String>) data.get("lore"));
                            data.ifPresent("fuel_tick", (tick) -> FuelRegistry.INSTANCE.add(EXAMPLE_ITEM.get(), (Integer) tick));
                            return EXAMPLE_ITEM;
                        }));

        register(new ContentFactory<>(CCPacksMain.identifier("durable"), Types.ITEM,
                new SerializableData()
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroup.FOOD)
                        .add("lore", CCPackDataTypes.STRINGS, null)
                        .add("durability", SerializableDataTypes.INT, null)
                        .add("fuel_tick", SerializableDataTypes.INT, 0)
                        .add("start_color", CCPackDataTypes.COLOR, null)
                        .add("end_color", CCPackDataTypes.COLOR, null)
                        .add("max_count", SerializableDataTypes.INT, 64),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            data.ifPresent("durability", settings::maxDamage);
                            data.ifPresent("item_group", settings::group);
                            Supplier<Item> EXAMPLE_ITEM = () -> new DDItem(settings, (List<String>) data.get("lore"), (ColourHolder) data.get("start_color"), (ColourHolder) data.get("end_color"));
                            data.<Integer>ifPresent("fuel_tick", (tick) -> FuelRegistry.INSTANCE.add(EXAMPLE_ITEM.get(), tick));
                            return EXAMPLE_ITEM;
                        }));

        register(new ContentFactory<>(CCPacksMain.identifier("trinket"), Types.ITEM,
                new SerializableData()
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroup.FOOD)
                        .add("lore", CCPackDataTypes.STRINGS, null)
                        .add("durability", SerializableDataTypes.INT, null)
                        .add("fuel_tick", SerializableDataTypes.INT, 0)
                        .add("start_color", CCPackDataTypes.COLOR, null)
                        .add("end_color", CCPackDataTypes.COLOR, null)
                        .add("max_count", SerializableDataTypes.INT, 1),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            data.ifPresent("durability", settings::maxDamage);
                            data.ifPresent("item_group", settings::group);
                            Supplier<Item> EXAMPLE_ITEM = () -> new DDTrinketItem(settings.maxDamage(data.getInt("durability")), (List<String>) data.get("lore"), (ColourHolder) data.get("start_color"), (ColourHolder) data.get("end_color"));

                            data.<Integer>ifPresent("fuel_tick", (tick) -> FuelRegistry.INSTANCE.add(EXAMPLE_ITEM.get(), tick));
                            return EXAMPLE_ITEM;
                        }));

        register(new ContentFactory<>(CCPacksMain.identifier("sword"), Types.ITEM,
                new SerializableData()
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroup.FOOD)
                        .add("durability", SerializableDataTypes.INT, 100)
                        .add("fuel_tick", SerializableDataTypes.INT, 0)
                        .add("mining_speed_multiplier",SerializableDataTypes.FLOAT, 0f)
                        .add("attack_speed",SerializableDataTypes.INT, 0)
                        .add("mining_level",SerializableDataTypes.INT, 0)
                        .add("enchantability",SerializableDataTypes.INT, 0)
                        .add("attack_damage",SerializableDataTypes.INT, 0)
                        .add("repair_item",SerializableDataTypes.ITEM_STACK,null)
                        .add("lore", CCPackDataTypes.STRINGS, null),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            data.ifPresent("item_group", settings::group);
                            Supplier<Item> EXAMPLE_ITEM = () -> new DDSwordItem(new DDToolMaterial(data.getInt("durability"), data.getFloat("mining_speed_multiplier"), 0, data.getInt("mining_level"), data.getInt("enchantability"), (ItemStack)data.get("repair_item")), data.getInt("attack_damage"), 0, settings, (List<String>) data.get("lore"));
                            data.<Integer>ifPresent("fuel_tick", (tick) -> FuelRegistry.INSTANCE.add(EXAMPLE_ITEM.get(), tick));
                            return EXAMPLE_ITEM;
                        }));

        register(new ContentFactory<>(CCPacksMain.identifier("pickaxe"), Types.ITEM,
                new SerializableData()
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroup.FOOD)
                        .add("durability", SerializableDataTypes.INT, 100)
                        .add("fuel_tick", SerializableDataTypes.INT, 0)
                        .add("mining_speed_multiplier",SerializableDataTypes.FLOAT, 0f)
                        .add("attack_speed",SerializableDataTypes.INT, 0)
                        .add("mining_level",SerializableDataTypes.INT, 0)
                        .add("enchantability",SerializableDataTypes.INT, 0)
                        .add("attack_damage",SerializableDataTypes.INT, 0)
                        .add("repair_item",SerializableDataTypes.ITEM_STACK,null)
                        .add("lore", CCPackDataTypes.STRINGS, null),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            data.ifPresent("item_group", settings::group);
                            Supplier<Item> EXAMPLE_ITEM = () -> new DDPickaxeItem(new DDToolMaterial(data.getInt("durability"), data.getFloat("mining_speed_multiplier"), data.getInt("attack_damage"), data.getInt("mining_level"), data.getInt("enchantability"), (ItemStack)data.get("repair_item")), 0, 0, settings, (List<String>) data.get("lore"));
                            data.<Integer>ifPresent("fuel_tick", (tick) -> FuelRegistry.INSTANCE.add(EXAMPLE_ITEM.get(), tick));
                            return EXAMPLE_ITEM;
                        }));

        register(new ContentFactory<>(CCPacksMain.identifier("axe"), Types.ITEM,
                new SerializableData()
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroup.FOOD)
                        .add("durability", SerializableDataTypes.INT, 100)
                        .add("fuel_tick", SerializableDataTypes.INT, 0)
                        .add("mining_speed_multiplier",SerializableDataTypes.FLOAT, 0f)
                        .add("attack_speed",SerializableDataTypes.INT, 0)
                        .add("mining_level",SerializableDataTypes.INT, 0)
                        .add("enchantability",SerializableDataTypes.INT, 0)
                        .add("attack_damage",SerializableDataTypes.INT, 0)
                        .add("repair_item",SerializableDataTypes.ITEM_STACK,null)
                        .add("lore", CCPackDataTypes.STRINGS, null),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            data.ifPresent("item_group", settings::group);
                            Supplier<Item> EXAMPLE_ITEM = () -> new DDAxeItem(new DDToolMaterial(data.getInt("durability"), data.getFloat("mining_speed_multiplier"), data.getInt("attack_damage"), data.getInt("mining_level"), data.getInt("enchantability"), (ItemStack)data.get("repair_item")), 0, 0, settings, (List<String>) data.get("lore"));
                            data.<Integer>ifPresent("fuel_tick", (tick) -> FuelRegistry.INSTANCE.add(EXAMPLE_ITEM.get(), tick));
                            return EXAMPLE_ITEM;
                        }));

        register(new ContentFactory<>(CCPacksMain.identifier("shovel"), Types.ITEM,
                new SerializableData()
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroup.FOOD)
                        .add("durability", SerializableDataTypes.INT, 100)
                        .add("fuel_tick", SerializableDataTypes.INT, 0)
                        .add("mining_speed_multiplier",SerializableDataTypes.FLOAT, 0f)
                        .add("attack_speed",SerializableDataTypes.INT, 0)
                        .add("mining_level",SerializableDataTypes.INT, 0)
                        .add("enchantability",SerializableDataTypes.INT, 0)
                        .add("attack_damage",SerializableDataTypes.INT, 0)
                        .add("repair_item",SerializableDataTypes.ITEM_STACK,null)
                        .add("lore", CCPackDataTypes.STRINGS, null),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            data.ifPresent("item_group", settings::group);
                            Supplier<Item> EXAMPLE_ITEM = () -> new DDShovelItem(new DDToolMaterial(data.getInt("durability"), data.getFloat("mining_speed_multiplier"), data.getInt("attack_damage"), data.getInt("mining_level"), data.getInt("enchantability"), (ItemStack)data.get("repair_item")), data.getInt("attack_damage") - 4, data.getInt("attack_speed") - 3.3f, settings.maxCount(1), (List<String>) data.get("lore"));
                            data.<Integer>ifPresent("fuel_tick", (tick) -> FuelRegistry.INSTANCE.add(EXAMPLE_ITEM.get(), tick));
                            return EXAMPLE_ITEM;
                        }));

        register(new ContentFactory<>(CCPacksMain.identifier("hoe"), Types.ITEM,
                new SerializableData()
                        .add("durability", SerializableDataTypes.INT, 100)
                        .add("fuel_tick", SerializableDataTypes.INT, 0)
                        .add("mining_speed_multiplier",SerializableDataTypes.FLOAT, 0f)
                        .add("attack_speed",SerializableDataTypes.INT, 0)
                        .add("mining_level",SerializableDataTypes.INT, 0)
                        .add("enchantability",SerializableDataTypes.INT, 0)
                        .add("attack_damage",SerializableDataTypes.INT, 0)
                        .add("repair_item",SerializableDataTypes.ITEM_STACK,null)
                        .add("lore", CCPackDataTypes.STRINGS, null),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            data.ifPresent("item_group", settings::group);
                            Supplier<Item> EXAMPLE_ITEM = () -> new DDHoeItem(new DDToolMaterial(data.getInt("durability"), data.getFloat("mining_speed_multiplier"), data.getInt("attack_damage"), data.getInt("mining_level"), data.getInt("enchantability"), (ItemStack)data.get("repair_item")), 0, 0, settings, (List<String>) data.get("lore"));
                            data.<Integer>ifPresent("fuel_tick", (tick) -> FuelRegistry.INSTANCE.add(EXAMPLE_ITEM.get(), tick));
                            return EXAMPLE_ITEM;
                        }));

        register(new ContentFactory<>(CCPacksMain.identifier("food"), Types.ITEM,
                new SerializableData()
                        .add("max_count", SerializableDataTypes.INT, 64)
                        .add("hunger",SerializableDataTypes.INT, 4)
                        .add("fuel_tick", SerializableDataTypes.INT, 0)
                        .add("saturation",SerializableDataTypes.FLOAT, 8f)
                        .add("meat",SerializableDataTypes.BOOLEAN, false)
                        .add("always_edible",SerializableDataTypes.BOOLEAN, false)
                        .add("lore", CCPackDataTypes.STRINGS, null)
                        .add("drinkable", SerializableDataTypes.BOOLEAN, false)
                        .add("sound", SerializableDataTypes.SOUND_EVENT, SoundEvents.ENTITY_GENERIC_EAT)
                        .add("returns", SerializableDataTypes.ITEM, null)
                        .add("eating_time", SerializableDataTypes.INT, 30)
                        .add("eat_action", ApoliDataTypes.ENTITY_ACTION, null),
                data ->
                        (contentType,content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            FoodComponent.Builder food = new FoodComponent.Builder().hunger(data.getInt("hunger")).saturationModifier(data.getFloat("saturation"));
                            data.ifPresent("item_group", settings::group);
                            data.<Boolean>ifPresent("meat", aBoolean -> food.meat());
                            data.ifPresent("always_edible", aBoolean -> food.alwaysEdible());
                            FoodComponent foodComp = food.build();
                            Supplier<Item> EXAMPLE_ITEM = () -> new DDFoodItem(settings.food(foodComp).maxCount(data.getInt("max_count")), data.getBoolean("drinkable"), (SoundEvent) data.get("sound"), (ItemConvertible) data.get("returns"), data.getInt("eating_time"), (List<String>) data.get("lore"));
                            data.<Integer>ifPresent("fuel_tick", (tick) -> FuelRegistry.INSTANCE.add(EXAMPLE_ITEM.get(), tick));
                            return EXAMPLE_ITEM;
                        }));

        register(new ContentFactory<>(CCPacksMain.identifier("armor"), Types.ITEM,
                new SerializableData()
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroup.FOOD)
                        .add("name", SerializableDataTypes.STRING)
                        .add("fuel_tick", SerializableDataTypes.INT, 0)
                        .add("slot", SerializableDataType.enumValue(EquipmentSlot.class), EquipmentSlot.HEAD)
                        .add("durability", SerializableDataTypes.INT, 10)
                        .add("protection",SerializableDataTypes.INT, 0)
                        .add("toughness",SerializableDataTypes.INT, 0)
                        .add("knockback_resistance",SerializableDataTypes.INT, 0)
                        .add("enchantability",SerializableDataTypes.INT, 0)
                        .add("lore", CCPackDataTypes.STRINGS, null)
                        .add("repair_item", SerializableDataTypes.ITEM, null),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            data.ifPresent("item_group", settings::group);
                            DDArmorMaterial CUSTOM_MATERIAL = new DDArmorMaterial(data.getInt("durability"), data.getInt("protection"), data.getInt("enchantability"), data.getInt("toughness"), data.getInt("knockback_resistance"), data.getString("name"), (Item) data.get("repair_item"));
                            Supplier<Item> EXAMPLE_ITEM = () -> new DDArmorItem(CUSTOM_MATERIAL, (EquipmentSlot) data.get("slot"), settings, (List<String>) data.get("lore"));
                            data.<Integer>ifPresent("fuel_tick", (tick) -> FuelRegistry.INSTANCE.add(EXAMPLE_ITEM.get(), tick));
                            return EXAMPLE_ITEM;
                        }));

        register(new ContentFactory<>(CCPacksMain.identifier("music_disc"), Types.ITEM,
                new SerializableData()
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroup.FOOD)
                        .add("fuel_tick", SerializableDataTypes.INT, 0)
                        .add("comparator_output", SerializableDataTypes.INT, 1)
                        .add("sound", SerializableDataTypes.SOUND_EVENT),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            data.ifPresent("item_group", settings::group);
                            Supplier<Item> EXAMPLE_ITEM = () -> new DDMusicDiscItem(data.getInt("comparator_output"), (SoundEvent) data.get("sound"), (settings.maxCount(1)));
                            data.<Integer>ifPresent("fuel_tick", (tick) -> FuelRegistry.INSTANCE.add(EXAMPLE_ITEM.get(), tick));
                            return EXAMPLE_ITEM;
                        }));

        register(new ContentFactory<>(CCPacksMain.identifier("shield"), Types.ITEM,
                new SerializableData()
                        .add("durability", SerializableDataTypes.INT, 100)
                        .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroup.FOOD)
                        .add("fuel_tick", SerializableDataTypes.INT, 0)
                        .add("cooldown", SerializableDataTypes.INT, 60)
                        .add("enchantability",SerializableDataTypes.INT, 0)
                        .add("repair_item", SerializableDataTypes.ITEM, null),
                data ->
                        (contentType, content) -> {
                            FabricItemSettings settings = new FabricItemSettings();
                            data.ifPresent("item_group", settings::group);
                            Supplier<Item> EXAMPLE_ITEM = () -> new FabricShield(settings, data.getInt("cooldown"), data.getInt("durability"), data.getInt("enchantability"), (Item) data.get("repair_item"));
                            data.ifPresent("fuel_tick", (tick) -> FuelRegistry.INSTANCE.add(EXAMPLE_ITEM.get(), (Integer) tick));
                            return EXAMPLE_ITEM;
                        }));
    }

    private static void register(ContentFactory<Supplier<?>> serializer) {
        Registry.register(CCPacksRegistries.CONTENT_FACTORY, serializer.getSerializerId(), serializer);
    }
}
