package io.github.thatrobin.ccpacks.factories.content_factories;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class ContentRegistry {
    private static final HashMap<Identifier, ContentType> idToContent = new HashMap<>();

    public static ContentType register(Identifier id, ContentType contentType) {
        if(idToContent.containsKey(id)) {
            throw new IllegalArgumentException("Duplicate content type id tried to register: '" + id.toString() + "'");
        }
        idToContent.put(id, contentType);
        return contentType;
    }

    public static int size() {
        return idToContent.size();
    }

    public static Stream<Identifier> identifiers() {
        return idToContent.keySet().stream();
    }

    public static Iterable<Map.Entry<Identifier, ContentType>> entries() {
        return idToContent.entrySet();
    }

    public static ContentType get(Identifier id) {
        if(!idToContent.containsKey(id)) {
            throw new IllegalArgumentException("Could not get content type from id '" + id.toString() + "', as it was not registered!");
        }
        return idToContent.get(id);
    }

    public static Identifier getId(ContentType powerType) {
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
