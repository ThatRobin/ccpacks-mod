package io.github.thatrobin.ccpacks.factories.task_factories;

import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class TaskType {

    private Identifier identifier;
    private TaskFactory<TaskType>.Instance factory;

    private String nameTranslationKey;
    private String descriptionTranslationKey;

    public TaskType(Identifier id, TaskFactory<TaskType>.Instance factory) {
        this.identifier = id;
        this.factory = factory;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public TaskFactory<TaskType>.Instance getFactory() {
        return factory;
    }

    public TaskType setHidden() {
        return this;
    }

    public void setTranslationKeys(String name, String description) {
        this.nameTranslationKey = name;
        this.descriptionTranslationKey = description;
    }

    public TaskType create(Identifier identifier) {
        return (TaskType) factory.apply(identifier, null);
    }

    public String getOrCreateNameTranslationKey() {
        if(nameTranslationKey == null || nameTranslationKey.isEmpty()) {
            nameTranslationKey =
                    "item." + identifier.getNamespace() + "." + identifier.getPath() + ".name";
        }
        return nameTranslationKey;
    }

    public TranslatableText getName() {
        return new TranslatableText(getOrCreateNameTranslationKey());
    }

    public String getOrCreateDescriptionTranslationKey() {
        if(descriptionTranslationKey == null || descriptionTranslationKey.isEmpty()) {
            descriptionTranslationKey =
                    "item." + identifier.getNamespace() + "." + identifier.getPath() + ".description";
        }
        return descriptionTranslationKey;
    }

    public TranslatableText getDescription() {
        return new TranslatableText(getOrCreateDescriptionTranslationKey());
    }

    @Override
    public int hashCode() {
        return this.identifier.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof TaskType)) {
            return false;
        }
        Identifier id = ((TaskType)obj).getIdentifier();
        return identifier.equals(id);
    }
}

