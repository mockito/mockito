/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.framework;

import org.mockito.MockitoFramework;
import org.mockito.internal.util.Checks;

import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

public class DefaultMockitoFramework implements MockitoFramework {

    @Override
    public MockitoFramework addListener(Object listener) {
        Checks.checkNotNull(listener, "listener");
        mockingProgress().addListener(listener);
        return this;
    }

    @Override
    public MockitoFramework removeListener(Object listener) {
        Checks.checkNotNull(listener, "listener");
        mockingProgress().removeListener(listener);
        return this;
    }
}
