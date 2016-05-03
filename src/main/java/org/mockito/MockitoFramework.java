package org.mockito;

import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.listeners.StubbingListener;

import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

/**
 * Created by sfaber on 5/2/16.
 */
@Incubating
public class MockitoFramework {

    void setStubbingListener(StubbingListener listener) {
        mockingProgress().setStubbingListener(listener);
    }
}
