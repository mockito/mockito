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

public class WarningsPrinter {

    private final List<Invocation> unusedStubs;
    private final List<InvocationMatcher> unstubbedInvocations;

    public WarningsPrinter(List<Invocation> unusedStubs, List<InvocationMatcher> unstubbedInvocations) {
        this.unusedStubs = new LinkedList<Invocation>(unusedStubs);
        this.unstubbedInvocations = new LinkedList<InvocationMatcher>(unstubbedInvocations);
    }

    public void print(MockitoLogger logger) {
        //TODO refactor after 1.7, it should be visible that this method changes the state
        warnAboutStubsUsedWithDifferentArgs(logger);
        
        warnAboutUnusedStubs(logger);
        warnAboutUnstubbedInvocations(logger);
    }

    private void warnAboutUnstubbedInvocations(MockitoLogger logger) {
        for (InvocationMatcher i : unstubbedInvocations) {
            logger.log(join(
                "[Mockito] Warning - this method was not stubbed:",
                i,
                i.getInvocation().getStackTrace().getStackTrace()[0],
                ""));
        }
    }

    private void warnAboutUnusedStubs(MockitoLogger logger) {
        for (Invocation i : unusedStubs) {
            logger.log(join(
                "[Mockito] Warning - this stub was not used:",
                i,
                i.getStackTrace().getStackTrace()[0],
                ""));
        }
    }

    private void warnAboutStubsUsedWithDifferentArgs(MockitoLogger logger) {
        Iterator<Invocation> unusedIterator = unusedStubs.iterator();
        while(unusedIterator.hasNext()) {
            Invocation unused = unusedIterator.next();
            Iterator<InvocationMatcher> unstubbedIterator = unstubbedInvocations.iterator();
            while(unstubbedIterator.hasNext()) {
                InvocationMatcher unstubbed = unstubbedIterator.next();
                if(unstubbed.hasSimilarMethod(unused)) { 
                    logger.log(join(
                            "[Mockito] Warning - stubbed method called with different arguments.",
                            "Stubbed this way:",
                            unused,
                            unused.getStackTrace().getStackTrace()[0],
                            "",
                            "But called with different arguments:",
                            unstubbed,
                            unstubbed.getInvocation().getStackTrace().getStackTrace()[0],
                            ""));
                    
                    unusedIterator.remove();
                    unstubbedIterator.remove();
                }
            }
        }
    }
}