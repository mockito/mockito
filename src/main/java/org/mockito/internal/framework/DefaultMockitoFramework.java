package org.mockito.internal.framework;

import org.mockito.MockitoFramework;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.listeners.StubbingListener;

public class DefaultMockitoFramework implements MockitoFramework {
    public void setStubbingListener(StubbingListener listener) {
        ThreadSafeMockingProgress.mockingProgress().setStubbingListener(new ThreadSafeStubbingListener(listener));
    }
}
