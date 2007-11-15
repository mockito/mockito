package org.mockito;

import java.util.List;

import org.easymock.IArgumentMatcher;
import org.easymock.internal.*;

public class InvocationWithMatchers extends ExpectedInvocation {

    public InvocationWithMatchers(Invocation invocation, List<IArgumentMatcher> matchers) {
        super(invocation, matchers);
    }
    
    MockitoInvocation getInvocation() {
        return (MockitoInvocation) this.invocation;
    }
}
