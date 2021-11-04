package io.github.ThatRobin.ccpacks.Util;

import com.google.common.collect.Lists;
import io.github.ThatRobin.ccpacks.Factories.TaskFactories.TaskRegistry;
import io.github.ThatRobin.ccpacks.Factories.TaskFactories.TaskType;
import io.github.ThatRobin.ccpacks.Factories.TaskFactories.TaskTypeReference;
import net.minecraft.util.Identifier;

import java.util.List;

public class GoalMap {

    public static List<TaskType> goals = Lists.newArrayList(TaskRegistry.get(new Identifier("ccpacks", "goal")));

    public List<TaskType> getGoals() {
        return this.goals;
    }
}
