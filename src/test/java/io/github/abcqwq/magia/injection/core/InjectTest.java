package io.github.abcqwq.magia.injection.core;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import testpkgs.inject.DependencyClass;
import testpkgs.inject.field.FieldClass;
import testpkgs.inject.method.MethodClass;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InjectTest {

    private static final String BASE_PACKAGE = "testpkgs.inject";

    @BeforeAll
    public static void setUp() {
        ComponentRegistrator.registerComponents(BASE_PACKAGE);
    }

    @Test
    void injectField_shouldInvokeDependencyMethodSuccessfully() {
        var instance = ComponentContext.getComponent(FieldClass.class);
        var dependency = ComponentContext.getComponent(DependencyClass.class);
        assertNotNull(instance);
        assertNotNull(dependency);
        assertEquals(10, instance.invokeTimesTwo(5));
    }

    @Test
    void injectMethod_shouldInvokeDependencyMethodSuccessfully() {
        var instance = ComponentContext.getComponent(MethodClass.class);
        var dependency = ComponentContext.getComponent(DependencyClass.class);
        assertNotNull(instance);
        assertNotNull(dependency);
        assertEquals(12, instance.invokeTimesTwo(6));
    }

}
