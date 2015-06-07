/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.UnusedStubsFinder;
import org.mockito.internal.invocation.finder.AllInvocationsFinder;
import org.mockito.internal.listeners.CollectCreatedMocks;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.invocation.Invocation;

public class WarningsCollector {
   
    List createdMocks;

    public WarningsCollector() {
        createdMocks = new LinkedList();
        final MockingProgress progress = new ThreadSafeMockingProgress();
        progress.setListener(new CollectCreatedMocks(createdMocks));
    }

    public String getWarnings() {
        final List<Invocation> unused = new UnusedStubsFinder().find(createdMocks);
        final List<Invocation> all = new AllInvocationsFinder().find(createdMocks);
        final List<InvocationMatcher> allInvocationMatchers = InvocationMatcher.createFrom(all);

        final String warnings = new WarningsPrinterImpl(unused, allInvocationMatchers, false).print();

        return warnings;
    }
}