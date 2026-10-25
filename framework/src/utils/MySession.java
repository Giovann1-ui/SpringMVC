package utils;

import java.util.HashMap;
import java.util.Map;

public class MySession {
    private final Map<String, Object> attributes;
    private final Map<String, Object> initialAttributes;
    private boolean invalidated = false;

    MySession(Map<String, Object> attributes) {
        this.attributes = new HashMap<>(attributes);
        this.initialAttributes = new HashMap<>(attributes);
    }

    // API pour le developpeur
    public Object get(String key) {
        return attributes.get(key);
    }

    public boolean has(String key) {
        return attributes.containsKey(key);
    }

    public void add(String key, Object value) {
        attributes.put(key, value);
    }

    public void delete(String key) {
        attributes.remove(key);
    }

    public void invalidate() {
        attributes.clear();
        invalidated = true;
    }

    // reserve au framework (visibilite package)
    Map<String, Object> getAttributes() {
        return attributes;
    }

    Map<String, Object> getInitialAttributes() {
        return initialAttributes;
    }

    boolean isInvalidated() {
        return invalidated;
    }
}