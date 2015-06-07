/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.handler;

import java.util.List;

import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.progress.HandyReturnValues;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.invocation.Invocation;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.VoidMethodStubbable;

/**
 * Protects the results from delegate MockHandler. Makes sure the results are valid.
 *
 * by Szczepan Faber, created at: 5/22/12
 */
@SuppressWarnings({"unchecked", "rawtypes"})
class NullResultGuardian implements InternalMockHandler {

    private static final long serialVersionUID = 3415582812871378410L;
    private final InternalMockHandler delegate;

    public NullResultGuardian(final InternalMockHandler delegate) {
        this.delegate = delegate;
    }

    public Object handle(final Invocation invocation) throws Throwable {
        final Object result = delegate.handle(invocation);
        final Class<?> returnType = invocation.getMethod().getReturnType();
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

    public VoidMethodStubbable voidMethodStubbable(final Object mock) {
        return delegate.voidMethodStubbable(mock);
    }

    public void setAnswersForStubbing(final List answers) {
        delegate.setAnswersForStubbing(answers);
    }

    public InvocationContainer getInvocationContainer() {
        return delegate.getInvocationContainer();
    }
}
