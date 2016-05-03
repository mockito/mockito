package org.mockito;

import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.listeners.StubbingListener;

/**
 * Created by sfaber on 5/2/16.
 */
@Incubating
public class MockitoFramework {

    void setStubbingListener(StubbingListener listener) {
        new ThreadSafeMockingProgress().setStubbingListener(listener);
    }
}
