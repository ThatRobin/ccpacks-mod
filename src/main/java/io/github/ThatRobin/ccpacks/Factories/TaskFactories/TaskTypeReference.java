package io.github.ThatRobin.ccpacks.Factories.TaskFactories;

import net.minecraft.util.Identifier;

public class TaskTypeReference extends TaskType {

    private TaskType referencedPowerType;
    private int cooldown = 0;

    public TaskTypeReference(Identifier id) {
        super(id, null);
    }

    @Override
    public TaskFactory<TaskType>.Instance getFactory() {
        getReferencedPowerType();
        if(referencedPowerType == null) {
            return null;
        }
        return referencedPowerType.getFactory();
    }

    public TaskType getReferencedPowerType() {
        if(isReferenceInvalid()) {
            if(cooldown > 0) {
                cooldown--;
                return null;
            }
            try {
                referencedPowerType = null;
                referencedPowerType = TaskRegistry.get(getIdentifier());
            } catch(IllegalArgumentException e) {
                cooldown = 600;
            }
        }
        return referencedPowerType;
    }

    private boolean isReferenceInvalid() {
        if(referencedPowerType != null) {
            if(TaskRegistry.contains(referencedPowerType.getIdentifier())) {
                TaskType type = TaskRegistry.get(referencedPowerType.getIdentifier());
                return type != referencedPowerType;
            }
        }
        return true;
    }
}
