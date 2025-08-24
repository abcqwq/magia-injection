package io.github.abcqwq.core;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;

public class ClassScanner {

    private static final String CLASS_SUFFIX = ".class";
    private static final String FILE_PROTOCOL = "file";
    private static final String JAR_PROTOCOL = "jar";
    private static final String EMPTY_STRING = "";

    private static final Character PACKAGE_SEPARATOR = '.';
    private static final Character DIRECTORY_SEPARATOR = '/';

    protected static Set<Class<?>> scanPackage(String basePackage) {

        var path = basePackage.replace(PACKAGE_SEPARATOR, DIRECTORY_SEPARATOR);

        try {

            var resources = Thread.currentThread().getContextClassLoader().getResources(path);
            var classes = new HashSet<Class<?>>();

            while (resources.hasMoreElements()) {

                var resource = resources.nextElement();
                var protocol = resource.getProtocol();

                if (FILE_PROTOCOL.equals(protocol)) {

                    classes.addAll(findClassesInDirectory(new File(resource.getFile()), basePackage));

                } else if (JAR_PROTOCOL.equals(protocol)) {

                    classes.addAll(findClassesInJar(resource, path));

                }
            }
            return classes;

        } catch (IOException e) {

            throw new RuntimeException(
                    String.format("Failed to scan package: %s", basePackage), e
            );
        }
    }

    private static Set<Class<?>> findClassesInDirectory(File directory, String packageName) {

        var classes = new HashSet<Class<?>>();
        if (!directory.exists()) return classes;

        var files = directory.listFiles();
        if (files == null) return classes;

        Arrays.stream(files).forEach(file -> {
            if (file.isDirectory()) {

                classes.addAll(
                        findClassesInDirectory(
                                file, packageName + PACKAGE_SEPARATOR + file.getName()
                        )
                );

            } else if (file.getName().endsWith(CLASS_SUFFIX)) {

                var className = (
                        packageName
                                + PACKAGE_SEPARATOR
                                + file.getName().replace(CLASS_SUFFIX, EMPTY_STRING)
                );

                try {
                    classes.add(Class.forName(className));
                } catch (ClassNotFoundException ignored) {}
            }
        });

        return classes;
    }

    private static Set<Class<?>> findClassesInJar(URL resource, String path) {

        var classes = new HashSet<Class<?>>();
        try {

            var jarConnection = (JarURLConnection) resource.openConnection();
            try (var jarFile = jarConnection.getJarFile()) {
                jarFile.stream()
                        .map(JarEntry::getName)
                        .filter(name -> name.startsWith(path) && name.endsWith(CLASS_SUFFIX))
                        .map(name -> name.replace(DIRECTORY_SEPARATOR, PACKAGE_SEPARATOR))
                        .map(name -> name.replace(CLASS_SUFFIX, EMPTY_STRING))
                        .forEach(className -> {
                            try {
                                classes.add(Class.forName(className));
                            } catch (ClassNotFoundException ignored) {}
                        });
            }

        } catch (IOException e) {
            throw new RuntimeException(String.format("Failed to scan JAR: %s", resource), e);
        }
        return classes;
    }
}

