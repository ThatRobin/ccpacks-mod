package io.github.thatrobin.ccpacks.util;

import com.google.common.collect.Lists;
import io.github.thatrobin.ccpacks.factories.task_factories.TaskRegistry;
import io.github.thatrobin.ccpacks.factories.task_factories.TaskType;
import net.minecraft.util.Identifier;

import java.util.List;

public class GoalMap {

    public static List<TaskType> goals = Lists.newArrayList(TaskRegistry.get(new Identifier("ccpacks", "goal")));

    public List<TaskType> getGoals() {
        return this.goals;
    }
}
