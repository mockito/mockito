/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.mockito.invocation.Invocation;

import java.util.List;

//TODO move to different package
public interface InvocationContainer {
    List<Invocation> getInvocations();

    void clearInvocations();

    List<StubbedInvocationMatcher> getStubbedInvocations();
}
