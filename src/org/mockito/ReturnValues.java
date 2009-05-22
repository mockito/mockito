/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * @deprecated
 * <b>Just use {@link Answer} interface</b>
 * <p>
 * The reason why it is deprecated is that ReturnValues is being replaced by Answer
 * for better consistency & interoperability of the framework. 
 * Answer interface has been in Mockito for a while and it's the same as ReturnValues.
 * <p>
 * Configures return values for an unstubbed invocation
 * <p>
 * Can be used in {@link Mockito#mock(Class, ReturnValues)}
 */
@Deprecated
public interface ReturnValues {

    /**
     * return value for an unstubbed invocation
     * 
     * @param invocation placeholder for mock and a method
     * @return the return value
     */
    Object valueFor(InvocationOnMock invocation) throws Throwable;
}