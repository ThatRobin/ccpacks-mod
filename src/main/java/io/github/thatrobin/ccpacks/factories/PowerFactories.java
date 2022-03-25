package io.github.thatrobin.ccpacks.factories;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Active;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.power.factory.PowerFactorySupplier;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.thatrobin.ccpacks.CCPackDataTypes;
import io.github.thatrobin.ccpacks.CCPacksMain;
import io.github.thatrobin.ccpacks.power.*;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class PowerFactories {

    public static void register() {

        register(new PowerFactory<>(CCPacksMain.identifier("stat_bar"),
                new SerializableData()
                        .add("start_value", SerializableDataTypes.INT, 20)
                        .add("hud_render", CCPackDataTypes.STAT_BAR_HUD_RENDER),
                data ->
                        (type, player) -> new StatBar(type, player, data.get("hud_render")))
                .allowCondition());

        register(new PowerFactory<>(CCPacksMain.identifier("bind_item"),
                new SerializableData()
                        .add("item_condition", ApoliDataTypes.ITEM_CONDITION, null)
                        .add("prevent_use_condition", ApoliDataTypes.ITEM_CONDITION, null)
                        .add("slots", SerializableDataTypes.INTS, null),
                data ->
                        (type, player) -> new BindPower(type, player, data.get("item_condition"), data.get("slots"), data.get("prevent_use")))
                .allowCondition());

        register(new PowerFactory<>(CCPacksMain.identifier("action_on_projectile_land"),
                new SerializableData()
                        .add("projectile", SerializableDataTypes.IDENTIFIER, null)
                        .add("entity_action", ApoliDataTypes.ENTITY_ACTION, null)
                        .add("self_action", ApoliDataTypes.ENTITY_ACTION, null)
                        .add("block_action", ApoliDataTypes.BLOCK_ACTION, null)
                        .add("block_condition", ApoliDataTypes.BLOCK_CONDITION, null),
                data ->
                        (type, player) -> new ActionOnProjectileLand(type, player, data.get("block_condition"), data.get("entity_action"), data.get("self_action"), data.get("block_action"), data.getId("projectile")))
                .allowCondition());

        register(new PowerFactory<>(CCPacksMain.identifier("item_use"),
                new SerializableData()
                        .add("cooldown", SerializableDataTypes.INT, 0)
                        .add("entity_action", ApoliDataTypes.ENTITY_ACTION, null)
                        .add("item_condition", ApoliDataTypes.ITEM_CONDITION, null)
                        .add("item_action", ApoliDataTypes.ITEM_ACTION, null),
                data ->
                        (type, player) -> {
                            ItemUsePower power = new ItemUsePower(type, player, (Predicate<ItemStack>)data.get("item_condition"), (Consumer<Entity>)data.get("entity_action"), (Consumer<ItemStack>)data.get("item_action"), data.getInt("cooldown"));
                            return power;
                        })
                .allowCondition());

        register(new PowerFactory<>(CCPacksMain.identifier("use_as_bundle"),
                new SerializableData()
                        .add("max_amount", SerializableDataTypes.INT, 64)
                        .add("item_condition", ApoliDataTypes.ITEM_CONDITION),
                data ->
                        (type, player) -> new BundlePower(type, player, data.getInt("max_amount"), data.get("item_condition")))
                .allowCondition());

        register(new PowerFactory<>(CCPacksMain.identifier("toggle"),
                new SerializableData()
                        .add("active_by_default", SerializableDataTypes.BOOLEAN, true)
                        .add("retain_state", SerializableDataTypes.BOOLEAN, true)
                        .add("toggle_on_action", ApoliDataTypes.ENTITY_ACTION, null)
                        .add("toggle_off_action", ApoliDataTypes.ENTITY_ACTION, null)
                        .add("key", ApoliDataTypes.BACKWARDS_COMPATIBLE_KEY, new Active.Key()),
                data ->
                        (type, player) -> {
                            CustomToggle toggle = new CustomToggle(type, player, data.get("toggle_on_action"), data.get("toggle_off_action"), data.getBoolean("active_by_default"), data.getBoolean("retain_state"));
                            toggle.setKey(data.get("key"));
                            return toggle;
                        }).allowCondition());

    }

    private static void register(PowerFactory<?> serializer) {
        Registry.register(ApoliRegistries.POWER_FACTORY, serializer.getSerializerId(), serializer);
    }

    private static void register(PowerFactorySupplier<?> factorySupplier) {
        register(factorySupplier.createFactory());
    }
}
