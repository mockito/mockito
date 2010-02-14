/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import static org.mockito.internal.util.StringJoiner.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.util.MockitoLogger;

public class WarningsPrinterImpl {

    private final List<Invocation> unusedStubs;
    private List<InvocationMatcher> allInvocations;
    private final boolean warnAboutUnstubbed;

    public WarningsPrinterImpl(List<Invocation> unusedStubs, List<InvocationMatcher> unstubbedInvocations) {
        this(unusedStubs, unstubbedInvocations, false);
    }

    public WarningsPrinterImpl(List<Invocation> unusedStubs, List<InvocationMatcher> allInvocations, boolean warnAboutUnstubbed) {
        this.allInvocations = allInvocations;
        this.unusedStubs = unusedStubs;
        this.warnAboutUnstubbed = warnAboutUnstubbed;
    }
    
    public void print(final MockitoLogger logger) {
        WarningsFinder finder = new WarningsFinder(unusedStubs, allInvocations);
        finder.find(new FindingsListener() {
            public void foundStubCalledWithDifferentArgs(Invocation unused, InvocationMatcher unstubbed) {
                logger.log(stubbedMethodCalledWithDifferentArguments(unused, unstubbed));
            }

            public void foundUnusedStub(Invocation unused) {
                logger.log(thisStubWasNotUsed(unused));
            }

            public void foundUstubbed(InvocationMatcher unstubbed) {
                if (warnAboutUnstubbed) {
                    logger.log(thisMethodWasNotStubbed(unstubbed));
                }
            }
        });       
    }

    private String thisStubWasNotUsed(Invocation i) {
        return "This stubbing was never used:   " + i.getLocation() + "\n";
    }

    private String thisMethodWasNotStubbed(InvocationMatcher i) {
        return join(
            "[Mockito hint] This method was not stubbed:",
            i,
            i.getInvocation().getLocation(),
            "");
    }

    private String stubbedMethodCalledWithDifferentArguments(Invocation unused, InvocationMatcher unstubbed) {
        return join(
                " *** Stubbing warnings from Mockito: *** ",
                "",
                "stubbed with those args here:   " + unused.getLocation(),
                "BUT called with different args: " + unstubbed.getInvocation().getLocation(),
                "");
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