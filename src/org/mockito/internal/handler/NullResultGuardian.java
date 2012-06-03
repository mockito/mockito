/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.handler;

import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.progress.HandyReturnValues;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.invocation.Invocation;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.VoidMethodStubbable;

import java.util.List;

/**
 * Protects the results from delegate MockHandler. Makes sure the results are valid.
 *
 * by Szczepan Faber, created at: 5/22/12
 */
class NullResultGuardian implements InternalMockHandler {
    private final InternalMockHandler delegate;

    public NullResultGuardian(InternalMockHandler delegate) {
        this.delegate = delegate;
    }

    public Object handle(Invocation invocation) throws Throwable {
        Object result = delegate.handle(invocation);
        Class<?> returnType = invocation.getMethod().getReturnType();
        if(result == null && returnType.isPrimitive()) {
            //primitive values cannot be null
            return new HandyReturnValues().returnFor(returnType);
        } else {
            return result;
        }
    }

    //boring delegation:

    public MockCreationSettings getMockSettings() {
        return delegate.getMockSettings();
    }

    public VoidMethodStubbable voidMethodStubbable(Object mock) {
        return delegate.voidMethodStubbable(mock);
    }

    public void setAnswersForStubbing(List answers) {
        delegate.setAnswersForStubbing(answers);
    }

    public InvocationContainer getInvocationContainer() {
        return delegate.getInvocationContainer();
    }
}
