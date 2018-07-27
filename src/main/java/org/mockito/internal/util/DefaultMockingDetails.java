/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import org.mockito.MockingDetails;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.debugging.InvocationsPrinter;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.Stubbing;

import java.util.Collection;

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
        return getInvocationContainer().getInvocations();
    }

    private InvocationContainerImpl getInvocationContainer() {
        assertGoodMock();
        return MockUtil.getInvocationContainer(toInspect);
    }

    @Override
    public MockCreationSettings<?> getMockCreationSettings() {
        return mockHandler().getMockSettings();
    }

    @Override
    public Collection<Stubbing> getStubbings() {
        return getInvocationContainer().getStubbingsAscending();
    }

    @Override
    public String printInvocations() {
        assertGoodMock();
        return new InvocationsPrinter().printInvocations(toInspect);
    }

    @Override
    public MockHandler getMockHandler() {
        return mockHandler();
    }

    @Override
    public Object getMock() {
        return toInspect;
    }

    private MockHandler<Object> mockHandler() {
        assertGoodMock();
        return MockUtil.getMockHandler(toInspect);
    }

    private void assertGoodMock() {
        if (toInspect == null) {
            throw new NotAMockException("Argument passed to Mockito.mockingDetails() should be a mock, but is null!");
        } else if (!isMock()) {
            throw new NotAMockException("Argument passed to Mockito.mockingDetails() should be a mock, but is an instance of " + toInspect.getClass() + "!");
        }
    }
}

