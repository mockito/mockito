/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Optional Answer that adds partial mocking support
 * <p>
 * {@link Answer} can be used to define the return values of unstubbed invocations.
 * <p>
 * This implementation can be helpful when working with legacy code.
 * When this implementation is used, unstubbed methods will delegate to the real implementation.
 * This is a way to create a partial mock object that calls real methods by default.
 * <p>
 */
public class CallsRealMethods implements Answer<Object> {
    public Object answer(InvocationOnMock invocation) throws Throwable {
        return invocation.callRealMethod();
    }
}