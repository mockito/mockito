/*
 * Copyright (c) 2024 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import org.mockito.internal.creation.bytebuddy.ClinitSuppressionTransformer;

import java.lang.instrument.Instrumentation;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
        Set<String> classes =
                parseSuppressClinitProperty(System.getProperty(SUPPRESS_CLINIT_PROPERTY));
        if (!classes.isEmpty()) {
            instrumentation.addTransformer(new ClinitSuppressionTransformer(classes), false);
        }
        PremainAttach.instrumentation = instrumentation;
    }

    public static Instrumentation getInstrumentation() {
        return instrumentation;
    }

    /**
     * Parses a comma-separated list of class names into a set, trimming whitespace from each
     * entry and ignoring empty entries.
     *
     * @param property the property value, may be {@code null}
     * @return an unmodifiable set of class names
     */
    static Set<String> parseSuppressClinitProperty(String property) {
        if (property == null || property.trim().isEmpty()) {
            return Collections.emptySet();
        }
        Set<String> result = new HashSet<>();
        for (String name : property.split(",")) {
            String trimmed = name.trim();
            if (!trimmed.isEmpty()) {
                result.add(trimmed);
            }
        }
        return Collections.unmodifiableSet(result);
    }
}
