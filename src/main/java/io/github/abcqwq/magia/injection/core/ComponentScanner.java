package io.github.abcqwq.magia.injection.core;

import io.github.abcqwq.magia.injection.annotation.Component;
import io.github.abcqwq.magia.injection.annotation.Experimental;

import java.util.Set;
import java.util.stream.Collectors;

public class ComponentScanner {

    @Experimental("Unreliable, please use the other API")
    public static Set<Class<?>> scan() {
        var callerPackage = getCallerPackage();
        return scan(callerPackage);
    }

    public static Set<Class<?>> scan(String basePackage) {
        return scan(Set.of(basePackage));
    }

    public static Set<Class<?>> scan(Set<String> packages) {
        return packages.stream()
                .flatMap(pkg -> ClassScanner.scanPackage(pkg).stream())
                .filter(cls -> cls.isAnnotationPresent(Component.class))
                .collect(Collectors.toSet());
    }

    private static String getCallerPackage() {
        return StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
                .walk(frames -> frames
                        .reduce((first, second) -> second)
                        .map(frame -> frame.getDeclaringClass().getPackageName())
                        .orElseThrow(() -> new IllegalStateException("Could not determine caller package")));
    }
}
