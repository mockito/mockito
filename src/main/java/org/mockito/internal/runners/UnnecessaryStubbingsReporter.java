package org.mockito.internal.runners;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.invocation.Invocation;
import org.mockito.listeners.StubbingListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by sfaber on 5/6/16.
 */
class UnnecessaryStubbingsReporter implements StubbingListener {

    private final Map<String, Invocation> stubbings = new HashMap<String, Invocation>();
    private final Set<String> used = new HashSet<String>();

    public void newStubbing(Invocation stubbing) {
        //We compare stubbings by the location of stubbing
        //so that a stubbing in @Before is considered used when at least one test method uses it
        //but not necessarily all test methods need to trigger 'using' it
        stubbings.put(stubbing.getLocation().toString(), stubbing);
    }

    //TODO make it thread safe so that users don't have to worry
    public void usedStubbing(Invocation stubbing, Invocation actual) {
        String location = stubbing.getLocation().toString();
        used.add(location);

        //perf tweak - attempting an early remove to keep the stubbings collection short
        stubbings.remove(location);
    }

    public void report(Class<?> testClass, RunNotifier notifier) {
        if (stubbings.isEmpty()) {
            //perf tweak, bailing out early to avoid extra computation
            return;
        }

        //removing all used stubbings accounting for possible constructor / @Before stubbings
        // that were used only in specific test methods (e.g. not all test methods)
        for (String u : used) {
            stubbings.remove(u);
        }

        if (stubbings.isEmpty()) {
            return;
        }

        //Oups, there are unused stubbings
        Description unnecessaryStubbings = Description.createTestDescription(testClass, "unnecessary Mockito stubbings");
        notifier.fireTestFailure(new Failure(unnecessaryStubbings,
                Reporter.formatUnncessaryStubbingException(testClass, stubbings.values())));
    }
}
