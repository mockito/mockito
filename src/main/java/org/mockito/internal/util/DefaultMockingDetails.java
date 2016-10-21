/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import org.mockito.MockingDetails;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.debugging.InvocationsPrinter;
import org.mockito.stubbing.Stubbing;
import org.mockito.internal.stubbing.StubbingComparator;
import org.mockito.invocation.Invocation;
import org.mockito.mock.MockCreationSettings;

import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import static org.mockito.internal.util.MockUtil.getMockHandler;

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
        return mockHandler().getInvocationContainer().getInvocations();
    }

    @Override
    public MockCreationSettings<?> getMockCreationSettings() {
        return mockHandler().getMockSettings();
    }

    @Override
    public Collection<Stubbing> getStubbings() {
        List<? extends Stubbing> stubbings = mockHandler().getInvocationContainer().getStubbedInvocations();
        TreeSet<Stubbing> out = new TreeSet<Stubbing>(new StubbingComparator());
        out.addAll(stubbings);
        return out;
    }

    @Override
    public String printInvocations() {
        assertGoodMock();
        return new InvocationsPrinter().printInvocations(toInspect);
    }

    private InternalMockHandler<Object> mockHandler() {
        assertGoodMock();
        return getMockHandler(toInspect);
    }

    private void assertGoodMock() {
        if (toInspect == null) {
            throw new NotAMockException("Argument passed to Mockito.mockingDetails() should be a mock, but is null!");
        } else if (!isMock()) {
            throw new NotAMockException("Argument passed to Mockito.mockingDetails() should be a mock, but is an instance of " + toInspect.getClass() + "!");
        }
    }
}

