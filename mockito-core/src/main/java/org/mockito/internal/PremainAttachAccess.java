/*
 * Copyright (c) 2024 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.Installer;

import java.lang.instrument.Instrumentation;
import java.util.List;

import static org.mockito.internal.util.StringUtil.join;

public class PremainAttachAccess {

    private static volatile Instrumentation inst;

    public static Instrumentation getInstrumentation() {
        if (inst != null) {
            return inst;
        }
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
                boolean dynamicAgentLoading;
                try {
                    Object runtimeMXBean =
                            Class.forName("java.lang.management.ManagementFactory")
                                    .getMethod("getRuntimeMXBean")
                                    .invoke(null);

                    @SuppressWarnings("unchecked")
                    List<String> arguments =
                            (List<String>)
                                    Class.forName("java.lang.management.RuntimeMXBean")
                                            .getMethod("getInputArguments")
                                            .invoke(runtimeMXBean);
                    dynamicAgentLoading =
                            arguments.stream()
                                    .anyMatch(
                                            argument ->
                                                    argument.contains(
                                                            "-XX:+EnableDynamicAgentLoading"));
                } catch (Exception ignored) {
                    dynamicAgentLoading = false;
                }
                if (!dynamicAgentLoading) {
                    // Cannot use `Plugins.getMockitoLogger().warn(...)` at this time due to a
                    // circular dependency on `Plugins.registry`.
                    // The `PluginRegistry` is not yet fully initialized (in `Plugins`), because it
                    // is currently initializing the `MockMaker` which is a
                    // InlineByteBuddyMockMaker, and it is later calling this
                    // method to access the instrumentation.
                    System.err.println(
                            "Mockito is currently self-attaching to enable the inline-mock-maker. This "
                                    + "will no longer work in future releases of the JDK. Please add Mockito as an agent to your "
                                    + "build as described in Mockito's documentation: "
                                    + "https://javadoc.io/doc/org.mockito/mockito-core/latest/org.mockito/org/mockito/Mockito.html#0.3");
                }
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
        inst = instrumentation;
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
