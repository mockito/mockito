package org.mockito.internal.junit;

import org.mockito.internal.util.MockitoLogger;
import org.mockito.invocation.Invocation;
import org.mockito.listeners.StubbingListener;

import java.util.*;

/**
 * Stubbing listener that is used in JUnit rules and detects stubbing problems.
 */
class RuleStubbingHintsReporter implements StubbingListener {

    private final Set<Invocation> unstubbedInvocations = new LinkedHashSet<Invocation>();
    private final Set<Invocation> stubbings = new LinkedHashSet<Invocation>();

    public void newStubbing(Invocation stubbing) {
        stubbings.add(stubbing);

        //Removing 'fake' unstubbed invocations
        //'stubbingNotFound' event (that populates unstubbed invocations) is also triggered
        // during regular stubbing using when(). It's a quirk of when() syntax. See javadoc for stubbingNotFound().
        unstubbedInvocations.remove(stubbing);
    }

    public void usedStubbing(Invocation stubbing, Invocation actual) {
        stubbings.remove(stubbing);
    }

    public void stubbingNotFound(Invocation actual) {
        unstubbedInvocations.add(actual);
    }

    void printStubbingMismatches(MockitoLogger logger) {
        StubbingArgMismatches mismatches = new StubbingArgMismatches();
        for (Invocation i : unstubbedInvocations) {
            for (Invocation stubbing : stubbings) {
                //method name & mock matches
                if (stubbing.getMock() == i.getMock()
                        && stubbing.getMethod().getName().equals(i.getMethod().getName())) {
                    mismatches.add(i, stubbing);
                }
            }
        }
        mismatches.log(logger);
    }

    void printUnusedStubbings(MockitoLogger logger) {
        if (stubbings.isEmpty()) {
            return;
        }

        StubbingHint hint = new StubbingHint();
        int x = 1;
        for (Invocation unused : stubbings) {
            hint.appendLine(x++, ". unused ", unused.getLocation());
        }
        logger.log(hint.toString());
    }
}
