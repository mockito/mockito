package org.mockito.internal.framework;

import org.mockito.MockitoFramework;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.listeners.MockCreationListener;
import org.mockito.listeners.MockitoListener;
import org.mockito.listeners.StubbingListener;

public class DefaultMockitoFramework implements MockitoFramework {

    //TODO SF!
    public void setStubbingListener(StubbingListener listener) {
        MockingProgress p = ThreadSafeMockingProgress.mockingProgress();
        if (listener == null) {
            p.setStubbingListener(null);
        } else {
            p.setStubbingListener(new ThreadSafeStubbingListener(listener));
        }
    }

    public void addListener(MockitoListener listener) {
        if (listener instanceof MockCreationListener) {
            ThreadSafeMockingProgress.mockingProgress().addListener(listener);
        }
    }

    public void removeListener(MockitoListener listener) {
        ThreadSafeMockingProgress.mockingProgress().removeListener(listener);
    }
}