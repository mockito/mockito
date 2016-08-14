package org.mockito;

import org.mockito.listeners.MockitoListener;
import org.mockito.listeners.StubbingListener;

/**
 * Mockito framework settings and lifecycle listeners, for advanced users or for integrating with other frameworks.
 * <p>
 * To get <code>MockitoFramework</code> instance use {@link Mockito#framework()}.
 * <p>
 * For more info on listeners see {@link #addListener(MockitoListener)}.
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

    /**
     * Adds listener to Mockito.
     * For a list of supported listeners, see the interfaces that extend {@link MockitoListener}.
     * <p>
     * Listeners can be useful for engs that extend Mockito framework.
     * They are used in the implementation of unused stubbings warnings ({@link org.mockito.quality.MockitoHint}).
     * <p>
     * Make sure you remove the listener when the job is complete, see {@link #removeListener(MockitoListener)}.
     * Currently the listeners list is thread local so you need to remove listener from the same thread otherwise
     * remove is ineffectual.
     * In typical scenarios, it is not a problem, because adding & removing listeners typically happens in the same thread.
     * <p>
     * For usage examples, see Mockito codebase.
     * If you have ideas and feature requests about Mockito listeners API
     * we are very happy to hear about it via our issue tracker or mailing list.
     *
     * <pre class="code"><code class="java">
     *   Mockito.framework().addListener(myListener);
     * </code></pre>
     *
     * @param listener to add
     */
    @Incubating
    void addListener(MockitoListener listener);

    /**
     * When you add listener using {@link #addListener(MockitoListener)} make sure to remove it.
     * Currently the listeners list is thread local so you need to remove listener from the same thread otherwise
     * remove is ineffectual.
     * In typical scenarios, it is not a problem, because adding & removing listeners typically happens in the same thread.
     * <p>
     * For usage examples, see Mockito codebase.
     * If you have ideas and feature requests about Mockito listeners API
     * we are very happy to hear about it via our issue tracker or mailing list.
     *
     * @param listener to remove
     */
    @Incubating
    void removeListener(MockitoListener listener);
}
