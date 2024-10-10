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
 * The <a href="https://openjdk.org/jeps/451">JEP 451</a>, delivered with JDK 21,
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
        return instrumentation;
    }
}
