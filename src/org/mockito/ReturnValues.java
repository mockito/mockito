/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * @deprecated
 * <b>Instead, please use {@link Answer} interface</b>
 * <p>
 * In rare cases your code might not compile with recent deprecation & changes.
 * Very sorry for inconvenience but it had to be done in order to keep framework consistent.  
 * <p>
 * Why it is deprecated? ReturnValues is being replaced by Answer
 * for better consistency & interoperability of the framework. 
 * Answer interface has been in Mockito for a while and it has the same responsibility as ReturnValues.
 * There's no point in mainting exactly the same interfaces.
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