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
 * <p>By default, an internal cglib/asm/objenesis based implementation is used.</p>
 *
 * <p>{@code MockMaker} is an extension point that makes it possible to use custom dynamic proxies
 * and avoid using the default cglib/asm/objenesis implementation.
 * For example, the android users can use a MockMaker that can work with Dalvik virtual machine
 * and hence bring Mockito to android apps developers.</p>
 *
 * <h3>Implementing custom {@code MockMaker}</h3>
 *
 * <ul>
 *     <li>Implement {@link #createMock)}. Do not provide your own implementation of  </li>
 *     <li></li>
 *     <li></li>
 * </ul>
 *
 * <h3>Using the extension point</h3>
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
 * <p>Note that if several <code>mockito-extensions/org.mockito.plugins.MockMaker</code> files exists in the classpath
 * Mockito will only use the first returned by the standard {@link ClassLoader#getResource} mechanism.
 *
 * @see org.mockito.mock.MockSettingsInfo
 * @see org.mockito.invocation.MockitoInvocationHandler
 * @since 1.9.5
 */
@Incubating
public interface MockMaker {

    /**
     * If you want to provide your own implementation of {@code MockMaker} this method should:
     * <ul>
     *     <li>Create a proxy object that implements {@code typeToMock} and potentially also {@code extraInterfaces}.</li>
     *     <li>You may use the information from {@code settings} to configure your proxy object.</li>
     *     <li>Your proxy object should carry the {@code hander} with it. For example, if you generate byte code
     *     to create the proxy you could generate an extra field to keep the {@code hanlder} with the generated object.
     *     Your implementation of {@code MockHandler} is required to provide this instance of {@code handler} when
     *     {@link #getHandler(Object)} is called.
     *     </li>
     * </ul>
     *
     * @param typeToMock The type of the mock, could be a <strong>class</strong> or an <strong>interface</strong>.
     * @param extraInterfaces Interfaces the mock should implements as well,
     *                        never <code>null</code>, interfaces only (no classes).
     * @param handler See {@link MockitoInvocationHandler}.
     *                <b>Do not</b> provide your own implementation at this time. Make sure your implementation of
     *                {@link #getHandler(Object)} will return this instance.
     * @param settings Mock creation settings.
     * @param <T> Type of the mock to return, actually the <code>typeToMock</code>.
     * @return The mock instance.
     * @since 1.9.5
     */
    <T> T createMock(
            Class<T> typeToMock,
            Class<?>[] extraInterfaces,
            MockitoInvocationHandler handler,
            MockSettingsInfo settings
    );

    /**
     * Returns the handler for the {@code mock}. The passed mock object is guaranteed to be a Mockito mock,
     * created by the {@link #createMock} method.
     *
     * @param mock The mock instance.
     * @return should never return null.
     * @since 1.9.5
     */
    MockitoInvocationHandler getHandler(Object mock);

    /**
     * Replaces the existing handler on {@code mock} with {@code newHandler}.
     *
     * <p>The invocation handler actually store invocations to achieve
     * stubbing and verification. In order to reset the mock, we pass
     * a new instance of the invocation handler.</p>
     *
     * <p>Your implementation should make sure the {@code newHandler} is correctly associated to passed {@code mock}</p>
     *
     * @param mock The mock instance whose invocation handler is to be replaced.
     * @param newHandler The new invocation handler instance.
     * @param settings The mock settings - should you need to access some of the mock creation details.
     * @since 1.9.5
     */
    void resetMock(
            Object mock,
            MockitoInvocationHandler newHandler,
            MockSettingsInfo settings
    );
}