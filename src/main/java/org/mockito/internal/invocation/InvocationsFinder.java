/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.invocation;

import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.collections.ListUtil.Filter;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

public class InvocationsFinder {

    private InvocationsFinder() {
    }
    
    public static List<Invocation> findInvocations(List<Invocation> invocations, InvocationMatcher wanted) {
        return ListUtil.filter(invocations, new RemoveNotMatching(wanted));
    }

    public static List<Invocation> findAllMatchingUnverifiedChunks(List<Invocation> invocations, InvocationMatcher wanted, InOrderContext orderingContext) {
        List<Invocation> unverified = removeVerifiedInOrder(invocations, orderingContext);
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
    public static List<Invocation> findMatchingChunk(List<Invocation> invocations, InvocationMatcher wanted, int wantedCount, InOrderContext context) {
        List<Invocation> unverified = removeVerifiedInOrder(invocations, context);
        List<Invocation> firstChunk = getFirstMatchingChunk(wanted, unverified);
        
        if (wantedCount != firstChunk.size()) {
            return findAllMatchingUnverifiedChunks(invocations, wanted, context);
        } else {
            return firstChunk;
        }
    }

    private static List<Invocation> getFirstMatchingChunk(InvocationMatcher wanted, List<Invocation> unverified) {
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
    
    public static Invocation findFirstMatchingUnverifiedInvocation( List<Invocation> invocations, InvocationMatcher wanted, InOrderContext context ){
        for( Invocation invocation : removeVerifiedInOrder( invocations, context )){
            if( wanted.matches( invocation )){
                return invocation;
            }
        }
        return null;
    }
    
    public static Invocation findSimilarInvocation(List<Invocation> invocations, InvocationMatcher wanted) {
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
    
    public static Invocation findFirstUnverified(List<Invocation> invocations) {
        return findFirstUnverified(invocations, null);
    }
    
    static Invocation findFirstUnverified(List<Invocation> invocations, Object mock) {
        for (Invocation i : invocations) {
            boolean mockIsValid = mock == null || mock == i.getMock();
            if (!i.isVerified() && mockIsValid) {
                return i;
            }
        }
        return null;
    }

    public static Location getLastLocation(List<Invocation> invocations) {
        if (invocations.isEmpty()) {
            return null;
        } else {
            Invocation last = invocations.get(invocations.size() - 1);
            return last.getLocation();
        }
    }
    
    public static Invocation findPreviousVerifiedInOrder(List<Invocation> invocations, InOrderContext context) {
        LinkedList<Invocation> verifiedOnly = ListUtil.filter(invocations, new RemoveUnverifiedInOrder(context));
        
        if (verifiedOnly.isEmpty()) {
            return null;
        } else {
            return verifiedOnly.getLast();
        }
    }
    
    private static List<Invocation> removeVerifiedInOrder(List<Invocation> invocations, InOrderContext orderingContext) {
        List<Invocation> unverified = new LinkedList<Invocation>();
        for (Invocation i : invocations) {
            if (orderingContext.isVerified(i)) {
                unverified.clear();
            } else {
                unverified.add(i);
            }
        }
        return unverified;
    }
    
    private static class RemoveNotMatching implements Filter<Invocation> {
        private final InvocationMatcher wanted;

        private RemoveNotMatching(InvocationMatcher wanted) {
            this.wanted = wanted;
        }

        public boolean isOut(Invocation invocation) {
            return !wanted.matches(invocation);
        }
    }

    private static class RemoveUnverifiedInOrder implements Filter<Invocation> {
        private final InOrderContext orderingContext;

        public RemoveUnverifiedInOrder(InOrderContext orderingContext) {
            this.orderingContext = orderingContext;
        }

        public boolean isOut(Invocation invocation) {
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
    public static Invocation findFirstUnverifiedInOrder(InOrderContext context, List<Invocation> orderedInvocations) {
        Invocation candidate = null;
        for(Invocation i : orderedInvocations) {
            if (!context.isVerified(i)) {
                candidate = candidate != null ? candidate : i;
            } else {
                candidate = null;
            }
        }
        return candidate;
    }
}