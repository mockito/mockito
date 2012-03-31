/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.invocation;

import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.debugging.LocationImpl;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.collections.ListUtil.Filter;
import org.mockito.internal.verification.api.InOrderContext;

public class InvocationsFinder {

    public List<InvocationImpl> findInvocations(List<InvocationImpl> invocations, InvocationMatcher wanted) {
        return ListUtil.filter(invocations, new RemoveNotMatching(wanted));
    }

    public List<InvocationImpl> findAllMatchingUnverifiedChunks(List<InvocationImpl> invocations, InvocationMatcher wanted, InOrderContext orderingContext) {
        List<InvocationImpl> unverified = removeVerifiedInOrder(invocations, orderingContext);
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
    public List<InvocationImpl> findMatchingChunk(List<InvocationImpl> invocations, InvocationMatcher wanted, int wantedCount, InOrderContext context) {
        List<InvocationImpl> unverified = removeVerifiedInOrder(invocations, context);
        List<InvocationImpl> firstChunk = getFirstMatchingChunk(wanted, unverified);
        
        if (wantedCount != firstChunk.size()) {
            return this.findAllMatchingUnverifiedChunks(invocations, wanted, context);
        } else {
            return firstChunk;
        }
    }

    private List<InvocationImpl> getFirstMatchingChunk(InvocationMatcher wanted, List<InvocationImpl> unverified) {
        List<InvocationImpl> firstChunk = new LinkedList<InvocationImpl>();
        for (InvocationImpl invocation : unverified) {
            if (wanted.matches(invocation)) {
                firstChunk.add(invocation);
            } else if (!firstChunk.isEmpty()) {
                break;
            }
        }
        return firstChunk;
    }
    
    public InvocationImpl findFirstMatchingUnverifiedInvocation( List<InvocationImpl> invocations, InvocationMatcher wanted, InOrderContext context ){
        for( InvocationImpl invocation : removeVerifiedInOrder( invocations, context )){
            if( wanted.matches( invocation )){
                return invocation;
            }
        }
        return null;
    }
    
    public InvocationImpl findSimilarInvocation(List<InvocationImpl> invocations, InvocationMatcher wanted) {
        InvocationImpl firstSimilar = null;
        for (InvocationImpl invocation : invocations) {
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
    
    public InvocationImpl findFirstUnverified(List<InvocationImpl> invocations) {
        return findFirstUnverified(invocations, null);
    }
    
    InvocationImpl findFirstUnverified(List<InvocationImpl> invocations, Object mock) {
        for (InvocationImpl i : invocations) {
            boolean mockIsValid = mock == null || mock == i.getMock();
            if (!i.isVerified() && mockIsValid) {
                return i;
            }
        }
        return null;
    }

    public LocationImpl getLastLocation(List<InvocationImpl> invocations) {
        if (invocations.isEmpty()) {
            return null;
        } else {
            InvocationImpl last = invocations.get(invocations.size() - 1);
            return last.getLocation();
        }
    }
    
    public InvocationImpl findPreviousVerifiedInOrder(List<InvocationImpl> invocations, InOrderContext context) {
        LinkedList<InvocationImpl> verifiedOnly = ListUtil.filter(invocations, new RemoveUnverifiedInOrder(context));
        
        if (verifiedOnly.isEmpty()) {
            return null;
        } else {
            return verifiedOnly.getLast();
        }
    }
    
    private List<InvocationImpl> removeVerifiedInOrder(List<InvocationImpl> invocations, InOrderContext orderingContext) {
        List<InvocationImpl> unverified = new LinkedList<InvocationImpl>();
        for (InvocationImpl i : invocations) {
            if (orderingContext.isVerified(i)) {
                unverified.clear();
            } else {
                unverified.add(i);
            }
        }
        return unverified;
    }
    
    private class RemoveNotMatching implements Filter<InvocationImpl> {
        private final InvocationMatcher wanted;

        private RemoveNotMatching(InvocationMatcher wanted) {
            this.wanted = wanted;
        }

        public boolean isOut(InvocationImpl invocation) {
            return !wanted.matches(invocation);
        }
    }

    private class RemoveUnverifiedInOrder implements Filter<InvocationImpl> {
        private final InOrderContext orderingContext;

        public RemoveUnverifiedInOrder(InOrderContext orderingContext) {
            this.orderingContext = orderingContext;
        }

        public boolean isOut(InvocationImpl invocation) {
            return !orderingContext.isVerified(invocation);
        }
    }

    /**
     * i3 is unverified here:
     * 
     * i1, i2, i3
     *     v
     *     
     * all good here:
     * 
     * i1, i2, i3
     *     v   v
     * 
     * @param context
     * @param orderedInvocations
     */
    public InvocationImpl findFirstUnverifiedInOrder(InOrderContext context, List<InvocationImpl> orderedInvocations) {
        InvocationImpl candidate = null;
        for(InvocationImpl i : orderedInvocations) {
            if (!context.isVerified(i)) {
                candidate = candidate != null ? candidate : i;
            } else {
                candidate = null;
            }
        }
        return candidate;
    }
}