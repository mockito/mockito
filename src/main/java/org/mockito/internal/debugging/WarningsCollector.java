/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

import java.util.LinkedList;
import java.util.List;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.UnusedStubsFinder;
import org.mockito.internal.invocation.finder.AllInvocationsFinder;
import org.mockito.internal.listeners.CollectCreatedMocks;
import org.mockito.invocation.Invocation;

public class WarningsCollector {
   
    private final List<Object> createdMocks;

    public WarningsCollector() {
        createdMocks = new LinkedList<Object>();
        mockingProgress().setListener(new CollectCreatedMocks(createdMocks));
    }

    public String getWarnings() {
        List<Invocation> unused = new UnusedStubsFinder().find(createdMocks);
        List<Invocation> all = AllInvocationsFinder.find(createdMocks);
        List<InvocationMatcher> allInvocationMatchers = InvocationMatcher.createFrom(all);

        return new WarningsPrinterImpl(unused, allInvocationMatchers, false).print();
    }
}