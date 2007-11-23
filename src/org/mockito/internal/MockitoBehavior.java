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
                //TODO this stuff is really hacked in, refactor, add more testing
                InvocationWithMatchers similarInvocation = findSimilarInvocation(invocation);
                String message = 
                    "\n" +
                    "Failure on verify:" +
                    "\n";
                
                String expected = invocation.toString();
                if (similarInvocation != null) {
                    String actual = similarInvocation.toString();
                    if (expected.equals(actual)) {
                        expected = invocation.toStringWithTypes();
                        actual = similarInvocation.toStringWithTypes();
                    }
                    
                    message += 
                            "Expected: " + expected +
                            "\n" +
                    		"Actual: " + actual;
                } else {
                    message += "Not invoked: " + expected;
                }
                
                throw new VerificationAssertionError(message);
            }
        }
    }

    /**
     * gets first registered invocation with the same method name
     * or just first invocation
     */
    private InvocationWithMatchers findSimilarInvocation(InvocationWithMatchers expectedInvocation) {
        for (InvocationWithMatchers registeredInvocation : registeredInvocations) {
            String expectedMethodName = expectedInvocation.getMethod().getName();
            String registeredInvocationName = registeredInvocation.getMethod().getName();
            if (expectedMethodName.equals(registeredInvocationName)) {
                return registeredInvocation;
            }
        }

        return registeredInvocations.size() > 0 ? registeredInvocations.get(0) : null;
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
                throw new VerificationAssertionError(
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