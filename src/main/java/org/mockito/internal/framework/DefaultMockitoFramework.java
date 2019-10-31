/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.framework;

import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

import org.mockito.MockitoFramework;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.invocation.DefaultInvocationFactory;
import org.mockito.internal.util.Checks;
import org.mockito.invocation.InvocationFactory;
import org.mockito.listeners.MockitoListener;
import org.mockito.plugins.InlineMockMaker;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockitoPlugins;

public class DefaultMockitoFramework implements MockitoFramework {

    public MockitoFramework addListener(MockitoListener listener) {
        Checks.checkNotNull(listener, "listener");
        mockingProgress().addListener(listener);
        return this;
    }

    public MockitoFramework removeListener(MockitoListener listener) {
        Checks.checkNotNull(listener, "listener");
        mockingProgress().removeListener(listener);
        return this;
    }

    @Override
    public MockitoPlugins getPlugins() {
        return Plugins.getPlugins();
    }

    @Override
    public InvocationFactory getInvocationFactory() {
        return new DefaultInvocationFactory();
    }

    private InlineMockMaker getInlineMockMaker() {
        MockMaker mockMaker = Plugins.getMockMaker();
        return (mockMaker instanceof InlineMockMaker) ? (InlineMockMaker) mockMaker : null;
    }

    @Override
    public void clearInlineMocks() {
        InlineMockMaker mockMaker = getInlineMockMaker();
        if (mockMaker != null) {
            mockMaker.clearAllMocks();
        }
    }

    @Override
    public void clearInlineMock(Object mock) {
        InlineMockMaker mockMaker = getInlineMockMaker();
        if (mockMaker != null) {
            mockMaker.clearMock(mock);
        }
    }
}
