package org.mockito;

import org.mockito.listeners.StubbingListener;

/**
 * Mockito framework settings and lifecycle listeners, for advanced users or for integrating with other frameworks.
 *
 * @since 2.*
 */
@Incubating
public interface MockitoFramework {

    /**
     * Sets new {@link StubbingListener}.
     * <code>null</code> is permitted and removes any previously set listener.
     * If you set the stubbing listener, ensure that you have cleared it afterwards.
     * See examples in Mockito codebase how {@link #setStubbingListener(StubbingListener)} is used.
     */
    @Incubating
    void setStubbingListener(StubbingListener listener);
}
