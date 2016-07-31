package org.mockito.internal.junit;

import org.mockito.internal.util.MockitoLogger;
import org.mockito.invocation.Invocation;
import org.mockito.listeners.StubbingListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class RuleStubbingsReporter implements StubbingListener {

    private final Map<String, Invocation> stubbings = new HashMap<String, Invocation>();
    private final Set<String> used = new HashSet<String>();
    private final Set<Invocation> unstubbedInvocations = new HashSet<Invocation>();

    public void newStubbing(Invocation stubbing) {
        //We compare stubbings by the location of stubbing
        //so that a stubbing in @Before is considered used when at least one test method uses it
        //but not necessarily all test methods need to trigger 'using' it
        stubbings.put(stubbing.getLocation().toString(), stubbing);

        //Removing 'fake' unstubbed invocations
        //'stubbingNotFound' event (that populates unstubbed invocations) is also triggered
        // during regular stubbing using when(). It's a quirk of when() syntax. See javadoc for stubbingNotFound().
        unstubbedInvocations.remove(stubbing);
    }

    public void usedStubbing(Invocation stubbing, Invocation actual) {
        String location = stubbing.getLocation().toString();
        used.add(location);
    }

    public void stubbingNotFound(Invocation actual) {
        unstubbedInvocations.add(actual);
    }

    public void printStubbingMismatches(MockitoLogger logger) {
        StubbingArgMismatches mismatches = new StubbingArgMismatches();
        for (Invocation i : unstubbedInvocations) {
            for (Invocation stubbing : stubbings.values()) {
                //method name & mock matches
                if (stubbing.getMock() == i.getMock()
                        && stubbing.getMethod().getName().equals(i.getMethod().getName())) {
                    mismatches.add(i, stubbing);
                }
            }
        }
        mismatches.log(logger);
    }

    public String printUnusedStubbings(MockitoLogger logger) {
        return "";
    }
}
