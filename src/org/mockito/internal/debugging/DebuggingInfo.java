package org.mockito.internal.debugging;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.util.MockitoLogger;

public class DebuggingInfo {

    //TODO Thread safety issue?
    private final List<Invocation> stubbedInvocations = new LinkedList<Invocation>();
    private final List<InvocationMatcher> potentiallyUnstubbedInvocations = new LinkedList<InvocationMatcher>();
    //TODO this code is crap. Use different field to maintain unusedInvocations
    private final List<InvocationMatcher> unusedInvocations = new LinkedList<InvocationMatcher>();
    private boolean collectingData;

    public void addStubbedInvocation(Invocation invocation) {
        if (!collectingData) {
            return;
        }
        
        //TODO test 
        //this is required because we don't know if unstubbedInvocation was really stubbed later...
        Iterator<InvocationMatcher> unstubbedIterator = potentiallyUnstubbedInvocations.iterator();
        while(unstubbedIterator.hasNext()) {
            InvocationMatcher unstubbed = unstubbedIterator.next();
            if (unstubbed.getInvocation().equals(invocation)) {
                unstubbedIterator.remove();
            }
        }

        stubbedInvocations.add(invocation);
    }

    public void addPotentiallyUnstubbed(InvocationMatcher invocationMatcher) {
        if (!collectingData) {
            return;
        }
        potentiallyUnstubbedInvocations.add(invocationMatcher);
    }

    public void collectData() {
        collectingData = true;
    }

    public void clearData() {
        collectingData = false;
        potentiallyUnstubbedInvocations.clear();
        stubbedInvocations.clear();
    }

    public void printWarnings(MockitoLogger logger) {
        if (hasData()) {
            WarningsPrinter warningsPrinter = new WarningsPrinter(stubbedInvocations, potentiallyUnstubbedInvocations);
            warningsPrinter.print(logger);
        }
    }

    public boolean hasData() {
        return !stubbedInvocations.isEmpty() || !potentiallyUnstubbedInvocations.isEmpty();
    }
    
    @Override
    public String toString() {
        return "unusedInvocations: " + stubbedInvocations + "\nunstubbed invocations:" + potentiallyUnstubbedInvocations;
    }
}