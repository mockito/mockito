/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import org.mockito.MockingDetails;
import org.mockito.invocation.Invocation;

import java.util.Collection;

/**
 * Class to inspect any object, and identify whether a particular object is either a mock or a spy.  This is
 * a wrapper for {@link org.mockito.internal.util.MockUtil}.
 */
public class DefaultMockingDetails implements MockingDetails {

    private final Object toInspect;
    private final MockUtil delegate;

    public DefaultMockingDetails(Object toInspect, MockUtil delegate){
        this.toInspect = toInspect;
        this.delegate = delegate;
    }
    /**
     * Find out whether the object is a mock.
     * @return true if the object is a mock or a spy.
     */
    public boolean isMock(){
        return delegate.isMock( toInspect );
    }

    /**
     * Find out whether the object is a spy.
     * @return true if the object is a spy.
     */
    public boolean isSpy(){
        return delegate.isSpy( toInspect );
    }
    
    public Collection<Invocation> getInvocations() {
    	return delegate.getMockHandler(toInspect).getInvocationContainer().getInvocations();
    }
}

