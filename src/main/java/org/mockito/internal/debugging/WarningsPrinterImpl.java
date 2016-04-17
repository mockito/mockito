/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;

import java.util.List;

public class WarningsPrinterImpl {

    private final boolean warnAboutUnstubbed;
    private final WarningsFinder finder;

    public WarningsPrinterImpl(List<Invocation> unusedStubs, List<InvocationMatcher> allInvocations, boolean warnAboutUnstubbed) {
        this(warnAboutUnstubbed, new WarningsFinder(unusedStubs, allInvocations));
    }

    WarningsPrinterImpl(boolean warnAboutUnstubbed, WarningsFinder finder) {
        this.warnAboutUnstubbed = warnAboutUnstubbed;
        this.finder = finder;
    }

    public String print() {
        LoggingListener listener = new LoggingListener(warnAboutUnstubbed);
        finder.find(listener);
        return listener.getStubbingInfo();
    }
}