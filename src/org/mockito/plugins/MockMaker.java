/*
 * Copyright (c) 2012 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.plugins;

import org.mockito.Incubating;
import org.mockito.invocation.MockitoInvocationHandler;
import org.mockito.mock.MockSettingsInfo;

/**
 * The facility to create mocks.
 *
 * <p>By default, an internal cglib/asm based implementation is used.</p>
 *
 * <p>It is possible to configure your own mock maker so that dynamic proxies are created without cglib/asm.
 * For example, the android users can use a MockMaker that can work with Dalvik virtual machine
 * and hence bring Mockito to android apps developers.</p>
 *
 * <p>Can load available mockito extensions. Currently Mockito only have one extension point the
 * {@link MockMaker}. This extension point allows a user to provide his own bytecode engine to build mocks.</p>
 *
 * <p>Suppose you wrote an extension to create mocks with some <em>Awesome</em> library, in order to tell
 * Mockito to use it you need to put in your classpath
 * <ol style="list-style-type: lower-alpha">
 *     <li>The implementation itself, for example <code>org.awesome.mockito.AwesomeMockMaker</code>.</li>
 *     <li>A file named <code>org.mockito.plugins.MockMaker</code> in a folder named
 *     <code>mockito-extensions</code>, the content of this file need to have <strong>one</strong> line with
 *     the qualified name <code>org.awesome.mockito.AwesomeMockMaker</code>.</li>
 * </ol></p>
 *
 * @see org.mockito.mock.MockSettingsInfo
 * @see org.mockito.invocation.MockitoInvocationHandler
 * @see org.mockito.internal.configuration.ClassPathLoader
 */
@Incubating
public interface MockMaker {

    /**
     * Returns a new instance of {@code typeToMock} that implements the
     * interfaces of {@code extraInterfaces}. Invocations to the methods of the
     * returned instance will be delegated to {@code handler}.
     *
     * @param typeToMock The type to imposterize, could be a <strong>class</strong> or an <strong>interface</strong>.
     * @param extraInterfaces Interfaces the mock should implements as well, never <code>null</code>.
     * @param handler Handler of every invocation on the mock.
     * @param settings Mock creation settings.
     * @param <T> Type of the mock to return, actually the <code>typeToMock</code>.
     * @return The mock instance.
     */
    <T> T createMock(
            Class<T> typeToMock,
            Class<?>[] extraInterfaces,
            MockitoInvocationHandler handler,
            MockSettingsInfo settings
    );

    /**
     * Returns the handler for the {@code mock}, or null if {@code mock} was not
     * a mock object created by {@link #createMock(Class, Class[], MockitoInvocationHandler, MockSettingsInfo)}.
     *
     * @param mock The mock instance.
     * @return The invocation handler if this object is a mock, otherwise <code>null</code>.
     */
    MockitoInvocationHandler getHandler(Object mock);

    /**
     * Replaces the existing handler on {@code mock} with {@code newHandler}.
     *
     * <p>The invocation handler actually store invocations to achieve
     * stubbing and verification. In order to reset the mock, we pass
     * a new instance of the invocation handler.</p>
     *
     * @param mock The mock instance whose invocation handler shall be replaced.
     * @param newHandler The new invocation handler instance.
     * @param settings The mock settings.
     */
    void resetMock(
            Object mock,
            MockitoInvocationHandler newHandler,
            MockSettingsInfo settings
    );
}