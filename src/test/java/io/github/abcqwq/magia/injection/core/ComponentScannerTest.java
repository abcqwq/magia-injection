package io.github.abcqwq.magia.injection.core;

import org.junit.jupiter.api.Test;
import testpkgs.simple.AnnotatedClass;
import testpkgs.simple.NonAnnotatedClass;

import static org.junit.jupiter.api.Assertions.*;

class ComponentScannerTest {

    private static final String BASE_PACKAGE = "testpkgs.simple";

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
