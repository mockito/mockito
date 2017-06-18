/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.invocation;

import org.mockito.MockSettings;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.util.List;

/**
 * Mockito handler of an invocation on a mock. This is a core part of the API, the heart of Mockito.
 * See also the {@link org.mockito.plugins.MockMaker}.
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
     */
    MockCreationSettings<T> getMockSettings();

    /**
     * Provided list is a collection of answers declared by the user
     * using doReturn/doAnswer/doThrow/doNothing style of stubbing.
     * See {@link org.mockito.Mockito#doReturn(Object)}.
     *
     * @param answers recorded by user with doReturn/doAnswer/doNothing/doThrow stubbing style
     */
    void setAnswersForStubbing(List<Answer<?>> answers);

    /**
     * Returns the object that holds all invocations on the mock object,
     * including stubbings.
     *
     * @return invocations and stubbings on the mock
     */
    InvocationContainer getInvocationContainer();
}
