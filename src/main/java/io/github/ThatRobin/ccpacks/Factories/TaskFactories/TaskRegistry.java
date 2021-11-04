package io.github.ThatRobin.ccpacks.Factories.TaskFactories;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class TaskRegistry {
    private static HashMap<Identifier, TaskType> idToContent = new HashMap<>();

    public static TaskType register(Identifier id, TaskType contentType) {
        if(idToContent.containsKey(id)) {
            throw new IllegalArgumentException("Duplicate content type id tried to register: '" + id.toString() + "'");
        }
        idToContent.put(id, contentType);
        return contentType;
    }

    protected static TaskType update(Identifier id, TaskType contentType) {
        if(idToContent.containsKey(id)) {
            TaskType old = idToContent.get(id);
            idToContent.remove(id);
        }
        return register(id, contentType);
    }

    public static int size() {
        return idToContent.size();
    }

    public static Stream<Identifier> identifiers() {
        return idToContent.keySet().stream();
    }

    public static Iterable<Map.Entry<Identifier, TaskType>> entries() {
        return idToContent.entrySet();
    }

    public static Iterable<TaskType> values() {
        return idToContent.values();
    }

    public static TaskType get(Identifier id) {
        if(!idToContent.containsKey(id)) {
            throw new IllegalArgumentException("Could not get content type from id '" + id.toString() + "', as it was not registered!");
        }
        TaskType contentType = idToContent.get(id);
        return contentType;
    }

    public static Identifier getId(TaskType powerType) {
        return powerType.getIdentifier();
    }

    public static boolean contains(Identifier id) {
        return idToContent.containsKey(id);
    }

    public static void clear() {
        idToContent.clear();
    }

    public static void reset() {
        clear();
    }
}
