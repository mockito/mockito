/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
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

    public void verify(InvocationWithMatchers expected, VerifyingMode verifyingMode) {
        checkForMissingInvocation(expected, verifyingMode);
        checkOrderOfInvocations(expected, verifyingMode);
        checkForWrongNumberOfInvocations(expected, verifyingMode);        
        markInvocationsAsVerified(expected, verifyingMode);
    }
    
    void markInvocationsAsVerified(InvocationWithMatchers expected, VerifyingMode verifyingMode) {
        int verifiedSoFar = 0;        
        for (InvocationWithMatchers registeredInvocation : registeredInvocations) {
            Invocation invocation = registeredInvocation.getInvocation();
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
        InvocationWithMatchers similarInvocation = findSimilarInvocation(invocation);
        String message = 
            "\n" +
            "Invocation differs from actual" +
            "\n";
        
        String expected = invocation.toString();
        if (similarInvocation != null) {
            String actual = similarInvocation.toString();
            if (expected.equals(actual)) {
                expected = invocation.toStringWithArgumentTypes();
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
        
        Map<InvocationWithMatchers, Integer> sequenceOfInvocations = getSequenceOfInvocations(verifyingMode);
        InvocationWithMatchers firstUnverifiedInvocation = null;
        for (InvocationWithMatchers registered : sequenceOfInvocations.keySet()) {
            if (!registered.getInvocation().isVerified()) {
                firstUnverifiedInvocation = registered;
            } else {
                break;
            }
        }
        //TODO cover this scenario firstUnverified == null
        assert firstUnverifiedInvocation != null;
        
        if (!firstUnverifiedInvocation.matches(expected.getInvocation())) {
            throw new StrictVerificationError();
        }
    }

    private Map<InvocationWithMatchers, Integer> getSequenceOfInvocations(VerifyingMode verifyingMode) {
        Set<InvocationWithMatchers> allInvocationsInOrder = new TreeSet<InvocationWithMatchers>(
                new Comparator<InvocationWithMatchers>(){
                    public int compare(InvocationWithMatchers o1, InvocationWithMatchers o2) {
                        int comparison = o1.getSequenceNumber().compareTo(o2.getSequenceNumber());
                        assert comparison != 0;
                        return comparison;
                    }});
        
        List<Object> allMocksToBeVerifiedInOrder = verifyingMode.getAllMocksToBeVerifiedInSequence();
        for (Object mock : allMocksToBeVerifiedInOrder) {
            List<InvocationWithMatchers> invocations = MockUtil.getControl(mock).getRegisteredInvocations();
            allInvocationsInOrder.addAll(invocations);
        }
        
        Map<InvocationWithMatchers, Integer> sequenceOfInvocations = new LinkedHashMap<InvocationWithMatchers, Integer>();
        for (InvocationWithMatchers i : allInvocationsInOrder) {
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
    private InvocationWithMatchers findSimilarInvocation(InvocationWithMatchers expectedInvocation) {
        for (InvocationWithMatchers registeredInvocation : registeredInvocations) {
            String expectedMethodName = expectedInvocation.getMethod().getName();
            String registeredInvocationName = registeredInvocation.getMethod().getName();
            if (expectedMethodName.equals(registeredInvocationName) && !registeredInvocation.getInvocation().isVerified()) {
                return registeredInvocation;
            }
        }

        return null;
    }

    private int numberOfActualInvocations(InvocationWithMatchers expectedInvocation) {
        int verifiedInvocations = 0;
        for (InvocationWithMatchers registeredInvocation : registeredInvocations) {
            Invocation invocation = registeredInvocation.getInvocation();
            if (expectedInvocation.matches(invocation)) {
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
        for (InvocationWithMatchers registeredInvocation : registeredInvocations) {
            if (!registeredInvocation.getInvocation().isVerified()) {
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

    public Invocation lastInvocation() {
        return registeredInvocations.get(registeredInvocations.size() - 1).getInvocation();
    }

    public T getMock() {
        return mock;
    }

    public void setMock(T mock) {
        this.mock = mock;
    }

    public List<InvocationWithMatchers> getRegisteredInvocations() {
        return registeredInvocations;
    }
}