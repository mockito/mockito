/*
 * Copyright (c) 2024 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.lang.instrument.Instrumentation;

/**
 * Allows users to specify Mockito as a Java agent where the {@link Instrumentation}
 * instance is stored for use by the inline mock maker.
 *
 * The <a href="https://openjdk.org/jeps/451">JET 451</a>, delivered with JDK 21,
 * is the first milestone to disallow dynamic loading of by default which will happen
 * in a future version of the JDK.
 */
public class PremainAttach {

    private static volatile Instrumentation instrumentation;

    public static void premain(String arg, Instrumentation instrumentation) {
        if (PremainAttach.instrumentation != null) {
            return;
        }
        PremainAttach.instrumentation = instrumentation;
    }

    public static Instrumentation getInstrumentation() {
        // A Java agent is always added to the system class loader. If Mockito is executed from a
        // different class loader we need to make sure to resolve the instrumentation instance
        // from there, or fail the resolution, if this class does not exist on the system class
        // loader.
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        if (PremainAttach.class.getClassLoader() == systemClassLoader) {
            return instrumentation;
        } else {
            try {
                return (Instrumentation)
                        Class.forName(PremainAttach.class.getName(), true, systemClassLoader)
                                .getMethod("getInstrumentation")
                                .invoke(null);
            } catch (Exception ignored) {
                return null;
            }
        }
    }
}
