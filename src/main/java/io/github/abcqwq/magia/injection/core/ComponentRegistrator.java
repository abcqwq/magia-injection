package io.github.abcqwq.magia.injection.core;

import io.github.abcqwq.magia.injection.annotation.Component;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ComponentRegistrator {
    private static final HashSet<Class<?>> processedComponents = new HashSet<>();
    private static final Stack<Class<?>> processedComponentsPath = new Stack<>();

    private ComponentRegistrator() {}

    public static void registerComponents(String basePackage) {
        registerComponents(Set.of(basePackage));
    }

    public static void registerComponents(Set<String> packages) {
        var componentClasses = ComponentScanner.scan(packages);
        for (Class<?> cls : componentClasses) {
            createComponent(cls);
        }
    }

    private static Object createComponent(Class<?> cls) {

        if (!cls.isAnnotationPresent(Component.class)) {
            throw new IllegalStateException(String.format("No instance of %s class found", cls));
        }

        if (processedComponents.contains(cls)) {
            throw new IllegalStateException(String.format("Cyclic detected between components: %s", reconstructCyclicPath()));
        }

        if (ComponentContext.contains(cls)) {
            return ComponentContext.getComponent(cls);
        }

        processedComponents.add(cls);
        processedComponentsPath.push(cls);

        var constructor = selectConstructor(cls);
        var args = Arrays.stream(constructor.getParameterTypes())
                .map(ComponentRegistrator::createComponent)
                .toArray();

        try {

            var instance = constructor.newInstance(args);
            ComponentContext.register(cls, instance);
            return instance;

        } catch (Exception e) {
            throw new RuntimeException(String.format("Failed to instantiate %s", cls), e);
        } finally {
            processedComponents.remove(cls);
            processedComponentsPath.pop();
        }
    }

    private static Constructor<?> selectConstructor(Class<?> cls) {

        var constructors = cls.getDeclaredConstructors();

        return Arrays.stream(constructors)
                .max(Comparator.comparingInt(Constructor::getParameterCount))
                .orElseThrow(() ->
                        new RuntimeException(String.format("No constructor found for %s", cls)));
    }

    private static String reconstructCyclicPath() {
        var pathDelimiter = " -> ";

        return processedComponentsPath.stream()
                .map(Class::getName)
                .collect(Collectors.joining(pathDelimiter, "", pathDelimiter + processedComponentsPath.firstElement().getName()));
    }

    public static <T> void apply(Class<T> clazz, Consumer<T> consumer) {
        ComponentContext.getInstancesOf(clazz).forEach(consumer);
    }

}
