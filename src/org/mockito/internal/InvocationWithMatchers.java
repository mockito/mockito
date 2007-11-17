package org.mockito.internal;

import java.util.List;

import org.mockito.internal.matchers.IArgumentMatcher;

public class InvocationWithMatchers extends ExpectedInvocation {

    public InvocationWithMatchers(Invocation invocation, List<IArgumentMatcher> matchers) {
        super(invocation, matchers);
    }
    
    public MockitoInvocation getInvocation() {
        return (MockitoInvocation) this.invocation;
    }
}
