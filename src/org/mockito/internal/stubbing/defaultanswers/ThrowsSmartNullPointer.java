/*
 * Copyright (c) 2014 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.Location;
import org.mockito.stubbing.Answer;

/**
 * A package private class for throwing a smart null pointer exception reporting the unstubbed
 * invocation and the location.
 */
class ThrowsSmartNullPointer implements Answer<Object> {
    private final InvocationOnMock unstubbedInvocation;
    private final Location location;

    public ThrowsSmartNullPointer(InvocationOnMock unstubbedInvocation, Location location) {
        this.unstubbedInvocation = unstubbedInvocation;
        this.location = location;
    }

    public Object answer(InvocationOnMock currentInvocation) throws Throwable {
        if (new ObjectMethodsGuru().isToString(currentInvocation.getMethod())) {
            return "SmartNull returned by this unstubbed method call on a mock:\n" +
                    unstubbedInvocation.toString();
        }

        new Reporter().smartNullPointerException(unstubbedInvocation.toString(), location);
        return null;
    }
}
