/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification.checkers;

import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.invocation.InvocationImpl;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsFinder;
import org.mockito.internal.verification.api.InOrderContext;

class InvocationsFinderStub extends InvocationsFinder {
    
    InvocationImpl similarToReturn;
    InvocationImpl firstUnverifiedToReturn;
    InvocationImpl previousInOrderToReturn;
    
    final List<InvocationImpl> actualToReturn = new LinkedList<InvocationImpl>();
    final List<InvocationImpl> validMatchingChunkToReturn = new LinkedList<InvocationImpl>();
    final List<InvocationImpl> allMatchingUnverifiedChunksToReturn = new LinkedList<InvocationImpl>();
    
    List<InvocationImpl> invocations;
    
    @Override
    public List<InvocationImpl> findAllMatchingUnverifiedChunks(List<InvocationImpl> invocations, InvocationMatcher wanted, InOrderContext context) {
        return allMatchingUnverifiedChunksToReturn;
    }
    
    @Override
    public List<InvocationImpl> findMatchingChunk(List<InvocationImpl> invocations, InvocationMatcher wanted, int wantedCount, InOrderContext context) {
        return validMatchingChunkToReturn;
    }

    @Override public List<InvocationImpl> findInvocations(List<InvocationImpl> invocations, InvocationMatcher wanted) {
        this.invocations = invocations;
        return actualToReturn;
    }
    
    @Override public InvocationImpl findSimilarInvocation(List<InvocationImpl> invocations, InvocationMatcher wanted) {
        this.invocations = invocations;
        return similarToReturn;
    }
    
    @Override public InvocationImpl findFirstUnverified(List<InvocationImpl> invocations) {
        this.invocations = invocations;
        return firstUnverifiedToReturn;
    }
    
    @Override
    public InvocationImpl findPreviousVerifiedInOrder(List<InvocationImpl> invocations, InOrderContext context) {
        return previousInOrderToReturn;
    }
}