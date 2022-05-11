package io.github.thatrobin.ccpacksapoli.factories;

import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import io.github.thatrobin.ccpacksapoli.CCPacksApoli;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;

public class BiEntityActions {

    public static void register() {
        register(new ActionFactory<>(CCPacksApoli.identifier("punch"), new SerializableData(),
                (data, entities) -> {
                    if(entities.getLeft() instanceof PlayerEntity attacker) {
                        if(entities.getRight() instanceof LivingEntity target) {
                            attacker.attack(target);
                            attacker.resetLastAttackedTicks();
                        }
                    }
                }));

    }

    private static void register(ActionFactory<Pair<Entity, Entity>> actionFactory) {
        Registry.register(ApoliRegistries.BIENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }
}
