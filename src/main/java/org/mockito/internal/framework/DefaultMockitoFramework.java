/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.framework;

import org.mockito.Mockito;
import org.mockito.MockitoFramework;
import org.mockito.internal.util.Checks;
import org.mockito.listeners.MockitoListener;

import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

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

    /**
     * Adds listener only if it is an instance of MockitoListener.
     * Useful to avoid casting in internal code.
     */
    public static void tryAddListner(Object listener) {
        if (listener instanceof MockitoListener) {
            Mockito.framework().addListener((MockitoListener) listener);
        }
    }

    /**
     * Removes listener only if it is an instance of MockitoListener.
     * Useful to avoid casting in internal code.
     */
    public static void tryRemoveListener(Object listener) {
        if (listener instanceof MockitoListener) {
            Mockito.framework().removeListener((MockitoListener) listener);
        }
    }
}
