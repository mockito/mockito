/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.framework;

import org.mockito.MockitoFramework;
import org.mockito.listeners.MockitoListener;

import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

public class DefaultMockitoFramework implements MockitoFramework {

    public void addListener(MockitoListener listener) {
        assertNotNull(listener);
        mockingProgress().addListener(listener);
    }

    public void removeListener(MockitoListener listener) {
        assertNotNull(listener);
        mockingProgress().removeListener(listener);
    }

    private void assertNotNull(MockitoListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener cannot be null");
        }
    }
}