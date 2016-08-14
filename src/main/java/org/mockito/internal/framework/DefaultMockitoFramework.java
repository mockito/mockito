package org.mockito.internal.framework;

import org.mockito.MockitoFramework;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.listeners.MockitoListener;
import org.mockito.listeners.StubbingListener;

import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

public class DefaultMockitoFramework implements MockitoFramework {

    //TODO 401
    public void setStubbingListener(StubbingListener listener) {
        MockingProgress p = mockingProgress();
        if (listener == null) {
            p.setStubbingListener(null);
        } else {
            p.setStubbingListener(new ThreadSafeStubbingListener(listener));
        }
    }

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