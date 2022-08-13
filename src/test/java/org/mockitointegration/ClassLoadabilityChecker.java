/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitointegration;

import java.util.HashSet;
import java.util.Set;

/**
 * Check that classes can be loaded and initialized on a provided classloader. Used
 * for checking that Mockito has no dependency on libraries like JUnit.
 * <p>
 * Some classes are excluded from this checking - namely, classes that fail due to
 * the absence of Java classes. It's assumed that this is due to a specific optional
 * dependency on APIs available in certain Java versions and so other elements of the
 * test Matrix will check that those classes do not depend on JUnit or ByteBuddy. We
 * exclude based on the failure of a ClassNotFoundException, or a NoClassDefFoundError
 * caused by the failing to load of a failing parent class.
 */
public final class ClassLoadabilityChecker {
    private static final boolean INITIALIZE_CLASSES = true;
    private final Set<String> excludedClasses = new HashSet<>();
    private final ClassLoader classLoader;
    private final String purpose;

    public ClassLoadabilityChecker(ClassLoader classLoader, String purpose) {
        this.classLoader = classLoader;
        this.purpose = purpose;
    }

    public void checkLoadability(String className) {
        try {
            Class.forName(className, INITIALIZE_CLASSES, classLoader);
        } catch (ClassNotFoundException | LinkageError e) {
            if (isFailureExcluded(className, e)) {
                return;
            }
            e.printStackTrace();
            throw new AssertionError(
                    String.format("'%s' has some dependency to %s", className, purpose));
        }
    }

    private boolean isFailureExcluded(String loadedClass, Throwable thrown) {
        if (thrown == null) {
            return false;
        }
        if (thrown instanceof ClassNotFoundException) {
            ClassNotFoundException cnf = (ClassNotFoundException) thrown;
            if (cnf.getMessage().startsWith("java.")) {
                excludedClasses.add(loadedClass);
                return true;
            }
        } else if (thrown instanceof NoClassDefFoundError) {
            NoClassDefFoundError ncdf = (NoClassDefFoundError) thrown;
            // if Foo fails due to depending on a Java class, Foo$Bar will fail with a NCDFE
            int lastInnerClass = loadedClass.lastIndexOf('$');
            if (lastInnerClass != -1) {
                String parent = loadedClass.substring(0, lastInnerClass);
                if (excludedClasses.contains(parent) && ncdf.getMessage().contains(parent)) {
                    excludedClasses.add(loadedClass);
                    return true;
                }
            }
        }

        return isFailureExcluded(loadedClass, thrown.getCause());
    }
}
