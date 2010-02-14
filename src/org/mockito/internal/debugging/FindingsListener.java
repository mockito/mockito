package org.mockito.internal.debugging;

import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

public interface FindingsListener {
    void foundStubCalledWithDifferentArgs(Invocation unused, InvocationMatcher unstubbed);

    void foundUnusedStub(Invocation unused);

    void foundUstubbed(InvocationMatcher unstubbed);
}
