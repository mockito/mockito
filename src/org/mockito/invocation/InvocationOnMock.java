/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.invocation;

import java.lang.reflect.Method;

/**
 * An invocation on a mock
 * <p>
 * A placeholder for mock, the method that was called and the arguments that were passed.
 */
public interface InvocationOnMock {

    /**
     * returns the mock object 
     * 
     * @return mock object
     */
    Object getMock();

    /**
     * returns the method
     * 
     * @return method
     */
    Method getMethod();

    /**
     * returns arguments passed to the method
     * 
     * @return arguments
     */
    Object[] getArguments();
}