package org.mockito.internal.stubbing;

import org.mockito.internal.invocation.Invocation;

import java.util.List;

//TODO move to different package
public interface InvocationContainer {
    List<Invocation> getInvocations();

    List<StubbedInvocationMatcher> getStubbedInvocations();
}
