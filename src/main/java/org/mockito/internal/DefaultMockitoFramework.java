package org.mockito.internal;

import org.mockito.MockitoFramework;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.listeners.StubbingListener;

/**
 * Created by sfaber on 7/22/16.
 */
public class DefaultMockitoFramework implements MockitoFramework {
    public void setStubbingListener(StubbingListener listener) {
        ThreadSafeMockingProgress.mockingProgress().setStubbingListener(listener);
    }
}
