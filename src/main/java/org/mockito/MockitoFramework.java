/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.exceptions.misusing.RedundantListenerException;
import org.mockito.listeners.MockitoListener;

/**
 * Mockito framework settings and lifecycle listeners, for advanced users or for integrating with other frameworks.
 * <p>
 * To get <code>MockitoFramework</code> instance use {@link Mockito#framework()}.
 * <p>
 * For more info on listeners see {@link #addListener(MockitoListener)}.
 *
 * @since 2.1.0
 */
@Incubating
public interface MockitoFramework {

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
     * If you are trying to add the listener but a listener of the same type was already added (and not removed)
     * this method will throw {@link RedundantListenerException}.
     * This is a safeguard to ensure users actually remove the listeners via {@link #removeListener(MockitoListener)}.
     * We do not anticipate the use case where adding the same listener type multiple times is useful.
     * If this safeguard is problematic, please contact us via Mockito issue tracker.
     * <p>
     * For usage examples, see Mockito codebase.
     * If you have ideas and feature requests about Mockito listeners API
     * we are very happy to hear about it via our issue tracker or mailing list.
     *
     * <pre class="code"><code class="java">
     *   Mockito.framework().addListener(myListener);
     * </code></pre>
     *
     * @param listener to add to Mockito
     * @since 2.1.0
     */
    @Incubating
    MockitoFramework addListener(MockitoListener listener) throws RedundantListenerException;

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
     * @since 2.1.0
     */
    @Incubating
    MockitoFramework removeListener(MockitoListener listener);
}
