package org.mockito.internal.junit;

import org.mockito.Mockito;
import org.mockito.internal.util.MockitoLogger;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Stubbing;
import org.mockito.listeners.MockCreationListener;
import org.mockito.listeners.StubbingListener;
import org.mockito.mock.MockCreationSettings;

import java.util.*;

import static java.util.Arrays.asList;

/**
 * Stubbing listener that is used in JUnit rules and detects stubbing problems.
 */
class RuleStubbingHintsReporter implements MockCreationListener {

    private final String testName;
    private final List<Object> mocks = new LinkedList<Object>();

    RuleStubbingHintsReporter(String testName) {
        this.testName = testName;
    }

    //TODO 384 wip

    void newStubbing(Invocation stubbing) {
        //Removing 'fake' unstubbed invocations
        //'stubbingNotFound' event (that populates unstubbed invocations) is also triggered
        // during regular stubbing using when(). It's a quirk of when() syntax. See javadoc for stubbingNotFound().
//        unstubbedInvocations.remove(stubbing);
    }

    void printStubbingMismatches(MockitoLogger logger) {
        StubbingArgMismatches mismatches = new StubbingArgMismatches(testName);
//        for (Invocation i : unstubbedInvocations) {
//            for (Invocation stubbing : stubbings) {
//                //method name & mock matches
//                if (stubbing.getMock() == i.getMock()
//                        && stubbing.getMethod().getName().equals(i.getMethod().getName())) {
//                    mismatches.add(i, stubbing);
//                }
//            }
//        }
        mismatches.log(logger);
    }

    void printUnusedStubbings(MockitoLogger logger) {
        Collection<Stubbing> stubbings = Mockito.debug().getStubbings(mocks);

        LinkedList<Stubbing> unused = ListUtil.filter(stubbings, new ListUtil.Filter<Stubbing>() {
            public boolean isOut(Stubbing s) {
                return s.wasUsed();
            }
        });

        if (unused.isEmpty()) {
            return;
        }

        StubbingHint hint = new StubbingHint(testName);
        int x = 1;
        for (Stubbing candidate : unused) {
            if (!candidate.wasUsed()) {
                hint.appendLine(x++, ". Unused ", candidate.getInvocation().getLocation());
            }
        }
        logger.log(hint.toString());
    }

    @Override
    public void mockCreated(Object mock, MockCreationSettings settings) {
        mocks.add(mock);
    }
}
