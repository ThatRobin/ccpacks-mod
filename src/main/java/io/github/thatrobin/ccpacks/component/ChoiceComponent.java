package io.github.thatrobin.ccpacks.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import io.github.thatrobin.ccpacks.choice.Choice;
import io.github.thatrobin.ccpacks.choice.ChoiceLayer;
import io.github.thatrobin.ccpacks.choice.ChoiceLayers;
import io.github.thatrobin.ccpacks.choice.ChoiceRegistry;
import io.github.apace100.apoli.power.Power;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public interface ChoiceComponent extends AutoSyncedComponent {

    boolean hasChoice(ChoiceLayer layer);
    boolean hasAllChoices();

    HashMap<ChoiceLayer, Choice> getChoices();
    Choice getChoice(ChoiceLayer layer);

    <T extends Power> List<T> getPowers(Class<T> powerClass);
    <T extends Power> List<T> getPowers(Class<T> powerClass, boolean includeInactive);

    void setChoice(ChoiceLayer layer, Choice choice);

    void sync();

    static void sync(PlayerEntity player) {
        ModComponents.CHOICE.sync(player);
    }

    default void checkAutoChoosingLayers() {
        ArrayList<ChoiceLayer> layers = new ArrayList<>();
        for(ChoiceLayer layer : ChoiceLayers.getLayers()) {
            if(layer.isEnabled()) {
                layers.add(layer);
            }
        }
        Collections.sort(layers);
        for(ChoiceLayer layer : layers) {
            boolean shouldContinue = false;
            if (layer.isEnabled() && !hasChoice(layer)) {
                if (layer.getChoiceOptionCount() == 1) {
                    List<Choice> choices = layer.getChoices().stream().map(ChoiceRegistry::get).collect(Collectors.toList());
                    setChoice(layer, choices.get(0));
                    shouldContinue = true;
                } else if(layer.getChoiceOptionCount() == 0) {
                    shouldContinue = true;
                }
            } else {
                shouldContinue = true;
            }
            if(!shouldContinue) {
                break;
            }
        }
    }
}
