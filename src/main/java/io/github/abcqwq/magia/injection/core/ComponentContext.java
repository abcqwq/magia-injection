package io.github.abcqwq.magia.injection.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ComponentContext {

    private static final Map<Class<?>, Object> componentMap = new HashMap<>();
    private ComponentContext() {}

    protected static <T> void register(Class<T> clazz, Object instance) {
        componentMap.put(clazz, instance);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getComponent(Class<T> clazz) {
        return (T) componentMap.get(clazz);
    }

    protected static boolean contains(Class<?> clazz) {
        return componentMap.containsKey(clazz);
    }

    @SuppressWarnings("unchecked")
    protected static <T> List<T> getInstancesOf(Class<T> clazz) {
        return componentMap.entrySet().stream()
                .filter(entry -> clazz.isAssignableFrom(entry.getKey()))
                .map(Map.Entry::getValue)
                .map(a -> (T) a)
                .collect(Collectors.toList());
    }
}
