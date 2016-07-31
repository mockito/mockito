package org.mockito.internal.runners;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.util.MockitoLogger;
import org.mockito.invocation.Invocation;
import org.mockito.listeners.StubbingListener;

import java.util.*;

/**
 * Created by sfaber on 5/6/16.
 */
//TODO 384 package rework, merge internal.junit with internal.runners. Rename this class (it's doing more than reporting unnecessary stubs)
//TODO 384 make it thread safe so that users don't have to worry
//TODO 384 create MockitoHint class
public class UnnecessaryStubbingsReporter implements StubbingListener {

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

    public void validateUnusedStubs(Class<?> testClass, RunNotifier notifier) {
        for (String u : used) {
            stubbings.remove(u);
        }

        if (stubbings.isEmpty()) {
            return; //whoa!!! All stubbings were used!
        }

        //Oups, there are unused stubbings
        Description unnecessaryStubbings = Description.createTestDescription(testClass, "unnecessary Mockito stubbings");
        notifier.fireTestFailure(new Failure(unnecessaryStubbings,
                Reporter.formatUnncessaryStubbingException(testClass, stubbings.values())));
    }

    //TODO 384 I'm not completely happy with putting this functionality in this listener.
    //TODO 384  Perhaps add an interfaces for the clients
    public void printStubbingMismatches(MockitoLogger logger) {
        Map<Invocation, Invocation> argMismatches = new LinkedHashMap<Invocation, Invocation>();
        for (Invocation i : unstubbedInvocations) {
            for (Invocation stubbing : stubbings.values()) {
                //method name & mock matches
                if (stubbing.getMock() == i.getMock()
                        && stubbing.getMethod().getName().equals(i.getMethod().getName())) {
                    argMismatches.put(i, stubbing);
                }
            }
        }
        if (argMismatches.isEmpty()) {
            return;
        }

        StringBuilder out = new StringBuilder("[MockitoHint] See javadoc for MockitoHint class.\n");
        int x = 1;
        for (Invocation i : argMismatches.keySet()) {
            out.append("[MockitoHint] ").append(x++).append(". unused stub  ")
                    .append(argMismatches.get(i).getLocation()).append("\n");
            out.append("[MockitoHint]    similar call ").append(i.getLocation());
        }

        logger.log(out.toString());
    }

    public String printUnusedStubbings(MockitoLogger logger) {
        return "";
    }
}
