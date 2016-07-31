/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import org.mockito.MockingDetails;
import org.mockito.invocation.Invocation;

import static org.mockito.internal.util.MockUtil.getMockHandler;

import java.util.Collection;
import java.util.Set;

/**
 * Class to inspect any object, and identify whether a particular object is either a mock or a spy.  This is
 * a wrapper for {@link org.mockito.internal.util.MockUtil}.
 */
public class DefaultMockingDetails implements MockingDetails {

    private final Object toInspect;

    public DefaultMockingDetails(Object toInspect){
        this.toInspect = toInspect;
    }

    @Override
    public boolean isMock(){
        return MockUtil.isMock(toInspect);
    }

    @Override
    public boolean isSpy(){
        return MockUtil.isSpy(toInspect);
    }

    @Override
    public Collection<Invocation> getInvocations() {
        return getMockHandler(toInspect).getInvocationContainer().getInvocations();
    }

    @Override
    public Class<?> getMockedType() {
        return getMockHandler(toInspect).getMockSettings().getTypeToMock();
    }

    @Override
    public Set<Class<?>> getExtraInterfaces() {
        return getMockHandler(toInspect).getMockSettings().getExtraInterfaces();
    }
}

