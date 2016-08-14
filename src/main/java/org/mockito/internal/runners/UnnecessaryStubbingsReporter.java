package org.mockito.internal.runners;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.invocation.Invocation;
import org.mockito.listeners.StubbingListener;

import java.util.*;

/**
 * Reports unnecessary stubbings
 */
class UnnecessaryStubbingsReporter implements StubbingListener {

    private final Map<String, Invocation> stubbings = new LinkedHashMap<String, Invocation>();
    private final Set<String> used = new LinkedHashSet<String>();

    public void newStubbing(Invocation stubbing) {
        //We compare stubbings by the location of stubbing
        //  so that a stubbing in @Before is considered used when at least one test method uses it
        //  but not necessarily all test methods need to trigger 'using' it.
        //If we compared stubbings by 'invocation' we would not be able to satisfy this scenario
        //  because stubbing declared in the same place in code is a different 'invocation' for every test.
        //The downside of this approach is that it will not work when there are multiple stubbings / invocations
        //  per line of code. This should be pretty rare in Java, though.
        stubbings.put(stubbing.getLocation().toString(), stubbing);
    }

    public void usedStubbing(Invocation stubbing, Invocation actual) {
        String location = stubbing.getLocation().toString();
        used.add(location);

        //perf tweak, let's get rid of used stubbing now, less calculation later
        stubbings.remove(location);
    }

    public void stubbingNotFound(Invocation actual) {}

    void validateUnusedStubs(Class<?> testClass, RunNotifier notifier) {
        if (stubbings.isEmpty()) {
            //perf tweak, bailing out early to avoid extra computation
            return;
        }

        //removing all used stubbings accounting for possible constructor / @Before stubbings
        // that were used only in specific test methods (e.g. not all test methods)
        for (String u : used) {
            //it's awkward to manipulate the state of this object here,
            // we cannot safely rerun validateUnusedStubs() method.
            // However, we get good performance and simpler code.
            stubbings.remove(u);
        }

        if (stubbings.isEmpty()) {
            return;
        }

        if (stubbings.isEmpty()) {
            return; //whoa!!! All stubbings were used!
        }

        //Oups, there are unused stubbings
        Description unnecessaryStubbings = Description.createTestDescription(testClass, "unnecessary Mockito stubbings");
        notifier.fireTestFailure(new Failure(unnecessaryStubbings,
                Reporter.formatUnncessaryStubbingException(testClass, stubbings.values())));
    }
}
