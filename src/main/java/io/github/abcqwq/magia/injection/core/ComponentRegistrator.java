package io.github.abcqwq.magia.injection.core;

import io.github.abcqwq.magia.injection.annotation.Component;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;

public class ComponentRegistrator {

    public static void registerComponents(String basePackage) {
        registerComponents(Set.of(basePackage));
    }

    public static void registerComponents(Set<String> packages) {

        var componentClasses = ComponentScanner.scan(packages);
        for (Class<?> cls : componentClasses) {
            createBean(cls);
        }
    }

    private static Object createBean(Class<?> cls) {

        if (!cls.isAnnotationPresent(Component.class)) {
            throw new IllegalStateException(String.format("No instance of %s class found", cls));
        }

        if (ComponentContext.contains(cls)) {
            return ComponentContext.getComponent(cls);
        }

        var constructor = selectConstructor(cls);
        var args = Arrays.stream(constructor.getParameterTypes())
                .map(ComponentRegistrator::createBean)
                .toArray();

        try {

            var instance = constructor.newInstance(args);
            ComponentContext.register(cls, instance);
            return instance;

        } catch (Exception e) {
            throw new RuntimeException(String.format("Failed to instantiate %s", cls), e);
        }
    }

    private static Constructor<?> selectConstructor(Class<?> cls) {

        var constructors = cls.getDeclaredConstructors();

        return Arrays.stream(constructors)
                .max(Comparator.comparingInt(Constructor::getParameterCount))
                .orElseThrow(() ->
                        new RuntimeException(String.format("No constructor found for %s", cls)));
    }

}
