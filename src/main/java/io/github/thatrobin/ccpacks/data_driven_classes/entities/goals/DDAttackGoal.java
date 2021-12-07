package io.github.thatrobin.ccpacks.data_driven_classes.entities.goals;

import io.github.thatrobin.ccpacks.factories.task_factories.TaskFactory;
import io.github.thatrobin.ccpacks.factories.task_factories.TaskType;
import net.minecraft.util.Identifier;

public class DDAttackGoal extends TaskType {

    public DDAttackGoal(Identifier id, TaskFactory<TaskType>.Instance factory) {
        super(id, factory);
    }

}
