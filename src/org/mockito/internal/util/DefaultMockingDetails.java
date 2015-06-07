/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import java.util.Collection;
import java.util.Set;

import org.mockito.MockingDetails;
import org.mockito.invocation.Invocation;

/**
 * Class to inspect any object, and identify whether a particular object is either a mock or a spy.  This is
 * a wrapper for {@link org.mockito.internal.util.MockUtil}.
 */
public class DefaultMockingDetails implements MockingDetails {

    private final Object toInspect;
    private final MockUtil delegate;

    public DefaultMockingDetails(final Object toInspect, final MockUtil delegate){
        this.toInspect = toInspect;
        this.delegate = delegate;
    }
    
    public boolean isMock(){
        return delegate.isMock(toInspect);
    }
    
    public boolean isSpy(){
        return delegate.isSpy(toInspect);
    }
    
    public Collection<Invocation> getInvocations() {
        return delegate.getMockHandler(toInspect).getInvocationContainer().getInvocations();
    }

    public Class<?> getMockedType() {
        return delegate.getMockHandler(toInspect).getMockSettings().getTypeToMock();
    }
    
    @SuppressWarnings("unchecked")
    public Set<Class> getExtraInterfaces() {
        return delegate.getMockHandler(toInspect).getMockSettings().getExtraInterfaces();
    }
}

