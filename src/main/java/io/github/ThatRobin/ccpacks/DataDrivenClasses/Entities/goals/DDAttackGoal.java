package io.github.ThatRobin.ccpacks.DataDrivenClasses.Entities.goals;

import io.github.ThatRobin.ccpacks.Factories.TaskFactories.TaskFactory;
import io.github.ThatRobin.ccpacks.Factories.TaskFactories.TaskType;
import net.minecraft.util.Identifier;

public class DDAttackGoal extends TaskType {

    public DDAttackGoal(Identifier id, TaskFactory<TaskType>.Instance factory) {
        super(id, factory);
    }

}
