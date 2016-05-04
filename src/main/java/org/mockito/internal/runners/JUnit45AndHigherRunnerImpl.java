/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.runners;

import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoFramework;
import org.mockito.exceptions.misusing.UnnecessaryStubbingException;
import org.mockito.internal.runners.util.FrameworkUsageValidator;
import org.mockito.invocation.Invocation;
import org.mockito.listeners.StubbingListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JUnit45AndHigherRunnerImpl implements RunnerImpl {

    private final BlockJUnit4ClassRunner runner;
    private final Class<?> testClass;

    public JUnit45AndHigherRunnerImpl(Class<?> testClass) throws InitializationError {
        this.testClass = testClass;
        runner = new BlockJUnit4ClassRunner(testClass) {
            protected Statement withBefores(FrameworkMethod method, Object target,
                    Statement statement) {
                // init annotated mocks before tests
                MockitoAnnotations.initMocks(target);
                return super.withBefores(method, target, statement);
            }
        };
    }

    public void run(final RunNotifier notifier) {
        final Map<String, Invocation> stubbings = new HashMap<String, Invocation>();
        final Set<String> used = new HashSet<String>();
        MockitoFramework.setStubbingListener(new StubbingListener() {
            public void newStubbing(Invocation stubbing) {
                //We compare stubbings by the location of stubbing
                //so that a stubbing in @Before is considered used when at least one test method uses it
                //but not necessarily all test methods need to trigger 'using' it
                stubbings.put(stubbing.getLocation().toString(), stubbing);
            }

            public void usedStubbing(Invocation stubbing, Invocation actual) {
                String location = stubbing.getLocation().toString();
                used.add(location);

                //perf tweak - attempting an early remove to keep the stubbings collection short
                stubbings.remove(location);
            }
        });

        try {
            // add listener that validates framework usage at the end of each test
            notifier.addListener(new FrameworkUsageValidator(notifier));
            runner.run(notifier);
        } finally {
            MockitoFramework.setStubbingListener(null);
        }

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
        notifier.fireTestFailure(new Failure(Description.createSuiteDescription(testClass),
                new UnnecessaryStubbingException("Unnecessary stubbings: \n" + stubbings)));
    }

    public Description getDescription() {
        return runner.getDescription();
    }

    public void filter(Filter filter) throws NoTestsRemainException {
        runner.filter(filter);
    }
}