/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unchecked")
public class WarningsFinder {
    private final List<Invocation> baseUnusedStubs;
    private final List<InvocationMatcher> baseAllInvocations;

    public WarningsFinder(List<Invocation> unusedStubs, List<InvocationMatcher> allInvocations) {
        this.baseUnusedStubs = unusedStubs;
        this.baseAllInvocations = allInvocations;
    }
    
    public void find(FindingsListener findingsListener) {
        List<Invocation> unusedStubs = new LinkedList(this.baseUnusedStubs);
        List<InvocationMatcher> allInvocations = new LinkedList(this.baseAllInvocations);

        Iterator<Invocation> unusedIterator = unusedStubs.iterator();
        while(unusedIterator.hasNext()) {
            Invocation unused = unusedIterator.next();
            Iterator<InvocationMatcher> unstubbedIterator = allInvocations.iterator();
            while(unstubbedIterator.hasNext()) {
                InvocationMatcher unstubbed = unstubbedIterator.next();
                if(unstubbed.hasSimilarMethod(unused)) {
                    findingsListener.foundStubCalledWithDifferentArgs(unused, unstubbed);
                    unusedIterator.remove();
                    unstubbedIterator.remove();
                }
            }
        }

        for (Invocation i : unusedStubs) {
            findingsListener.foundUnusedStub(i);
        }

        for (InvocationMatcher i : allInvocations) {
            findingsListener.foundUnstubbed(i);
        }
    }
}
