package io.github.abcqwq.magia.injection.core;

import org.junit.jupiter.api.Test;
import testpkgs.inheritance.Child1Class;
import testpkgs.inheritance.Child2Class;
import testpkgs.inheritance.ParentClass;
import testpkgs.inheritance.UnrelatedClass;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ComponentRegistratorTest {

    @Test
    void registerComponents_shouldThrowForCyclicDependency() {
        var testPackage = "testpkgs.cyclic";

        var exception = assertThrows(
            IllegalStateException.class, () -> ComponentRegistrator.registerComponents(testPackage)
        );

        assertTrue(exception.getMessage().contains("Cyclic detected between components"));
    }

    @Test
    void apply_shouldInvokeConsumerOnEachInstance() {
        var testPackage = "testpkgs.inheritance";

        ComponentRegistrator.registerComponents(testPackage);

        List<ParentClass> received = new ArrayList<>();

        ComponentRegistrator.apply(ParentClass.class, received::add);

        var parentInstance = ComponentContext.getComponent(ParentClass.class);
        var child1Instance = ComponentContext.getComponent(Child1Class.class);
        var child2Instance = ComponentContext.getComponent(Child2Class.class);
        var unrelatedInstance = ComponentContext.getComponent(UnrelatedClass.class);

        assertEquals(3, received.size());
        assertTrue(received.contains(parentInstance), "Should apply consumer to the input class");
        assertTrue(received.contains(child1Instance), "Should apply consumer to child of input class");
        assertTrue(received.contains(child2Instance), "Should apply consumer to children of input class");
        assertFalse(received.contains(unrelatedInstance), "Should not apply consumer to unrelated class");
    }

}
