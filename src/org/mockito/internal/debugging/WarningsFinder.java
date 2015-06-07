/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;

@SuppressWarnings({"unchecked", "rawtypes"})
public class WarningsFinder {
    private final List<Invocation> baseUnusedStubs;
    private final List<InvocationMatcher> baseAllInvocations;

    public WarningsFinder(final List<Invocation> unusedStubs, final List<InvocationMatcher> allInvocations) {
        this.baseUnusedStubs = unusedStubs;
        this.baseAllInvocations = allInvocations;
    }
    
    public void find(final FindingsListener findingsListener) {
        final List<Invocation> unusedStubs = new LinkedList(this.baseUnusedStubs);
        final List<InvocationMatcher> allInvocations = new LinkedList(this.baseAllInvocations);

        final Iterator<Invocation> unusedIterator = unusedStubs.iterator();
        while(unusedIterator.hasNext()) {
            final Invocation unused = unusedIterator.next();
            final Iterator<InvocationMatcher> unstubbedIterator = allInvocations.iterator();
            while(unstubbedIterator.hasNext()) {
                final InvocationMatcher unstubbed = unstubbedIterator.next();
                if(unstubbed.hasSimilarMethod(unused)) {
                    findingsListener.foundStubCalledWithDifferentArgs(unused, unstubbed);
                    unusedIterator.remove();
                    unstubbedIterator.remove();
                }
            }
        }

        for (final Invocation i : unusedStubs) {
            findingsListener.foundUnusedStub(i);
        }

        for (final InvocationMatcher i : allInvocations) {
            findingsListener.foundUnstubbed(i);
        }
    }
}
