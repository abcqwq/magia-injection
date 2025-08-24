package io.github.abcqwq.magia.injection.core;

import io.github.abcqwq.magia.injection.annotation.Component;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Component
class AnnotatedClass {}
class NonAnnotatedClass {}

class ComponentScannerTest {

    private static final String BASE_PACKAGE = "io.github.abcqwq.magia.injection";

    @Test
    void scan() {
        ComponentScanner.scan();
    }

    @Test
    void scan_specificPackage_shouldReturnAnnotatedClasses() {
        var classes = ComponentScanner.scan(BASE_PACKAGE);

        assertEquals(1, classes.size());
        assertTrue(classes.contains(AnnotatedClass.class), "Should include annotated classes in the specified package");
        assertFalse(classes.contains(NonAnnotatedClass.class), "Should not include annotated classes in the specified package");
    }

    @Test
    void scan_noAnnotatedClasses_shouldReturnEmptySet() {
        var classes = ComponentScanner.scan("java.lang");
        assertTrue(classes.isEmpty(), "Should return empty set if no annotated classes found");
    }

}
