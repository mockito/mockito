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
import org.mockito.invocation.Invocation;

@Deprecated
public class WarningsCollector {

    private final List<Object> createdMocks;

    public WarningsCollector() {
        createdMocks = new LinkedList<Object>();
    }

    public String getWarnings() {
        List<Invocation> unused = new UnusedStubsFinder().find(createdMocks);
        List<Invocation> all = AllInvocationsFinder.find(createdMocks);
        List<InvocationMatcher> allInvocationMatchers = InvocationMatcher.createFrom(all);

        return new WarningsPrinterImpl(unused, allInvocationMatchers, false).print();
    }
}
