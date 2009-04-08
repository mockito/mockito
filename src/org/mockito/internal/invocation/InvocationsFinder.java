/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.debugging.Location;
import org.mockito.internal.util.ListUtil;
import org.mockito.internal.util.ListUtil.Filter;

public class InvocationsFinder {

    public List<Invocation> findInvocations(List<Invocation> invocations, InvocationMatcher wanted) {
        return ListUtil.filter(invocations, new RemoveNotMatching(wanted));
    }

    public List<Invocation> findAllMatchingUnverifiedChunks(List<Invocation> invocations, InvocationMatcher wanted) {
        List<Invocation> unverified = removeVerifiedInOrder(invocations);
        return ListUtil.filter(unverified, new RemoveNotMatching(wanted));
    }

    /**
     * some examples how it works:
     * 
     * Given invocations sequence:
     * 1,1,2,1
     * 
     * if wanted is 1 and mode is times(2) then returns
     * 1,1  
     * 
     * if wanted is 1 and mode is atLeast() then returns
     * 1,1,1
     * 
     * if wanted is 1 and mode is times(x), where x != 2 then returns
     * 1,1,1
     */
    public List<Invocation> findMatchingChunk(List<Invocation> invocations, InvocationMatcher wanted, int wantedCount) {
        List<Invocation> unverified = removeVerifiedInOrder(invocations);
        List<Invocation> firstChunk = getFirstMatchingChunk(wanted, unverified);
        
        if (wantedCount != firstChunk.size()) {
            return this.findAllMatchingUnverifiedChunks(invocations, wanted);
        } else {
            return firstChunk;
        }
    }

    private List<Invocation> getFirstMatchingChunk(InvocationMatcher wanted, List<Invocation> unverified) {
        List<Invocation> firstChunk = new LinkedList<Invocation>();
        for (Invocation invocation : unverified) {
            if (wanted.matches(invocation)) {
                firstChunk.add(invocation);
            } else if (!firstChunk.isEmpty()) {
                break;
            }
        }
        return firstChunk;
    }
    
    public Invocation findSimilarInvocation(List<Invocation> invocations, InvocationMatcher wanted) {
        Invocation firstSimilar = null;
        for (Invocation invocation : invocations) {
            if (!wanted.hasSimilarMethod(invocation)) {
                continue;
            }
            if (firstSimilar == null) {
                firstSimilar = invocation;
            }
            if (wanted.hasSameMethod(invocation)) {
                return invocation;
            }
        }
        
        return firstSimilar;
    }
    
    public Invocation findFirstUnverified(List<Invocation> invocations) {
        return findFirstUnverified(invocations, null);
    }
    
    Invocation findFirstUnverified(List<Invocation> invocations, Object mock) {
        for (Invocation i : invocations) {
            boolean mockIsValid = mock == null || mock == i.getMock();
            if (!i.isVerified() && mockIsValid) {
                return i;
            }
        }
        return null;
    }

    public Location getLastLocation(List<Invocation> invocations) {
        if (invocations.isEmpty()) {
            return null;
        } else {
            Invocation last = invocations.get(invocations.size() - 1);
            return last.getLocation();
        }
    }
    
    public Invocation findPreviousVerifiedInOrder(List<Invocation> invocations) {
        LinkedList<Invocation> verifiedOnly = ListUtil.filter(invocations, new RemoveUnverifiedInOrder());
        
        if (verifiedOnly.isEmpty()) {
            return null;
        } else {
            return verifiedOnly.getLast();
        }
    }
    
    private List<Invocation> removeVerifiedInOrder(List<Invocation> invocations) {
        List<Invocation> unverified = new LinkedList<Invocation>();
        for (Invocation i : invocations) {
            if (i.isVerifiedInOrder()) {
                unverified.clear();
            } else {
                unverified.add(i);
            }
        }
        return unverified;
    }
    
    private class RemoveNotMatching implements Filter<Invocation> {
        private final InvocationMatcher wanted;

        private RemoveNotMatching(InvocationMatcher wanted) {
            this.wanted = wanted;
        }

        public boolean isOut(Invocation invocation) {
            return !wanted.matches(invocation);
        }
    }

    private class RemoveUnverifiedInOrder implements Filter<Invocation> {
        public boolean isOut(Invocation invocation) {
            return !invocation.isVerifiedInOrder();
        }
    }
}