package org.mockito.internal.debugging;

import static org.mockito.internal.util.StringJoiner.*;

import java.util.Iterator;
import java.util.List;

import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.util.MockitoLogger;

public class WarningsPrinter {

    private final List<Invocation> unusedStubs;
    private final List<InvocationMatcher> unstubbedInvocations;

    public WarningsPrinter(List<Invocation> unusedStubs, List<InvocationMatcher> unstubbedInvocations) {
        this.unusedStubs = unusedStubs;
        this.unstubbedInvocations = unstubbedInvocations;
    }

    public void print(MockitoLogger logger) {
        if (!shouldPrint()) {
            return;
        }
        
        //TODO it should be visible that this method changes the state
        warnAboutStubsUsedWithDifferentArgs(logger);
        warnAboutUnusedStubs(logger);
        warnAboutUnstubbedInvocations(logger);
    }

    private void warnAboutUnstubbedInvocations(MockitoLogger logger) {
        for (InvocationMatcher i : unstubbedInvocations) {
            logger.println(join(
                "[Mockito] Warning - this method was not stubbed:",
                i,
                "Here:",
                i.getInvocation().getStackTrace().getStackTrace()[0],
                ""));
        }
    }

    private void warnAboutUnusedStubs(MockitoLogger logger) {
        for (Invocation i : unusedStubs) {
            logger.println(join(
                "[Mockito] Warning - this stub was not used:",
                i,
                "Here:",
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
                    logger.println(join(
                            "[Mockito] Warning - stubbed method called with different arguments.",
                            "Stubbed this way:",
                            unused,
                            "Here:",
                            unused.getStackTrace().getStackTrace()[0],
                            "",
                            "But called with different arguments:",
                            unstubbed.getInvocation(),
                            "Here:",
                            unstubbed.getInvocation().getStackTrace().getStackTrace()[0],
                            ""));
                    
                    unusedIterator.remove();
                    unstubbedIterator.remove();
                }
            }
        }
    }

    private boolean shouldPrint() {
        //TODO test, include unstubbedInvocations...
        return !unusedStubs.isEmpty() || !unstubbedInvocations.isEmpty();
    }
}