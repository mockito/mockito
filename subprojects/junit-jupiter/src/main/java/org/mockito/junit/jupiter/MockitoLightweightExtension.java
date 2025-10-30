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
    private static final String SESSION = "session";
    private static final String MOCKS = "mocks";
    private static final String LISTENER = "listener";
    private static final MockSettingsImpl<?> SETTING = new MockSettingsImpl<>();
    private static final Field listenerField = makeListenerAccessible();

    @Override
    public void beforeEach(final ExtensionContext context) throws Exception {
        var session = Mockito.mockitoSession()
                .logger(new MockitoSessionLoggerAdapter(Plugins.getMockitoLogger()))
                .strictness(Strictness.STRICT_STUBS)
                .startMocking();

        var listener = (UniversalTestListener) listenerField.get(session);
        for (var instance : context.getRequiredTestInstances().getAllInstances()) {
            listen(instance, listener);
        }

        var store = context.getStore(MOCKITO);
        store.put(MOCKS, new HashSet<>());
        store.put(SESSION, session);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void afterEach(ExtensionContext context) {
        var store = context.getStore(MOCKITO);
        store.remove(MOCKS, Set.class).forEach(mock -> ((ScopedMock) mock).closeOnDemand());
        store.remove(SESSION, MockitoSession.class)
                .finishMocking(context.getExecutionException().orElse(null));
    }

    @SuppressWarnings("java:S3011")
    private void listen(Object instance, UniversalTestListener listener) throws IllegalAccessException {
        for (var field : instance.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            var maybeMock = field.get(instance);
            if (MockUtil.isMock((maybeMock))) {
                listener.onMockCreated(maybeMock, SETTING);
            }
        }
    }

    /**
     * This is unfortunately the only way to retrieve the listener.
     *
     */
    @SuppressWarnings("java:S3011")
    private static Field makeListenerAccessible() {
        try {
            var listener = DefaultMockitoSession.class.getDeclaredField(LISTENER);
            listener.setAccessible(true);
            return listener;
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Unable to retrieve %s %s field from %s"
                            .formatted(UniversalTestListener.class, LISTENER, DefaultMockitoSession.class),
                    e);
        }
    }
}
