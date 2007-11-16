package org.mockito;

import java.util.*;

import org.easymock.internal.*;
import org.mockito.exceptions.*;

public class MockitoBehavior {

    private List<InvocationWithMatchers> registeredInvocations = new LinkedList<InvocationWithMatchers>();
    private Map<InvocationWithMatchers, Result> results = new HashMap<InvocationWithMatchers, Result>();
    
    public void addInvocation(InvocationWithMatchers invocation) {
        this.registeredInvocations.add(invocation);
    }

    public void addResult(Result result) {
        this.results.put(registeredInvocations.remove(registeredInvocations.size()-1), result);
    }

    public void verify(InvocationWithMatchers invocation, VerifyingMode verifyingMode) {
        int actuallyInvoked = numberOfActualInvocations(invocation);
        
        if (verifyingMode.numberOfInvocationsMatters()) {
            int expectedInvoked = verifyingMode.getExactNumberOfInvocations();
            
            if (actuallyInvoked != expectedInvoked) {
                throw new NumberOfInvocationsAssertionError(expectedInvoked, actuallyInvoked);
            } 
        } else {
            if (actuallyInvoked == 0) {
                throw new MockVerificationAssertionError();
            }
        }
    }

    private int numberOfActualInvocations(InvocationWithMatchers expectedInvocation) {
        int verifiedInvocations = 0;
        for (InvocationWithMatchers registeredInvocation : registeredInvocations) {
            MockitoInvocation invocation = registeredInvocation.getInvocation();
            if (expectedInvocation.matches(invocation)) {
                verifiedInvocations += 1;
                invocation.markVerified();
            } else {
                verifiedInvocations += 0;
            }
        }

        return verifiedInvocations;
    }

    public void verifyNoMoreInteractions() {
        for (InvocationWithMatchers registeredInvocation : registeredInvocations) {
            if (!registeredInvocation.getInvocation().isVerified()) {
                throw new MockVerificationAssertionError();
            }
        }
    }

    public Object resultFor(MockitoInvocation invocation) throws Throwable {
        for (InvocationWithMatchers invocationWithMatchers : results.keySet()) {
            if (invocationWithMatchers.matches(invocation)) {
                return results.get(invocationWithMatchers).answer();
            }
        }

        return ToTypeMappings.emptyReturnValueFor(invocation.getMethod().getReturnType());
    }

    public MockitoInvocation lastInvocation() {
        return registeredInvocations.get(registeredInvocations.size() - 1).getInvocation();
    }
}
