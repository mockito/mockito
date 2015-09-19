/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.listeners;

import org.mockito.MockSettings;

/**
 * This listener can be notified of method invocations on a mock.
 * 
 * For this to happen, it must be registered using {@link MockSettings#invocationListeners(InvocationListener...)}.
 */
public interface InvocationListener {
    
    /**
     * Called after the invocation of the listener's mock if it returned normally.
     *
     * <p>
     * Exceptions caused by this invocationListener will raise a {@link org.mockito.exceptions.base.MockitoException}.
     * </p>
     *
     * @param methodInvocationReport Information about the method call that just happened.
     *
     * @see MethodInvocationReport
     */
    void reportInvocation(MethodInvocationReport methodInvocationReport);
}
