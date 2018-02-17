/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.plugins;

import org.mockito.internal.creation.instance.Instantiator;
import org.mockito.mock.MockCreationSettings;

/**
 * @deprecated since 2.15.4 because this internal class was leaking from the public API.
 * For more information why deprecated, see {@link org.mockito.plugins.InstantiatorProvider2} and
 * <a href="https://github.com/mockito/mockito/issues/1303">Issue 1303</a>
 *
 * <p>
 *     Mockito will invoke this interface in order to fetch an instance instantiator provider.
 * </p>
 *
 * <p>
 *     By default, an internal byte-buddy/asm/objenesis based implementation is used.
 * </p>
 *
 * <h3>Using the extension point</h3>
 *
 * <p>
 *     The plugin mechanism of mockito works in a similar way as the
 *     {@link java.util.ServiceLoader}, however instead of looking in the <code>META-INF</code>
 *     directory, Mockito will look in <code>mockito-extensions</code> directory.
 *     <em>The reason for that is that Android SDK strips jar from the <code>META-INF</code>
 *     directory when creating an APK.</em>
 * </p>
 *
 * <ol style="list-style-type: lower-alpha">
 *     <li>The implementation itself, for example
 *         <code>org.awesome.mockito.AwesomeInstantiatorProvider</code> that implements the
 *         <code>InstantiatorProvider</code>.</li>
 *     <li>A file "<code>mockito-extensions/org.mockito.plugins.InstantiatorProvider</code>".
 *         The content of this file is exactly a <strong>one</strong> line with the qualified
 *         name: <code>org.awesome.mockito.AwesomeInstantiatorProvider</code>.</li>
 * </ol></p>
 *
 * <p>
 *     Note that if several <code>mockito-extensions/org.mockito.plugins.InstantiatorProvider</code>
 *     files exists in the classpath, Mockito will only use the first returned by the standard
 *     {@link ClassLoader#getResource} mechanism.
 * <p>
 *     So just create a custom implementation of {@link InstantiatorProvider} and place the
 *     qualified name in the following file
 *     <code>mockito-extensions/org.mockito.plugins.InstantiatorProvider</code>.
 * </p>
 * <p>
 *     This class is deprecated and was replaced by
 *     {@link org.mockito.plugins.InstantiatorProvider2}. Hence if there is both a
 *     <code>mockito-extensions/org.mockito.plugins.InstantiatorProvider</code> and
 *     <code>mockito-extensions/org.mockito.plugins.InstantiatorProvider2</code> the second one
 *     takes preference.
 * </p>
 *
 * @since 2.0.31
 */
@Deprecated
public interface InstantiatorProvider {

    /**
     * @deprecated, see {@link InstantiatorProvider}.
     *
     * Returns an instantiator, used to create new class instances.
     */
    @Deprecated
    Instantiator getInstantiator(MockCreationSettings<?> settings);
}
