package org.mockito;

import java.util.*;

import org.easymock.internal.*;
import org.mockito.exceptions.*;

public class MockitoBehavior {

    private List<MockitoInvocation> registeredInvocations = new LinkedList<MockitoInvocation>();
    private Map<MockitoInvocation, Result> results = new HashMap<MockitoInvocation, Result>();
    
    public void addInvocation(MockitoInvocation invocation) {
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
        for (MockitoInvocation registeredInvocation : registeredInvocations) {
            if (expectedInvocation.matches(registeredInvocation)) {
                verifiedInvocations += 1;
                registeredInvocation.markVerified();
            } else {
                verifiedInvocations += 0;
            }
        }

        return verifiedInvocations;
    }

    public void verifyNoMoreInteractions() {
        for (MockitoInvocation registeredInvocation : registeredInvocations) {
            if (!registeredInvocation.isVerified()) {
                throw new MockVerificationAssertionError();
            }
        }
    }

    public Object resultFor(MockitoInvocation invocation) throws Throwable {
        if (results.get(invocation) == null) {
            return ToTypeMappings.emptyReturnValueFor(invocation.getMethod().getReturnType());
        } else {
            return results.get(invocation).answer();
        }
    }

    public MockitoInvocation lastInvocation() {
        return registeredInvocations.get(registeredInvocations.size() - 1);
    }
}
