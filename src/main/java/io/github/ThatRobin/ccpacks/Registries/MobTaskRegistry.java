package io.github.ThatRobin.ccpacks.Registries;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class MobTaskRegistry {
    private static HashMap<Identifier, Goal> idToUP = new HashMap<>();

    public static Goal register(Identifier id, Goal power) {
        if(idToUP.containsKey(id)) {
            throw new IllegalArgumentException("Duplicate goal id tried to register: '" + id.toString() + "'");
        }
        idToUP.put(id, power);
        return power;
    }

    public static int size() {
        return idToUP.size();
    }

    public static Iterable<Map.Entry<Identifier, Goal>> entries() {
        return idToUP.entrySet();
    }

    public static Goal get(Identifier id) {
        if(!idToUP.containsKey(id)) {
            throw new IllegalArgumentException("Could not get goal from id '" + id.toString() + "', as it was not registered!");
        }
        Goal power = idToUP.get(id);
        return power;
    }

    public static boolean contains(Identifier id) {
        return idToUP.containsKey(id);
    }

    public static boolean contains(Goal goal) {
        return idToUP.containsValue(goal);
    }

    public static void clear() {
        idToUP.clear();
    }

    public static void reset() {
        clear();
    }
}
