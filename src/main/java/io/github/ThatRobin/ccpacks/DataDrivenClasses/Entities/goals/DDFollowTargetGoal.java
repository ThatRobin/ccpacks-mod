package io.github.ThatRobin.ccpacks.DataDrivenClasses.Entities.goals;

import io.github.ThatRobin.ccpacks.Factories.TaskFactories.TaskFactory;
import io.github.ThatRobin.ccpacks.Factories.TaskFactories.TaskType;
import net.minecraft.util.Identifier;

public class DDFollowTargetGoal extends TaskType {

    public DDFollowTargetGoal(Identifier id, TaskFactory<TaskType>.Instance factory) {
        super(id, factory);
    }

}
