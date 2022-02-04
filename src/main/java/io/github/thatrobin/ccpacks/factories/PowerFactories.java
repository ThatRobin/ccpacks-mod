package io.github.thatrobin.ccpacks.factories;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.thatrobin.ccpacks.CCPackDataTypes;
import io.github.thatrobin.ccpacks.CCPacksMain;
import io.github.thatrobin.ccpacks.power.ActionOnProjectileLand;
import io.github.thatrobin.ccpacks.power.StatBar;
import io.github.thatrobin.ccpacks.util.StatBarHudRender;
import net.minecraft.util.registry.Registry;

public class PowerFactories {

    public static void register() {

        register(new PowerFactory<>(CCPacksMain.identifier("stat_bar"),
                new SerializableData()
                        .add("start_value", SerializableDataTypes.INT, 20)
                        .add("hud_render", CCPackDataTypes.STAT_BAR_HUD_RENDER),
                data ->
                        (type, player) -> new StatBar(type, player, data.get("hud_render")))
                .allowCondition());

        register(new PowerFactory<>(CCPacksMain.identifier("action_on_projectile_land"),
                new SerializableData()
                        .add("projectile", SerializableDataTypes.IDENTIFIER, null)
                        .add("entity_action", ApoliDataTypes.ENTITY_ACTION, null)
                        .add("block_action", ApoliDataTypes.BLOCK_ACTION, null)
                        .add("block_condition", ApoliDataTypes.BLOCK_CONDITION, null),
                data ->
                        (type, player) -> new ActionOnProjectileLand(type, player, data.get("block_condition"), data.get("entity_action"), data.get("block_action"), data.getId("projectile")))
                .allowCondition());

    }

    private static void register(PowerFactory<?> serializer) {
        Registry.register(ApoliRegistries.POWER_FACTORY, serializer.getSerializerId(), serializer);
    }
}
