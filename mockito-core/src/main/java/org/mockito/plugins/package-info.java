/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

/**
 * Mockito plugins allow customization of behavior.
 * For example, it is useful for Android integration via dexmaker.
 * See examples in docs for MockMaker.
 *
 * <p>
 * The plugin mechanism of mockito works in a similar way as the {@link java.util.ServiceLoader}, however instead of
 * looking in the <code>META-INF</code> directory, Mockito will look in <code>mockito-extensions</code> directory.
 * <em>The reason for that is that Android SDK strips jars from the <code>META-INF</code> directory when creating an APK.</em>
 * </p>
 *
 * <p>
 * For example :
 * </p>
 *
 * <ol style="list-style-type: lower-alpha">
 * <li>
 * Create implementation itself, for example <code>org.awesome.mockito.AwesomeMockMaker</code> that extends
 * the <code>MockMaker</code>.
 * </li>
 * <li>
 * A file "<code>mockito-extensions/org.mockito.plugins.MockMaker</code>". The content of this file is exactly
 * a <strong>one</strong> line with the qualified name: <code>org.awesome.mockito.AwesomeMockMaker</code>.
 * </li>
 * </ol>
 *
 * <p>
 * Note that if several <code>mockito-extensions/org.mockito.plugins.MockMaker</code> files exists in the classpath
 * Mockito will only use the first returned by the standard <code>ClassLoader.getResource</code> mechanism.
 * </p>
 */
package org.mockito.plugins;
