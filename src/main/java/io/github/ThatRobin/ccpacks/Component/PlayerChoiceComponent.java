package io.github.ThatRobin.ccpacks.Component;

import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.ThatRobin.ccpacks.Choice.Choice;
import io.github.ThatRobin.ccpacks.Choice.ChoiceLayer;
import io.github.ThatRobin.ccpacks.Choice.ChoiceLayers;
import io.github.ThatRobin.ccpacks.Choice.ChoiceRegistry;
import io.github.apace100.apoli.power.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerChoiceComponent implements ChoiceComponent {

    private PlayerEntity player;
    private HashMap<ChoiceLayer, Choice> choices = new HashMap<>();
    private ConcurrentHashMap<PowerType<?>, Power> powers = new ConcurrentHashMap<>();

    private boolean hadChoiceBefore = false;

    public PlayerChoiceComponent(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public boolean hasAllChoices() {
        return ChoiceLayers.getLayers().stream().allMatch(layer -> {
            return !layer.isEnabled() || (choices.containsKey(layer) && choices.get(layer) != null && choices.get(layer) != Choice.EMPTY);
        });
    }

    @Override
    public HashMap<ChoiceLayer, Choice> getChoices() {
        return choices;
    }

    @Override
    public boolean hasChoice(ChoiceLayer layer) {
        return choices != null && choices.containsKey(layer) && choices.get(layer) != null && choices.get(layer) != Choice.EMPTY;
    }

    @Override
    public Choice getChoice(ChoiceLayer layer) {
        if(!choices.containsKey(layer)) {
            return null;
        }
        return choices.get(layer);
    }

    private boolean hasPowerType(PowerType<?> powerType) {
        return choices.values().stream().anyMatch(o -> o.hasPowerType(powerType));
    }

    private Set<PowerType<?>> getPowerTypes() {
        Set<PowerType<?>> powerTypes = new HashSet<>();
        choices.values().forEach(origin -> {
            if(origin != null) {
                origin.getPowerTypes().forEach(powerTypes::add);
            }
        });
        return powerTypes;
    }

    @Override
    public <T extends Power> List<T> getPowers(Class<T> powerClass) {
        return getPowers(powerClass, false);
    }

    @Override
    public <T extends Power> List<T> getPowers(Class<T> powerClass, boolean includeInactive) {
        List<T> list = new LinkedList<>();
        for(Power power : powers.values()) {
            if(powerClass.isAssignableFrom(power.getClass()) && (includeInactive || power.isActive())) {
                list.add((T)power);
            }
        }
        return list;
    }

    @Override
    public void setChoice(ChoiceLayer layer, Choice origin) {
        Choice oldChoice = getChoice(layer);
        if(oldChoice == origin) {
            return;
        }
        this.choices.put(layer, origin);
        if(oldChoice != null) {
            List<PowerType<?>> powersToRemove = new LinkedList<>();
            for (Map.Entry<PowerType<?>, Power> powerEntry: powers.entrySet()) {
                if(!hasPowerType(powerEntry.getKey())) {
                    powerEntry.getValue().onRemoved();
                    powerEntry.getValue().onLost();
                    powersToRemove.add(powerEntry.getKey());
                }
            }
            for(PowerType<?> toRemove : powersToRemove) {
                powers.remove(toRemove);
            }
        }
        origin.getPowerTypes().forEach(powerType -> {
            if(!powers.containsKey(powerType)) {
                Power power = powerType.create(player);
                this.powers.put(powerType, power);
                power.onAdded();
            }
        });
        if(this.hasAllChoices()) {
            this.hadChoiceBefore = true;
        }
    }

    @Override
    public void serverTick() {
        this.getPowers(Power.class, true).stream().filter(p -> p.shouldTick() && (p.shouldTickWhenInactive() || p.isActive())).forEach(Power::tick);
        if(this.player.age % 10 == 0) {
            this.getPowers(SimpleStatusEffectPower.class).forEach(SimpleStatusEffectPower::applyEffects);
            this.getPowers(StackingStatusEffectPower.class, true).forEach(StackingStatusEffectPower::tick);
        }
    }

    @Override
    public void readFromNbt(NbtCompound compoundTag) {
        this.fromTag(compoundTag, true);
    }

    private void fromTag(NbtCompound compoundTag, boolean callPowerOnAdd) {
        if(player == null) {
            CCPacksMain.LOGGER.error("Player was null in `fromTag`! This is a bug!");
        }
        if(this.choices != null) {
            if(callPowerOnAdd) {
                for (Power power: powers.values()) {
                    power.onRemoved();
                    power.onLost();
                }
            }
            powers.clear();
        }

        this.choices.clear();

        if(compoundTag.contains("Choice")) {
            try {
                ChoiceLayer defaultChoiceLayer = ChoiceLayers.getLayer(new Identifier(CCPacksMain.MODID, "origin"));
                this.choices.put(defaultChoiceLayer, ChoiceRegistry.get(Identifier.tryParse(compoundTag.getString("Choice"))));
            } catch(IllegalArgumentException e) {
                CCPacksMain.LOGGER.warn("Player " + player.getDisplayName().asString() + " had old origin which could not be migrated: " + compoundTag.getString("Choice"));
            }
        } else {
            NbtList originLayerList = (NbtList)compoundTag.get("ChoiceLayers");
            if(originLayerList != null) {
                for(int i = 0; i < originLayerList.size(); i++) {
                    NbtCompound layerTag = originLayerList.getCompound(i);
                    Identifier layerId = Identifier.tryParse(layerTag.getString("Layer"));
                    ChoiceLayer layer = null;
                    try {
                        layer = ChoiceLayers.getLayer(layerId);
                    } catch(IllegalArgumentException e) {
                        CCPacksMain.LOGGER.warn("Could not find origin layer with id " + layerId.toString() + ", which existed on the data of player " + player.getDisplayName().asString() + ".");
                    }
                    if(layer != null) {
                        Identifier originId = Identifier.tryParse(layerTag.getString("Choice"));
                        Choice origin = null;
                        try {
                            origin = ChoiceRegistry.get(originId);
                        } catch(IllegalArgumentException e) {
                            CCPacksMain.LOGGER.warn("Could not find origin with id " + originId.toString() + ", which existed on the data of player " + player.getDisplayName().asString() + ".");
                        }
                        if(origin != null) {
                            if(!layer.contains(origin)) {
                                CCPacksMain.LOGGER.warn("Choice with id " + origin.getIdentifier().toString() + " is not in layer " + layer.getIdentifier().toString() + " and is not special, but was found on " + player.getDisplayName().asString() + ", setting to EMPTY.");
                                origin = Choice.EMPTY;
                            }
                            this.choices.put(layer, origin);
                        }
                    }
                }
            }
        }
        this.hadChoiceBefore = compoundTag.getBoolean("HadChoiceBefore");
        NbtList powerList = (NbtList)compoundTag.get("Powers");
        for(int i = 0; i < powerList.size(); i++) {
            NbtCompound powerTag = powerList.getCompound(i);
            Identifier powerTypeId = Identifier.tryParse(powerTag.getString("Type"));
            try {
                PowerType<?> type = PowerTypeRegistry.get(powerTypeId);
                if(hasPowerType(type)) {
                    NbtElement data = powerTag.get("Data");
                    Power power = type.create(player);
                    try {
                        power.fromTag(data);
                    } catch(ClassCastException e) {
                        // Occurs when power was overriden by data pack since last world load
                        // to be a power type which uses different data class.
                        CCPacksMain.LOGGER.warn("Data type of \"" + powerTypeId + "\" changed, skipping data for that power on player " + player.getName().asString());
                    }
                    this.powers.put(type, power);
                    if(callPowerOnAdd) {
                        power.onAdded();
                    }
                }
            } catch(IllegalArgumentException e) {
                CCPacksMain.LOGGER.warn("Power data of unregistered power \"" + powerTypeId + "\" found on player, skipping...");
            }
        }
        this.getPowerTypes().forEach(pt -> {
            if(!this.powers.containsKey(pt)) {
                Power power = pt.create(player);
                this.powers.put(pt, power);
            }
        });
    }

    @Override
    public void writeToNbt(NbtCompound compoundTag) {
        NbtList originLayerList = new NbtList();
        for(Map.Entry<ChoiceLayer, Choice> entry : choices.entrySet()) {
            NbtCompound layerTag = new NbtCompound();
            layerTag.putString("Layer", entry.getKey().getIdentifier().toString());
            layerTag.putString("Choice", entry.getValue().getIdentifier().toString());
            originLayerList.add(layerTag);
        }
        compoundTag.put("ChoiceLayers", originLayerList);
        compoundTag.putBoolean("HadChoiceBefore", this.hadChoiceBefore);
        NbtList powerList = new NbtList();
        for(Map.Entry<PowerType<?>, Power> powerEntry : powers.entrySet()) {
            NbtCompound powerTag = new NbtCompound();
            powerTag.putString("Type", PowerTypeRegistry.getId(powerEntry.getKey()).toString());
            powerTag.put("Data", powerEntry.getValue().toTag());
            powerList.add(powerTag);
        }
        compoundTag.put("Powers", powerList);
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        NbtCompound compoundTag = buf.readNbt();
        if(compoundTag != null) {
            this.fromTag(compoundTag, false);
        }
    }

    @Override
    public void sync() {
        ChoiceComponent.sync(this.player);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("ChoiceComponent[\n");
        for (Map.Entry<PowerType<?>, Power> powerEntry : powers.entrySet()) {
            str.append("\t").append(PowerTypeRegistry.getId(powerEntry.getKey())).append(": ").append(powerEntry.getValue().toTag().toString()).append("\n");
        }
        str.append("]");
        return str.toString();
    }
}
