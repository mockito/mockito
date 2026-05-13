/*
 * Copyright (c) 2024 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import org.mockito.internal.creation.bytebuddy.ClinitSuppressionTransformer;

import java.lang.instrument.Instrumentation;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Allows users to specify Mockito as a Java agent where the {@link Instrumentation}
 * instance is stored for use by the inline mock maker.
 *
 * The <a href="https://openjdk.org/jeps/451">JEP 451</a>, delivered with JDK 21,
 * is the first milestone to disallow dynamic loading of by default which will happen
 * in a future version of the JDK.
 */
public class PremainAttach {

    private static volatile Instrumentation instrumentation;
    private static final String SUPPRESS_CLINIT_PROPERTY = "mockito.suppress.clinit";

    public static void premain(String arg, Instrumentation instrumentation) {
        if (PremainAttach.instrumentation != null) {
            return;
        }
        Predicate<String> shouldSuppress =
                buildClinitPredicate(System.getProperty(SUPPRESS_CLINIT_PROPERTY));
        if (shouldSuppress != null) {
            System.err.println(
                    "Suppressing static class initializer for '"
                            + System.getProperty(SUPPRESS_CLINIT_PROPERTY)
                            + "' (see mockito.suppress.clinit system property)");
            instrumentation.addTransformer(new ClinitSuppressionTransformer(shouldSuppress), false);
        }
        PremainAttach.instrumentation = instrumentation;
    }

    public static Instrumentation getInstrumentation() {
        return instrumentation;
    }

    /**
     * Parses the {@code mockito.suppress.clinit} property value into a predicate over
     * fully-qualified class names. The property is a comma-separated list; each entry is
     * either an exact class name ({@code com.example.MyClass}) or a package wildcard ending
     * in {@code .*} ({@code com.example.*}) that matches the package and all sub-packages.
     * Whitespace is trimmed; empty entries are ignored.
     *
     * @param property the property value, may be {@code null}
     * @return the predicate, or {@code null} if no entries are configured
     */
    static Predicate<String> buildClinitPredicate(String property) {
        if (property == null || property.trim().isEmpty()) {
            return null;
        }
        Set<String> classes = new HashSet<>();
        Set<String> packages = new HashSet<>();
        for (String name : property.split(",")) {
            String trimmed = name.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            if (trimmed.endsWith(".*")) {
                // Drop only the trailing '*' and keep the preceding '.' so that
                // "com.example." correctly rejects "com.examples.Foo".
                packages.add(trimmed.substring(0, trimmed.length() - 1));
            } else {
                classes.add(trimmed);
            }
        }
        if (classes.isEmpty() && packages.isEmpty()) {
            return null;
        }
        return name -> {
            if (classes.contains(name)) {
                return true;
            }
            for (String prefix : packages) {
                if (name.startsWith(prefix)) {
                    return true;
                }
            }
            return false;
        };
    }
}
