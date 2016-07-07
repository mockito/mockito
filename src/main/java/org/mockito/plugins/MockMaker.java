/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.plugins;

import org.mockito.Incubating;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;

/**
 * The facility to create mocks.
 *
 * <p>By default, an internal byte-buddy/asm/objenesis based implementation is used.</p>
 *
 * <p>{@code MockMaker} is an extension point that makes it possible to use custom dynamic proxies
 * and avoid using the default byte-buddy/asm/objenesis implementation.
 * For example, the android users can use a MockMaker that can work with Dalvik virtual machine
 * and hence bring Mockito to android apps developers.</p>
 *
 * <h3>Using the extension point</h3>
 *
 * <p>Suppose you wrote an extension to create mocks with some <em>Awesome</em> library, in order to tell
 * Mockito to use it you need to put in your <strong>classpath</strong>:
 * <ol style="list-style-type: lower-alpha">
 *     <li>
 *         The implementation itself, for example <code>org.awesome.mockito.AwesomeMockMaker</code> that
 *         extends the <code>MockMaker</code>.
 *     </li>
 *     <li>
 *         A file "<code>mockito-extensions/org.mockito.plugins.MockMaker</code>". The content of this file is
 *         exactly a <strong>one</strong> line with the qualified name:
 *         <code>org.awesome.mockito.AwesomeMockMaker</code>.
*      </li>
 * </ol>
 * </p>
 *
 * <p>Note that if several <code>mockito-extensions/org.mockito.plugins.MockMaker</code> files exists in the classpath
 * Mockito will only use the first returned by the standard {@link ClassLoader#getResource} mechanism.
 *
 * @see org.mockito.mock.MockCreationSettings
 * @see org.mockito.invocation.MockHandler
 * @since 1.9.5
 */
public interface MockMaker {

    /**
     * If you want to provide your own implementation of {@code MockMaker} this method should:
     * <ul>
     *     <li>Create a proxy object that implements {@code settings.typeToMock} and potentially also {@code settings.extraInterfaces}.</li>
     *     <li>You may use the information from {@code settings} to create/configure your proxy object.</li>
     *     <li>Your proxy object should carry the {@code handler} with it. For example, if you generate byte code
     *     to create the proxy you could generate an extra field to keep the {@code handler} with the generated object.
     *     Your implementation of {@code MockMaker} is required to provide this instance of {@code handler} when
     *     {@link #getHandler(Object)} is called.
     *     </li>
     * </ul>
     *
     * @param settings Mock creation settings like type to mock, extra interfaces and so on.
     * @param handler See {@link org.mockito.invocation.MockHandler}.
     *                <b>Do not</b> provide your own implementation at this time. Make sure your implementation of
     *                {@link #getHandler(Object)} will return this instance.
     * @param <T> Type of the mock to return, actually the <code>settings.getTypeToMock</code>.
     * @return The mock instance.
     * @since 1.9.5
     */
    <T> T createMock(
            MockCreationSettings<T> settings,
            MockHandler handler
    );

    /**
     * Returns the handler for the {@code mock}. <b>Do not</b> provide your own implementations at this time
     * because the work on the {@link MockHandler} api is not completed.
     * Use the instance provided to you by Mockito at {@link #createMock} or {@link #resetMock}.
     *
     * @param mock The mock instance.
     * @return The mock handler, but may return null - it means that there is no handler attached to provided object.
     *   This means the passed object is not really a Mockito mock.
     * @since 1.9.5
     */
    MockHandler getHandler(Object mock);

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
            MockHandler newHandler,
            MockCreationSettings settings
    );

    /**
     * Indicates if the given type can be mocked by this mockmaker.
     *
     * <p>Mockmaker may have different capabilities in term of mocking, typically
     * Mockito 1.x's internal mockmaker cannot mock final types. Other implementations, may
     * have different limitations.</p>
     *
     * @param type The type inspected for mockability.
     * @return object that carries the information about mockability of given type.
     * @since 2.0
     */
    @Incubating
    TypeMockability isTypeMockable(Class<?> type);

    /**
     * Carries the mockability information
     *
     * @since 2.0
     */
    @Incubating
    interface TypeMockability {
        /**
         * informs if type is mockable
         */
        boolean mockable();

        /**
         * informs why type is not mockable
         */
        String nonMockableReason();
    }
}
