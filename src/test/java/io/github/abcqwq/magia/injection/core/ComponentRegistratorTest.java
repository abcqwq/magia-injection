package io.github.abcqwq.magia.injection.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComponentRegistratorTest {

    private static final String BASE_PACKAGE = "io.github.abcqwq.cyclic";

    @Test
    void registerComponents_shouldThrowForCyclicDependency() {
        var exception = assertThrows(
            IllegalStateException.class, () -> ComponentRegistrator.registerComponents(BASE_PACKAGE)
        );

        assertTrue(exception.getMessage().contains("Cyclic detected between components"));
    }
}
