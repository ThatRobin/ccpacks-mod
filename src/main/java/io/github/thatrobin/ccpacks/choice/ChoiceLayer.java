package io.github.thatrobin.ccpacks.choice;

import com.google.common.collect.Lists;
import com.google.gson.*;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.power.factory.condition.ConditionTypes;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChoiceLayer implements Comparable<ChoiceLayer> {

    public static final SerializableData DATA = new SerializableData()
            .add("enabled", SerializableDataTypes.BOOLEAN, true)
            .add("choices", SerializableDataTypes.IDENTIFIERS, Lists.newArrayList());

    private Identifier identifier;
    private List<ConditionedChoice> choices;
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



    public boolean isEnabled() {
        return enabled;
    }

    public List<Identifier> getChoices() {
        return choices.stream().flatMap(co -> co.getChoices().stream()).filter(ChoiceRegistry::contains).collect(Collectors.toList());
    }

    public List<Identifier> getChoices(PlayerEntity playerEntity) {
        return choices.stream().filter(co -> co.isConditionFulfilled(playerEntity)).flatMap(co -> co.getChoices().stream()).filter(ChoiceRegistry::contains).collect(Collectors.toList());
    }

    public int getChoiceOptionCount(PlayerEntity player) {
        long choosableChoices = getChoices(player).stream().map(ChoiceRegistry::get).count();
        return (int)choosableChoices;
    }

    public boolean contains(Choice choice) {
        return choices.contains(choice.getIdentifier());
    }

    public void merge(SerializableData.Instance data) {
        data.<Boolean>ifPresent("enabled", aBoolean -> this.enabled = aBoolean);
        data.<List<ConditionedChoice>>ifPresent("choices", choices -> this.choices = choices);
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

    @Environment(EnvType.CLIENT)
    public static ChoiceLayer read(PacketByteBuf buffer) {
        ChoiceLayer layer = new ChoiceLayer();
        layer.identifier = Identifier.tryParse(buffer.readString());
        layer.enabled = buffer.readBoolean();
        layer.nameTranslationKey = buffer.readString();
        return layer;
    }

    public static ChoiceLayer createFromData(Identifier id, JsonObject json) {
        ChoiceLayer choiceLayer = new ChoiceLayer();
        JsonArray choiceArray = json.getAsJsonArray("choices");
        List<ConditionedChoice> list = new ArrayList<>(choiceArray.size());
        choiceArray.forEach(je -> list.add(ConditionedChoice.read(je)));
        boolean enabled = JsonHelper.getBoolean(json, "enabled", true);

        choiceLayer.identifier = id;
        choiceLayer.choices = list;
        choiceLayer.enabled = enabled;
        return choiceLayer;
    }

    @Override
    public int compareTo(@NotNull ChoiceLayer o) {
        return 0;
    }

    public static class ConditionedChoice {
        private final ConditionFactory<Entity>.Instance condition;
        private final List<Identifier> choices;

        public ConditionedChoice(ConditionFactory<Entity>.Instance condition, List<Identifier> choices) {
            this.condition = condition;
            this.choices = choices;
        }

        public boolean isConditionFulfilled(PlayerEntity playerEntity) {
            return condition == null || condition.test(playerEntity);
        }

        public List<Identifier> getChoices() {
            return choices;
        }
        private static final SerializableData conditionedOriginObjectData = new SerializableData()
                .add("condition", ApoliDataTypes.ENTITY_CONDITION)
                .add("choices", SerializableDataTypes.IDENTIFIERS);

        public void write(PacketByteBuf buffer) {
            buffer.writeBoolean(condition != null);
            if(condition != null)
                condition.write(buffer);
            buffer.writeInt(choices.size());
            choices.forEach(buffer::writeIdentifier);
        }

        @Environment(EnvType.CLIENT)
        public static ConditionedChoice read(PacketByteBuf buffer) {
            ConditionFactory<Entity>.Instance condition = null;
            if(buffer.readBoolean()) {
                condition = ConditionTypes.ENTITY.read(buffer);
            }
            int originCount = buffer.readInt();
            List<Identifier> originList = new ArrayList<>(originCount);
            for(int i = 0; i < originCount; i++) {
                originList.add(buffer.readIdentifier());
            }
            return new ConditionedChoice(condition, originList);
        }

        @SuppressWarnings("unchecked")
        public static ConditionedChoice read(JsonElement element) {
            if(element.isJsonPrimitive()) {
                JsonPrimitive elemPrimitive = element.getAsJsonPrimitive();
                if(elemPrimitive.isString()) {
                    return new ConditionedChoice(null, Lists.newArrayList(Identifier.tryParse(elemPrimitive.getAsString())));
                }
                throw new JsonParseException("Expected origin in layer to be either a string or an object.");
            } else if(element.isJsonObject()) {
                SerializableData.Instance data = conditionedOriginObjectData.read(element.getAsJsonObject());
                return new ConditionedChoice((ConditionFactory<Entity>.Instance)data.get("condition"), (List<Identifier>)data.get("choices"));
            }
            throw new JsonParseException("Expected origin in layer to be either a string or an object.");
        }
    }
}