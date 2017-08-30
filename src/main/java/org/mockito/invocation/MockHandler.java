/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.invocation;

import org.mockito.Incubating;
import org.mockito.MockSettings;
import org.mockito.mock.MockCreationSettings;

import java.io.Serializable;

/**
 * Mockito handler of an invocation on a mock. This is a core part of the API, the heart of Mockito.
 * See also the {@link org.mockito.plugins.MockMaker}.
 * Handler can be used to programmatically simulate invocations on the mock object.
 * <p>
 * Mockito will provide you with the implementation of this interface via {@link org.mockito.plugins.MockMaker} methods:
 * {@link org.mockito.plugins.MockMaker#createMock(MockCreationSettings, MockHandler)}
 * and {@link org.mockito.plugins.MockMaker#resetMock(Object, MockHandler, MockCreationSettings)}.
 * <p>
 * You can provide your own implementation of MockHandler but make sure that the right instance is returned by
 * {@link org.mockito.plugins.MockMaker#getHandler(Object)}.
 */
public interface MockHandler<T> extends Serializable {

    /**
     * Takes an invocation object and handles it.
     * <p>
     * The default implementation provided by Mockito handles invocations by recording
     * method calls on mocks for further verification, captures the stubbing information when mock is stubbed,
     * returns the stubbed values for invocations that have been stubbed, and much more.
     *
     * @param invocation The invocation to handle
     * @return Result
     * @throws Throwable Throwable
     */
    Object handle(Invocation invocation) throws Throwable;

    /**
     * Read-only settings the mock object was created with.
     * See {@link org.mockito.Mockito#mock(Class, MockSettings)}
     *
     * @return read-only settings of the mock
     * @since 2.10.0
     */
    @Incubating
    MockCreationSettings<T> getMockSettings();

    /**
     * Returns the object that holds all invocations on the mock object,
     * including stubbings with declared answers. Do not provide your own implementation.
     * Returned object is an internal implementation, hidden beneath a public marker interface.
     * <p>
     * Please do not provide your own implementation of {@link InvocationContainer} interface at this point.
     * If you have a use case that requires your own implementation of {@link InvocationContainer}
     * please reach out to us. You can open a ticket in our issue tracker to start a discussion.
     *
     * @return container of invocations, stubbings, and answers of the mock.
     *          The container is not part of the public API, please do not cast it or provide custom implementations.
     * @since 2.10.0
     */
    @Incubating
    InvocationContainer getInvocationContainer();
}
