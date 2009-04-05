/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.util.MockitoLogger;

public class DebuggingInfo {

    private final List<Invocation> unusedStubs = new LinkedList<Invocation>();
    private final List<InvocationMatcher> unstubbedInvocations = new LinkedList<InvocationMatcher>();

    private boolean collectingData;

    public void addStubbedInvocation(Invocation invocation) {
        if (!collectingData) {
            return;
        }
        
        Iterator<InvocationMatcher> unstubbedIterator = unstubbedInvocations.iterator();
        while(unstubbedIterator.hasNext()) {
            InvocationMatcher unstubbed = unstubbedIterator.next();
            if (unstubbed.getInvocation().equals(invocation)) {
                unstubbedIterator.remove();
            }
        }
        
        unusedStubs.add(invocation);
    }

    public void addPotentiallyUnstubbed(InvocationMatcher invocationMatcher) {
        if (!collectingData) {
            return;
        }
        unstubbedInvocations.add(invocationMatcher);
    }
    
    public void reportUsedStub(InvocationMatcher invocationMatcher) {
        Iterator<Invocation> i = unusedStubs.iterator();
        while(i.hasNext()) {
            Invocation invocation = i.next();
            if (invocationMatcher.matches(invocation)) {
                i.remove();
            }
        }
    }

    public void collectData() {
        collectingData = true;
    }

    public void clearData() {
        collectingData = false;
        unstubbedInvocations.clear();
        unusedStubs.clear();
    }

    public void printWarnings(MockitoLogger logger) {
        if (hasData()) {
            WarningsPrinter warningsPrinter = new WarningsPrinter(unusedStubs, unstubbedInvocations);
            warningsPrinter.print(logger);
        }
    }

    public boolean hasData() {
        return !unusedStubs.isEmpty() || !unstubbedInvocations.isEmpty();
    }
}