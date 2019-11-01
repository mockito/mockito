/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import static org.mockito.internal.stubbing.StrictnessSelector.determineStrictness;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.stubbing.UnusedStubbingReporting;
import org.mockito.invocation.Invocation;
import org.mockito.listeners.StubbingLookupEvent;
import org.mockito.listeners.StubbingLookupListener;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Stubbing;

/**
 * Default implementation of stubbing lookup listener.
 * Fails early if stub called with unexpected arguments, but only if current strictness is set to STRICT_STUBS.
 */
class DefaultStubbingLookupListener implements StubbingLookupListener, Serializable {

    private static final long serialVersionUID = -6789800638070123629L;

    private Strictness currentStrictness;
    private boolean failureReported;

    DefaultStubbingLookupListener(Strictness strictness) {
        this.currentStrictness = strictness;
    }

    public void onStubbingLookup(StubbingLookupEvent event) {
        Strictness actualStrictness = determineStrictness(event.getStubbingFound(), event.getMockSettings(), currentStrictness);

        if (actualStrictness != Strictness.STRICT_STUBS && actualStrictness != Strictness.STRICT_MOCKS) {
            return;
        }

        if (event.getStubbingFound() == null) {
            List<Invocation> argMismatchStubbings = potentialArgMismatches(event.getInvocation(), event.getAllStubbings());
            if (!argMismatchStubbings.isEmpty()) {
                failureReported = true;
                //If stubbing was not found for invocation it means that either the mock invocation was not stubbed or
                //we have a stubbing arg mismatch.
                Reporter.stubbingArgumentMismatch(event.getInvocation(), argMismatchStubbings);
            } else if (actualStrictness == Strictness.STRICT_MOCKS && event.getInvocation().getRawReturnType() != Void.TYPE) {
                failureReported = true;
                //when there are no mismatches, we still need to report unstubbed, non-void call when the mode is STRICT_MOCKS
                Reporter.unstubbedInvocation(event.getInvocation(), event.getAllStubbings());
            }
        } else {
            //when strict stubs are in use, every time a stub is realized in the code it is implicitly marked as verified
            //this way, the users don't have to repeat themselves to verify stubbed invocations (DRY)
            event.getInvocation().markVerified();
        }
    }

    private static List<Invocation> potentialArgMismatches(Invocation invocation, Collection<Stubbing> stubbings) {
        List<Invocation> matchingStubbings = new LinkedList<Invocation>();
        for (Stubbing s : stubbings) {
            if (UnusedStubbingReporting.shouldBeReported(s)
                && s.getInvocation().getMethod().getName().equals(invocation.getMethod().getName())
                //If stubbing and invocation are in the same source file we assume they are in the test code,
                // and we don't flag it as mismatch:
                && !s.getInvocation().getLocation().getSourceFile().equals(invocation.getLocation().getSourceFile())) {
                    matchingStubbings.add(s.getInvocation());
            }
        }
        return matchingStubbings;
    }

    /**
     * Enables resetting the strictness to desired level
     */
    void setCurrentStrictness(Strictness currentStrictness) {
        this.currentStrictness = currentStrictness;
    }

    /**
     * Indicates that a failure was reported (example: stubbing argument mismatch was reported)
     */
    boolean isFailureReported() {
        return failureReported;
    }
}
