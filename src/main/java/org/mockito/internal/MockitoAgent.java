/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.lang.instrument.Instrumentation;

public class MockitoAgent {

    private static Instrumentation instrumentation;

    public static void premain(String arg, Instrumentation instrumentation) {
        MockitoAgent.instrumentation = instrumentation;
    }

    public static Instrumentation getInstrumentation() {
        Class<?> caller =
                StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass();
        try {
            if (caller
                            != Class.forName(
                                    "org.mockito.internal.creation.bytebuddy.InlineDelegateByteBuddyMockMaker")
                    && caller
                            != Class.forName(
                                    "org.mockito.internal.util.reflection.InstrumentationMemberAccessor")) {
                throw new RuntimeException(
                        "Cannot access Mockito agent from unknown class: " + caller);
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        return instrumentation;
    }
}
