package io.github.ThatRobin.ccpacks.util;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class UniversalPowerRegistry {

    private static HashMap<Identifier, UniversalPower> idToUP = new HashMap<>();

    public static UniversalPower register(UniversalPower origin) {
        return register(origin.getIdentifier(), origin);
    }

    public static UniversalPower register(Identifier id, UniversalPower origin) {
        if(idToUP.containsKey(id)) {
            throw new IllegalArgumentException("Duplicate origin id tried to register: '" + id.toString() + "'");
        }
        idToUP.put(id, origin);
        return origin;
    }

    protected static UniversalPower update(Identifier id, UniversalPower origin) {
        if(idToUP.containsKey(id)) {
            UniversalPower old = idToUP.get(id);
            idToUP.remove(id);
        }
        return register(id, origin);
    }

    public static int size() {
        return idToUP.size();
    }

    public static Stream<Identifier> identifiers() {
        return idToUP.keySet().stream();
    }

    public static Iterable<Map.Entry<Identifier, UniversalPower>> entries() {
        return idToUP.entrySet();
    }

    public static Iterable<UniversalPower> values() {
        return idToUP.values();
    }

    public static UniversalPower get(Identifier id) {
        if(!idToUP.containsKey(id)) {
            throw new IllegalArgumentException("Could not get origin from id '" + id.toString() + "', as it was not registered!");
        }
        UniversalPower origin = idToUP.get(id);
        return origin;
    }

    public static boolean contains(Identifier id) {
        return idToUP.containsKey(id);
    }

    public static boolean contains(UniversalPower origin) {
        return contains(origin.getIdentifier());
    }

    public static void clear() {
        idToUP.clear();
    }

    public static void reset() {
        clear();
    }
}
