package org.mockito.internal.framework;

import org.mockito.MockitoFramework;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.listeners.MockCreationListener;
import org.mockito.listeners.MockitoListener;
import org.mockito.listeners.StubbingListener;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class DefaultMockitoFramework implements MockitoFramework {

    //TODO 384 kill!
    public void setStubbingListener(StubbingListener listener) {
        MockingProgress p = ThreadSafeMockingProgress.mockingProgress();
        if (listener == null) {
            p.setStubbingListener(null);
        } else {
            p.setStubbingListener(new ThreadSafeStubbingListener(listener));
        }
    }

    //TODO 384 listeners!
    public void addListener(MockitoListener listener) {
        if (listener instanceof MockCreationListener) {
            ThreadSafeMockingProgress.mockingProgress().setListener(listener);
        }
    }

    public void removeListener(MockitoListener listener) {
        ThreadSafeMockingProgress.mockingProgress().setListener(null);
    }
}