/*
 * Copyright (c) 2024 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.lang.instrument.Instrumentation;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.Installer;

import static org.mockito.internal.util.StringUtil.join;

public class PremainAttachAccess {

    public static Instrumentation getInstrumentation() {
        // A Java agent is always added to the system class loader. If Mockito is executed from a
        // different class loader we need to make sure to resolve the instrumentation instance
        // from there, or fail the resolution, if this class does not exist on the system class
        // loader.
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        Instrumentation instrumentation =
                doGetInstrumentation(PremainAttach.class, "getInstrumentation", systemClassLoader);
        if (instrumentation == null) {
            try {
                // At this point, we do not want to attempt dynamic attach but check the Byte Buddy
                // agent as a fallback if the Mockito agent is not attached.
                instrumentation =
                        doGetInstrumentation(
                                Installer.class, "getInstrumentation", systemClassLoader);
            } catch (IllegalStateException ignored) {
            }
        }
        if (instrumentation == null) {
            if (ClassFileVersion.ofThisVm().isAtLeast(ClassFileVersion.JAVA_V21)) {
                System.out.println(
                        "Mockito is currently self-attaching to enable the inline-mock-maker. This "
                                + "will no longer work in future releases of the JDK. Please add Mockito as an agent to your "
                                + "build what is described in Mockito's documentation: "
                                + "https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html#0.3");
            }
            // Attempt to dynamically attach, as a last resort.
            instrumentation = ByteBuddyAgent.install();
        }
        if (!instrumentation.isRetransformClassesSupported()) {
            throw new IllegalStateException(
                    join(
                            "Mockito requires retransformation for creating inline mocks. This feature is unavailable on the current VM.",
                            "",
                            "You cannot use this mock maker on this VM"));
        }
        return instrumentation;
    }

    private static Instrumentation doGetInstrumentation(
            Class<?> type, String method, ClassLoader systemClassLoader) {
        try {
            return (Instrumentation)
                    Class.forName(type.getName(), true, systemClassLoader)
                            .getMethod(method)
                            .invoke(null);
        } catch (Exception ignored) {
            return null;
        }
    }
}
