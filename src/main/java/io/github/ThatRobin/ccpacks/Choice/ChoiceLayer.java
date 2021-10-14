package io.github.ThatRobin.ccpacks.Choice;

import com.google.common.collect.Lists;
import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChoiceLayer implements Comparable<ChoiceLayer> {

    public static final SerializableData DATA = new SerializableData()
            .add("enabled", SerializableDataTypes.BOOLEAN, true)
            .add("action_on_chosen", ApoliDataTypes.ENTITY_ACTION, null)
            .add("choices", SerializableDataTypes.IDENTIFIERS, Lists.newArrayList());

    private Identifier identifier;
    private ActionFactory<Entity>.Instance action;
    private List<Identifier> choices;
    private boolean enabled = false;

    private String nameTranslationKey;

    public String getOrCreateTranslationKey() {
        if(nameTranslationKey == null || nameTranslationKey.isEmpty()) {
            this.nameTranslationKey = "layer." + identifier.getNamespace() + "." + identifier.getPath() + ".name";
        }
        return nameTranslationKey;
    }

    public String getTranslationKey() {
        return getOrCreateTranslationKey();
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public ActionFactory<Entity>.Instance getChosenAction() {
        CCPacksMain.LOGGER.info(action);
        CCPacksMain.LOGGER.info(getIdentifier());
        return action;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public List<Identifier> getChoices() {
        return choices;
    }

    public int getChoiceOptionCount() {
        long choosableChoices = getChoices().stream().map(ChoiceRegistry::get).count();
        return (int)choosableChoices;
    }

    public boolean contains(Choice choice) {
        return choices.contains(choice.getIdentifier());
    }

    public void merge(SerializableData.Instance data) {
        data.<Boolean>ifPresent("enabled", aBoolean -> this.enabled = aBoolean);
        data.<ActionFactory<Entity>.Instance>ifPresent("action_on_chosen", action -> this.action = action);
        data.<List<Identifier>>ifPresent("choices", choices -> this.choices = choices);
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        } else if(!(obj instanceof ChoiceLayer)) {
            return false;
        } else {
            return identifier.equals(((ChoiceLayer)obj).identifier);
        }
    }

    public static ChoiceLayer createFromData(Identifier id, SerializableData.Instance data) {
        ChoiceLayer choiceLayer = new ChoiceLayer();
        choiceLayer.action = (ActionFactory<Entity>.Instance)data.get("action_on_chosen");
        choiceLayer.identifier = id;
        choiceLayer.choices = (List<Identifier>) data.get("choices");
        choiceLayer.enabled = data.getBoolean("enabled");
        return choiceLayer;
    }

    @Override
    public int compareTo(@NotNull ChoiceLayer o) {
        return 0;
    }
}