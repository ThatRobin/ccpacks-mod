package io.github.ThatRobin.ccpacks.Factories.MechanicFactories;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class MechanicRegistry {
    private static HashMap<Identifier, MechanicType> idToMechanic = new HashMap<>();

    public static MechanicType register(Identifier id, MechanicType contentType) {
        if(idToMechanic.containsKey(id)) {
            throw new IllegalArgumentException("Duplicate content type id tried to register: '" + id.toString() + "'");
        }
        idToMechanic.put(id, contentType);
        return contentType;
    }

    protected static MechanicType update(Identifier id, MechanicType contentType) {
        if(idToMechanic.containsKey(id)) {
            MechanicType old = idToMechanic.get(id);
            idToMechanic.remove(id);
        }
        return register(id, contentType);
    }

    public static int size() {
        return idToMechanic.size();
    }

    public static Stream<Identifier> identifiers() {
        return idToMechanic.keySet().stream();
    }

    public static Iterable<Map.Entry<Identifier, MechanicType>> entries() {
        return idToMechanic.entrySet();
    }

    public static Iterable<MechanicType> values() {
        return idToMechanic.values();
    }

    public static MechanicType get(Identifier id) {
        if(!idToMechanic.containsKey(id)) {
            throw new IllegalArgumentException("Could not get content type from id '" + id.toString() + "', as it was not registered!");
        }
        MechanicType contentType = idToMechanic.get(id);
        return contentType;
    }

    public static Identifier getId(MechanicType powerType) {
        return powerType.getIdentifier();
    }

    public static boolean contains(Identifier id) {
        return idToMechanic.containsKey(id);
    }

    public static void clear() {
        idToMechanic.clear();
    }

    public static void reset() {
        clear();
    }
}
