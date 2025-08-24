package io.github.abcqwq.magia.injection.core;

import io.github.abcqwq.magia.injection.core.recursion.RootType;
import io.github.abcqwq.magia.injection.core.recursion.level1.Level1Type;
import io.github.abcqwq.magia.injection.core.recursion.level1.level2.Level2Type;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ClassScannerTest {
    
    private static final String TEST_PACKAGE = "io.test";
    private static final String BASE_PACKAGE = "io.github.abcqwq.magia.injection";

    @TempDir
    File tempDir;

    @Test
    void scanPackage_shouldFindCurrentTestClass() {

        var testPackage = this.getClass().getPackageName();
        var classes = ClassScanner.scanPackage(testPackage);

        assertTrue(classes.contains(this.getClass()),
                "Expected ClassScanner to find " + this.getClass().getName());

        assertTrue(classes.contains(RootType.class),
                "Expected ClassScanner to find " + RootType.class);

        assertTrue(classes.contains(Level1Type.class),
                "Expected ClassScanner to find " + Level1Type.class);

        assertTrue(classes.contains(Level2Type.class),
                "Expected ClassScanner to find " + Level2Type.class);
    }

    @Test
    void scanPackage_shouldReturnEmptySetForNonExistingPackage() {
        var fakePackage = "io.github.abcqwq.magia.injection.doesnotexist";
        var classes = ClassScanner.scanPackage(fakePackage);

        assertTrue(classes.isEmpty(),
                "Expected no classes for non-existing package");
    }

    @Test
    void findClassesInDirectory_directoryDoesNotExist() {
        var nonExistent = new File(tempDir, "notHere");
        var result = invokeFindClassesInDirectory(nonExistent, TEST_PACKAGE);

        assertTrue(result.isEmpty(), "Should return empty when directory does not exist");
    }

    @Test
    void findClassesInDirectory_listFilesIsNull() {

        var notADir = new File(tempDir, "afile.txt");
        try {
            assertTrue(notADir.createNewFile());
        } catch (IOException e) {
            fail("Failed to create file");
        }

        var result = invokeFindClassesInDirectory(notADir, TEST_PACKAGE);
        assertTrue(result.isEmpty(), "Should return empty when listFiles() returns null");
    }

    @Test
    void findClassesInDirectory_testIgnoresNonClassFiles() throws Exception {
        var pkgDir = new File(tempDir, TEST_PACKAGE);
        assertTrue(pkgDir.mkdir());

        var txtFile = new File(pkgDir, "notes.txt");
        assertTrue(txtFile.createNewFile());

        var result = invokeFindClassesInDirectory(pkgDir, TEST_PACKAGE);
        assertTrue(result.isEmpty(), "Should ignore non-class files like .txt");
    }

    @Test
    void scanPackage_whenIOException_thenThrowsRuntimeException() {

        var brokenLoader = new ClassLoader() {
            @Override
            public Enumeration<URL> getResources(String name) throws IOException {
                throw new IOException();
            }
        };

        var current = Thread.currentThread();
        var original = current.getContextClassLoader();
        current.setContextClassLoader(brokenLoader);

        var exception = assertThrows(
                RuntimeException.class, () -> ClassScanner.scanPackage(TEST_PACKAGE)
        );
        current.setContextClassLoader(original);

        assertEquals("Failed to scan package: io.test", exception.getMessage());
    }

    @SuppressWarnings("unchecked")
    private Set<Class<?>> invokeFindClassesInDirectory(File dir, String pkg) {

        try {
            var method = ClassScanner.class.getDeclaredMethod("findClassesInDirectory", File.class, String.class);
            method.setAccessible(true);
            return (Set<Class<?>>) method.invoke(null, dir, pkg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
