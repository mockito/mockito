/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.plugins;

import java.util.List;
import org.mockito.plugins.AnnotationEngine;
import org.mockito.plugins.DoNotMockEnforcer;
import org.mockito.plugins.InstantiatorProvider2;
import org.mockito.plugins.MemberAccessor;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockResolver;
import org.mockito.plugins.MockitoLogger;
import org.mockito.plugins.PluginSwitch;
import org.mockito.plugins.StackTraceCleanerProvider;

class PluginRegistry {

    private final PluginSwitch pluginSwitch =
            new PluginLoader(new DefaultPluginSwitch()).loadPlugin(PluginSwitch.class);

    private final MockMaker mockMaker =
            new PluginLoader(
                            pluginSwitch,
                            DefaultMockitoPlugins.INLINE_ALIAS,
                            DefaultMockitoPlugins.PROXY_ALIAS)
                    .loadPlugin(MockMaker.class);

    private final MemberAccessor memberAccessor =
            new PluginLoader(pluginSwitch, DefaultMockitoPlugins.MODULE_ALIAS)
                    .loadPlugin(MemberAccessor.class);

    private final StackTraceCleanerProvider stackTraceCleanerProvider =
            new PluginLoader(pluginSwitch).loadPlugin(StackTraceCleanerProvider.class);

    private final InstantiatorProvider2 instantiatorProvider;

    private final AnnotationEngine annotationEngine =
            new PluginLoader(pluginSwitch).loadPlugin(AnnotationEngine.class);

    private final MockitoLogger mockitoLogger =
            new PluginLoader(pluginSwitch).loadPlugin(MockitoLogger.class);

    private final List<MockResolver> mockResolvers =
            new PluginLoader(pluginSwitch).loadPlugins(MockResolver.class);

    private final DoNotMockEnforcer doNotMockEnforcer =
            new PluginLoader(pluginSwitch).loadPlugin(DoNotMockEnforcer.class);

    PluginRegistry() {
        instantiatorProvider =
                new PluginLoader(pluginSwitch).loadPlugin(InstantiatorProvider2.class);
    }

    /**
     * The implementation of the stack trace cleaner
     */
    StackTraceCleanerProvider getStackTraceCleanerProvider() {
        // TODO we should throw some sensible exception if this is null.
        return stackTraceCleanerProvider;
    }

    /**
     * Returns the implementation of the mock maker available for the current runtime.
     *
     * <p>Returns {@link org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker} if no
     * {@link org.mockito.plugins.MockMaker} extension exists or is visible in the current classpath.</p>
     */
    MockMaker getMockMaker() {
        return mockMaker;
    }

    /**
     * Returns the implementation of the member accessor available for the current runtime.
     *
     * <p>Returns {@link org.mockito.internal.util.reflection.ReflectionMemberAccessor} if no
     * {@link org.mockito.plugins.MockMaker} extension exists or is visible in the current classpath.</p>
     */
    MemberAccessor getMemberAccessor() {
        return memberAccessor;
    }

    /**
     * Returns the instantiator provider available for the current runtime.
     *
     * <p>Returns {@link org.mockito.internal.creation.instance.DefaultInstantiatorProvider} if no
     * {@link org.mockito.plugins.InstantiatorProvider2} extension exists or is visible in the
     * current classpath.</p>
     */
    InstantiatorProvider2 getInstantiatorProvider() {
        return instantiatorProvider;
    }

    /**
     * Returns the annotation engine available for the current runtime.
     *
     * <p>Returns {@link org.mockito.internal.configuration.InjectingAnnotationEngine} if no
     * {@link org.mockito.plugins.AnnotationEngine} extension exists or is visible in the current classpath.</p>
     */
    AnnotationEngine getAnnotationEngine() {
        return annotationEngine;
    }

    /**
     * Returns the logger available for the current runtime.
     *
     * <p>Returns {@link org.mockito.internal.util.ConsoleMockitoLogger} if no
     * {@link org.mockito.plugins.MockitoLogger} extension exists or is visible in the current classpath.</p>
     */
    MockitoLogger getMockitoLogger() {
        return mockitoLogger;
    }

    /**
     * Returns the DoNotMock enforce for the current runtime.
     *
     * <p> Returns {@link org.mockito.internal.configuration.DefaultDoNotMockEnforcer} if no
     * {@link DoNotMockEnforcer} extension exists or is visible in the current classpath.</p>
     */
    DoNotMockEnforcer getDoNotMockEnforcer() {
        return doNotMockEnforcer;
    }

    /**
     * Returns a list of available mock resolvers if any.
     *
     * @return A list of available mock resolvers or an empty list if none are registered.
     */
    List<MockResolver> getMockResolvers() {
        return mockResolvers;
    }
}
