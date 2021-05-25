/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

import org.mockito.listeners.MockitoListener;

public class StateMaster {

    public void reset() {
        mockingProgress().reset();
        mockingProgress().resetOngoingStubbing();
    }

    public void validate() {
        mockingProgress().validateState();
    }

    /**
     * Clears Mockito listeners added by {@link MockitoFramework#addListener(MockitoListener)}
     */
    public void clearMockitoListeners() {
        mockingProgress().clearListeners();
    }
}
