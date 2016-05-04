package org.mockito;

import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.listeners.StubbingListener;

/**
 * New incubating API, mainly for other tools that integrate with Mockito.
 *
 * @since 2.0.0
 */
@Incubating
public class MockitoFramework {

    /**
     * Sets new {@link StubbingListener}.
     * <code>null</code> is permitted and removes any previously set listener.
     * If you set the stubbing listener, ensure that you have cleared it afterwards.
     * See examples in Mockito codebase how {@link #setStubbingListener(StubbingListener)} is used.
     *
     * @since 2.0.0
     */
    public static void setStubbingListener(StubbingListener listener) {
        new ThreadSafeMockingProgress().setStubbingListener(listener);
    }
}
