/*
 * Copyright (c) 2024 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.junit.jupiter;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.create;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.ScopedMock;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.framework.DefaultMockitoSession;
import org.mockito.internal.junit.UniversalTestListener;
import org.mockito.internal.session.MockitoSessionLoggerAdapter;
import org.mockito.internal.util.MockUtil;
import org.mockito.quality.Strictness;

public class MockitoLightweightExtension implements BeforeEachCallback, AfterEachCallback {
    private static final ExtensionContext.Namespace MOCKITO = create("org.mockito");

    private static final String SESSION = "session", MOCKS = "mocks";

    @SuppressWarnings("rawtypes")
    private static final MockSettingsImpl SETTING = new MockSettingsImpl();

    private static final Field field;

    static {
        try {
            field = DefaultMockitoSession.class.getDeclaredField("listener");
            field.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void beforeEach(final ExtensionContext context) throws Exception {
        var session =
                Mockito.mockitoSession()
                        .logger(new MockitoSessionLoggerAdapter(Plugins.getMockitoLogger()))
                        .strictness(Strictness.STRICT_STUBS)
                        .startMocking();

        var listener = (UniversalTestListener) field.get(session);
        for (var instance : context.getRequiredTestInstances().getAllInstances()) {
            listen(instance, listener);
        }

        context.getStore(MOCKITO).put(MOCKS, new HashSet<>());
        context.getStore(MOCKITO).put(SESSION, session);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void afterEach(ExtensionContext context) {
        context.getStore(MOCKITO)
                .remove(MOCKS, Set.class)
                .forEach(mock -> ((ScopedMock) mock).closeOnDemand());
        context.getStore(MOCKITO)
                .remove(SESSION, MockitoSession.class)
                .finishMocking(context.getExecutionException().orElse(null));
    }

    private void listen(Object instance, UniversalTestListener listener)
            throws IllegalAccessException {
        for (var field : instance.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            var maybeMock = field.get(instance);
            if (MockUtil.isMock((maybeMock))) {
                listener.onMockCreated(maybeMock, SETTING);
            }
        }
    }
}
