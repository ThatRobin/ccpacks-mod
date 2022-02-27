package io.github.thatrobin.ccpacks.factories;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Active;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.VariableIntPower;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.apoli.util.ResourceOperation;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.thatrobin.ccpacks.CCPacksMain;
import io.github.thatrobin.ccpacks.mixins.KeyBindingAccessor;
import io.github.thatrobin.ccpacks.networking.CCPacksModPackets;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.registry.Registry;

public class EntityActions {

    public static void register() {
        register(new ActionFactory<>(CCPacksMain.identifier("open_choice_screen"), new SerializableData()
                .add("choice_layer", SerializableDataTypes.IDENTIFIER),
                (data, entity) -> {
                    if(!entity.getEntityWorld().isClient()) {
                        if (entity instanceof PlayerEntity player) {
                            PacketByteBuf data2 = new PacketByteBuf(Unpooled.buffer());
                            data2.writeBoolean(false);
                            data2.writeIdentifier(data.getId("choice_layer"));
                            ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, CCPacksModPackets.OPEN_CHOICE_SCREEN, data2);
                        }
                    }
                }
        ));

        register(new ActionFactory<>(CCPacksMain.identifier("change_stat"), new SerializableData()
                .add("stat_bar", ApoliDataTypes.POWER_TYPE)
                .add("change", SerializableDataTypes.INT)
                .add("operation", ApoliDataTypes.RESOURCE_OPERATION, ResourceOperation.ADD),
                (data, entity) -> {
                    if(entity instanceof LivingEntity) {
                        PowerHolderComponent component = PowerHolderComponent.KEY.get(entity);
                        Power p = component.getPower((PowerType<?>)data.get("stat_bar"));
                        ResourceOperation operation = data.get("operation");
                        int change = data.getInt("change");
                        if(p instanceof VariableIntPower vip) {
                            if (operation == ResourceOperation.ADD) {
                                int newValue = vip.getValue() + change;
                                vip.setValue(newValue);
                            } else if (operation == ResourceOperation.SET) {
                                vip.setValue(change);
                            }
                            PowerHolderComponent.sync(entity);
                        }
                    }
                }));

    }

    private static void register(ActionFactory<Entity> actionFactory) {
        Registry.register(ApoliRegistries.ENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }
}
