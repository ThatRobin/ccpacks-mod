package io.github.thatrobin.ccpacks.component;

import io.github.thatrobin.ccpacks.CCPacksMain;
import io.github.thatrobin.ccpacks.choice.Choice;
import io.github.thatrobin.ccpacks.choice.ChoiceLayer;
import io.github.thatrobin.ccpacks.choice.ChoiceLayers;
import io.github.thatrobin.ccpacks.choice.ChoiceRegistry;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerChoiceComponent implements ChoiceComponent {

    private final PlayerEntity player;
    private final HashMap<ChoiceLayer, Choice> choices = new HashMap<>();
    private final ConcurrentHashMap<PowerType<?>, Power> powers = new ConcurrentHashMap<>();

    private boolean hadChoiceBefore = false;

    public PlayerChoiceComponent(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public boolean hasAllChoices() {
        return ChoiceLayers.getLayers().stream().allMatch(layer -> !layer.isEnabled() || layer.getChoices(player).size() == 0 || (choices.containsKey(layer) && choices.get(layer) != null && choices.get(layer) != Choice.EMPTY));
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
        choices.values().forEach(choice -> {
            if(choice != null) {
                choice.getPowerTypes().forEach(powerTypes::add);
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
    public void setChoice(ChoiceLayer layer, Choice choice) {
        Choice oldChoice = getChoice(layer);
        if(oldChoice == choice) {
            return;
        }
        this.choices.put(layer, choice);
        PowerHolderComponent powerComponent = PowerHolderComponent.KEY.get(player);
        grantPowersFromChoice(choice, powerComponent);
        if(oldChoice != null) {
            powerComponent.removeAllPowersFromSource(oldChoice.getIdentifier());
        }
        powerComponent.sync();
        if(this.hasAllChoices()) {
            this.hadChoiceBefore = true;
        }
    }

    private void grantPowersFromChoice(Choice choice, PowerHolderComponent powerComponent) {
        Identifier source = choice.getIdentifier();
        for(PowerType<?> powerType : choice.getPowerTypes()) {
            if(!powerComponent.hasPower(powerType, source)) {
                powerComponent.addPower(powerType, source);
            }
        }
    }

    private void revokeRemovedPowers(Choice choice, PowerHolderComponent powerComponent) {
        Identifier source = choice.getIdentifier();
        List<PowerType<?>> powersByOrigin = powerComponent.getPowersFromSource(source);
        powersByOrigin.stream().filter(p -> !choice.hasPowerType(p)).forEach(p -> powerComponent.removePower(p, source));
    }

    @Override
    public void readFromNbt(NbtCompound compoundTag) {
        if (player == null) {
            CCPacksMain.LOGGER.error("Player was null in `fromTag`! This is a bug!");
        }

        this.choices.clear();

        if (compoundTag.contains("Choice")) {
            try {
                ChoiceLayer defaultChoiceLayer = ChoiceLayers.getLayer(new Identifier(CCPacksMain.MODID, "choice"));
                this.choices.put(defaultChoiceLayer, ChoiceRegistry.get(Identifier.tryParse(compoundTag.getString("Choice"))));
            } catch (IllegalArgumentException e) {
                assert player != null;
                CCPacksMain.LOGGER.warn("Player " + player.getDisplayName().asString() + " had old choice which could not be migrated: " + compoundTag.getString("Choice"));
            }
        } else {
            NbtList choiceLayerList = (NbtList) compoundTag.get("ChoiceLayers");
            if (choiceLayerList != null) {
                for (int i = 0; i < choiceLayerList.size(); i++) {
                    NbtCompound layerTag = choiceLayerList.getCompound(i);
                    Identifier layerId = Identifier.tryParse(layerTag.getString("Layer"));
                    ChoiceLayer layer = null;
                    try {
                        layer = ChoiceLayers.getLayer(layerId);
                    } catch (IllegalArgumentException e) {
                        CCPacksMain.LOGGER.warn("Could not find choice layer with id " + layerId.toString() + ", which existed on the data of player " + player.getDisplayName().asString() + ".");
                    }
                    if (layer != null) {
                        Identifier choiceId = Identifier.tryParse(layerTag.getString("Choice"));
                        Choice choice = null;
                        try {
                            choice = ChoiceRegistry.get(choiceId);
                        } catch (IllegalArgumentException e) {
                            CCPacksMain.LOGGER.warn("Could not find choice with id " + choiceId + ", which existed on the data of player " + player.getDisplayName().asString() + ".");
                            PowerHolderComponent powerComponent = PowerHolderComponent.KEY.get(player);
                            powerComponent.removeAllPowersFromSource(choiceId);
                        }
                        if (choice != null) {
                            if (!layer.contains(choice)) {
                                assert player != null;
                                CCPacksMain.LOGGER.warn("Choice with id " + choice.getIdentifier().toString() + " is not in layer " + layer.getIdentifier().toString() + ", but was found on " + player.getDisplayName().asString() + ", setting to EMPTY.");
                                choice = Choice.EMPTY;
                                PowerHolderComponent powerComponent = PowerHolderComponent.KEY.get(player);
                                powerComponent.removeAllPowersFromSource(choiceId);
                            }
                            this.choices.put(layer, choice);
                        }
                    }
                }
            }
        }
        this.hadChoiceBefore = compoundTag.getBoolean("HadChoiceBefore");

        if (!player.world.isClient) {
            PowerHolderComponent powerHolderComponent = PowerHolderComponent.KEY.get(player);
            for (Choice choice : choices.values()) {
                // Grants powers only if the player doesn't have them yet from the specific Origin source.
                // Needed in case the origin was set before the update to Apoli happened.
                grantPowersFromChoice(choice, powerHolderComponent);
            }
            for (Choice origin : choices.values()) {
                revokeRemovedPowers(origin, powerHolderComponent);
            }

            // Compatibility with old worlds:
            // Loads power data from Origins tag, whereas new versions
            // store the data in the Apoli tag.
            if (compoundTag.contains("Powers")) {
                NbtList powerList = (NbtList) compoundTag.get("Powers");
                for (int i = 0; i < powerList.size(); i++) {
                    NbtCompound powerTag = powerList.getCompound(i);
                    Identifier powerTypeId = Identifier.tryParse(powerTag.getString("Type"));
                    try {
                        PowerType<?> type = PowerTypeRegistry.get(powerTypeId);
                        if (powerHolderComponent.hasPower(type)) {
                            NbtElement data = powerTag.get("Data");
                            try {
                                powerHolderComponent.getPower(type).fromTag(data);
                            } catch (ClassCastException e) {
                                // Occurs when power was overriden by data pack since last world load
                                // to be a power type which uses different data class.
                                CCPacksMain.LOGGER.warn("Data type of \"" + powerTypeId + "\" changed, skipping data for that power on player " + player.getName().asString());
                            }
                        }
                    } catch (IllegalArgumentException e) {
                        CCPacksMain.LOGGER.warn("Power data of unregistered power \"" + powerTypeId + "\" found on player, skipping...");
                    }
                }
            }
        }
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound compoundTag) {
        NbtList choiceLayerList = new NbtList();
        for(Map.Entry<ChoiceLayer, Choice> entry : choices.entrySet()) {
            NbtCompound layerTag = new NbtCompound();
            layerTag.putString("Layer", entry.getKey().getIdentifier().toString());
            layerTag.putString("Choice", entry.getValue().getIdentifier().toString());
            choiceLayerList.add(layerTag);
        }
        compoundTag.put("ChoiceLayers", choiceLayerList);
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
