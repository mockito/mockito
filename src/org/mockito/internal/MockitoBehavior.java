/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.util.*;

import org.mockito.exceptions.*;

public class MockitoBehavior<T> {

    private T mock;
    
    private List<Invocation> registeredInvocations = new LinkedList<Invocation>();
    private Map<InvocationWithMatchers, Result> results = new HashMap<InvocationWithMatchers, Result>();

    private InvocationWithMatchers invocationForStubbing;
    
    public void addInvocation(InvocationWithMatchers invocation) {
        this.registeredInvocations.add(invocation.getInvocation());
        this.invocationForStubbing = invocation;
    }

    public void addResult(Result result) {
        assert invocationForStubbing != null;
        registeredInvocations.remove(invocationForStubbing.getInvocation());
        this.results.put(invocationForStubbing, result);
    }

    public void verify(InvocationWithMatchers expected, VerifyingMode verifyingMode) {
        checkForMissingInvocation(expected, verifyingMode);
        checkOrderOfInvocations(expected, verifyingMode);
        checkForWrongNumberOfInvocations(expected, verifyingMode);        
        markInvocationsAsVerified(expected, verifyingMode);
    }
    
    void markInvocationsAsVerified(InvocationWithMatchers expected, VerifyingMode verifyingMode) {
        int verifiedSoFar = 0;        
        for (Invocation invocation : registeredInvocations) {
            boolean shouldMarkAsVerified = 
                verifyingMode.atLeastOnceMode() || verifyingMode.getExpectedNumberOfInvocations() >= verifiedSoFar;
            if (expected.matches(invocation) && shouldMarkAsVerified) {
                invocation.markVerified();
                verifiedSoFar++;
            }
        }
    }

    private void checkForMissingInvocation(InvocationWithMatchers expected, VerifyingMode verifyingMode) {
        int actuallyInvoked = numberOfActualInvocations(expected);
        Integer expectedInvoked = verifyingMode.getExpectedNumberOfInvocations();
        boolean atLeastOnce = verifyingMode.atLeastOnceMode();
               
        if ((atLeastOnce || expectedInvoked == 1) && actuallyInvoked == 0) {
            reportMissingInvocationError(expected);
        }
    }

    private void checkForWrongNumberOfInvocations(InvocationWithMatchers expected, VerifyingMode verifyingMode) throws NumberOfInvocationsAssertionError {
        int actuallyInvoked = numberOfActualInvocations(expected);
        Integer expectedInvoked = verifyingMode.getExpectedNumberOfInvocations();
        boolean atLeastOnce = verifyingMode.atLeastOnceMode();
        
        if (!atLeastOnce && actuallyInvoked != expectedInvoked) {
            throw new NumberOfInvocationsAssertionError(expectedInvoked, actuallyInvoked, expected);
        }
    }

    private void reportMissingInvocationError(InvocationWithMatchers invocation) throws VerificationAssertionError {
        //TODO refactor message building somewhere else...
        Invocation similarInvocation = findSimilarInvocation(invocation);
        String message = 
            "\n" +
            "Invocation differs from actual" +
            "\n";
        
        String expected = invocation.toString();
        if (similarInvocation != null) {
            String actual = similarInvocation.toString();
            if (expected.equals(actual)) {
                expected = invocation.getInvocation().toStringWithArgumentTypes();
                actual = similarInvocation.toStringWithArgumentTypes();
            }
            
            message += 
                    "Expected: " + expected +
                    "\n" +
            		"Actual:   " + actual;
        } else {
            message = 
                    "\n" +
                    "Expected but not invoked:" +
                    "\n" +    
                    expected;
        }
        
        throw new VerificationAssertionError(message);
    }

    private void checkOrderOfInvocations(InvocationWithMatchers expected, VerifyingMode verifyingMode) {
        if (!verifyingMode.orderOfInvocationsMatters()) {
            return;
        }
        
        Map<Invocation, Integer> sequenceOfInvocations = getSequenceOfInvocations(verifyingMode);
        Invocation firstUnverifiedInvocation = null;
        for (Invocation registered : sequenceOfInvocations.keySet()) {
            if (!registered.isVerified()) {
                firstUnverifiedInvocation = registered;
            } else {
                break;
            }
        }
        //TODO cover this scenario firstUnverified == null
        assert firstUnverifiedInvocation != null;
        
        if (!expected.matches(firstUnverifiedInvocation)) {
            throw new StrictVerificationError();
        }
    }

    private Map<Invocation, Integer> getSequenceOfInvocations(VerifyingMode verifyingMode) {
        Set<Invocation> allInvocationsInOrder = new TreeSet<Invocation>(
                new Comparator<Invocation>(){
                    public int compare(Invocation o1, Invocation o2) {
                        int comparison = o1.getSequenceNumber().compareTo(o2.getSequenceNumber());
                        assert comparison != 0;
                        return comparison;
                    }});
        
        List<Object> allMocksToBeVerifiedInOrder = verifyingMode.getAllMocksToBeVerifiedInSequence();
        for (Object mock : allMocksToBeVerifiedInOrder) {
            List<Invocation> invocations = MockUtil.getControl(mock).getRegisteredInvocations();
            allInvocationsInOrder.addAll(invocations);
        }
        
        Map<Invocation, Integer> sequenceOfInvocations = new LinkedHashMap<Invocation, Integer>();
        for (Invocation i : allInvocationsInOrder) {
            if (sequenceOfInvocations.containsKey(i)) {
                int currentCount = sequenceOfInvocations.get(i).intValue();
                sequenceOfInvocations.put(i, currentCount + 1);
            } else {
                sequenceOfInvocations.put(i, 1);
            }
        }
        
        return sequenceOfInvocations;
    }

    /**
     * gets first registered invocation with the same method name
     * or just first invocation
     */
    private Invocation findSimilarInvocation(InvocationWithMatchers expectedInvocation) {
        for (Invocation registeredInvocation : registeredInvocations) {
            String expectedMethodName = expectedInvocation.getMethod().getName();
            String registeredInvocationName = registeredInvocation.getMethod().getName();
            if (expectedMethodName.equals(registeredInvocationName) && !registeredInvocation.isVerified()) {
                return registeredInvocation;
            }
        }

        return null;
    }

    private int numberOfActualInvocations(InvocationWithMatchers expectedInvocation) {
        int verifiedInvocations = 0;
        for (Invocation registeredInvocation : registeredInvocations) {
            if (expectedInvocation.matches(registeredInvocation)) {
                verifiedInvocations++;
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
        for (Invocation registeredInvocation : registeredInvocations) {
            if (!registeredInvocation.isVerified()) {
                String mockName = Namer.nameForMock(mock);
                throw new VerificationAssertionError(
                        "\n" +
                        verificationErrorMessage + " on " + mockName +
                        "\n" +
                        "Unexpected: " + registeredInvocation.toString());
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

    public T getMock() {
        return mock;
    }

    public void setMock(T mock) {
        this.mock = mock;
    }

    public List<Invocation> getRegisteredInvocations() {
        return registeredInvocations;
    }

    public InvocationWithMatchers getInvocationForStubbing() {
        return invocationForStubbing;
    }
}