/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import java.util.List;

import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.util.MockitoLogger;

public class WarningsPrinterImpl {

    private final boolean warnAboutUnstubbed;
    private WarningsFinder finder;

    public WarningsPrinterImpl(List<Invocation> unusedStubs, List<InvocationMatcher> unstubbedInvocations) {
        this(unusedStubs, unstubbedInvocations, false);
    }

    public WarningsPrinterImpl(List<Invocation> unusedStubs, List<InvocationMatcher> allInvocations, boolean warnAboutUnstubbed) {
        this(warnAboutUnstubbed, new WarningsFinder(unusedStubs, allInvocations));
    }

    WarningsPrinterImpl(boolean warnAboutUnstubbed, WarningsFinder finder) {
        this.warnAboutUnstubbed = warnAboutUnstubbed;
        this.finder = finder;
    }
    
    public void print(final MockitoLogger logger) {
        finder.find(new LoggingListener(warnAboutUnstubbed, logger));
    }

    public String print() {
        //TODO: test and figure out if it is the best place for it
        final StringBuilder sb = new StringBuilder();
        this.print(new MockitoLogger() {
            public void log(Object what) {
                sb.append(what);
            }
        });
        return sb.toString();
    }
}