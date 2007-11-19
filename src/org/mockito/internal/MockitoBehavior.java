package org.mockito.internal;

import java.util.*;

import org.mockito.exceptions.*;

public class MockitoBehavior<T> {

    private T mock;
    
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
                throw VerificationAssertionError.createNotInvokedError(
                        "\n" +
                        "Not invoked: " + invocation.toString());
            }
        }
    }

    private int numberOfActualInvocations(InvocationWithMatchers expectedInvocation) {
        int verifiedInvocations = 0;
        for (InvocationWithMatchers registeredInvocation : registeredInvocations) {
            Invocation invocation = registeredInvocation.getInvocation();
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
        verifyNoMoreInteractions("No more interactions expected");
    }
    
    public void verifyZeroInteractions() {
        verifyNoMoreInteractions("Zero interactions expected");
    }
    
    private void verifyNoMoreInteractions(String verificationErrorMessage) {
        for (InvocationWithMatchers registeredInvocation : registeredInvocations) {
            if (!registeredInvocation.getInvocation().isVerified()) {
                String mockName = Namer.nameForMock(mock);
                throw VerificationAssertionError.createNoMoreInteractionsError(
                        "\n" +
                        verificationErrorMessage + " on " + mockName + " but found: " + registeredInvocation.toString());
            }
        }
    }

    public Object resultFor(Invocation invocation) throws Throwable {
        for (InvocationWithMatchers invocationWithMatchers : results.keySet()) {
            if (invocationWithMatchers.matches(invocation)) {
                return results.get(invocationWithMatchers).answer();
            }
        }

        return ToTypeMappings.emptyReturnValueFor(invocation.getMethod().getReturnType());
    }

    public Invocation lastInvocation() {
        return registeredInvocations.get(registeredInvocations.size() - 1).getInvocation();
    }

    public T getMock() {
        return mock;
    }

    public void setMock(T mock) {
        this.mock = mock;
    }
}