/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import java.util.List;

import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.util.MockitoLogger;
import org.mockito.internal.util.SimpleMockitoLogger;
import org.mockito.invocation.Invocation;

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
        SimpleMockitoLogger logger = new SimpleMockitoLogger();
        this.print(logger);
        return logger.getLoggedInfo();
    }
}