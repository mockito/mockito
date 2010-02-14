package org.mockito.internal.debugging;

import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class WarningsFinder {
    private List<Invocation> unusedStubs;
    private List<InvocationMatcher> allInvocations;

    public WarningsFinder(List<Invocation> unusedStubs, List<InvocationMatcher> allInvocations) {
        this.unusedStubs = new LinkedList(unusedStubs);
        this.allInvocations = new LinkedList(allInvocations);
    }

    public void find(FindingsListener findingsListener) {
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
            findingsListener.foundUstubbed(i);
        }
    }
}
