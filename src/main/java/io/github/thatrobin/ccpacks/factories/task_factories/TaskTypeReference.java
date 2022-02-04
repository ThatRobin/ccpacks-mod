package io.github.thatrobin.ccpacks.factories.task_factories;

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

    public void getReferencedPowerType() {
        if(isReferenceInvalid()) {
            if(cooldown > 0) {
                cooldown--;
                return;
            }
            try {
                referencedPowerType = null;
                referencedPowerType = TaskRegistry.get(getIdentifier());
            } catch(IllegalArgumentException e) {
                cooldown = 600;
            }
        }
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
