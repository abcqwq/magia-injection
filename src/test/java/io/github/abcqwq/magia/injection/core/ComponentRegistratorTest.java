package io.github.abcqwq.magia.injection.core;

import org.junit.jupiter.api.Test;

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
}
