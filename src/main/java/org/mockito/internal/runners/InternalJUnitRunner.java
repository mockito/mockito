/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.runners;

import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoFramework;
import org.mockito.internal.runners.util.MockitoJUnitListener;

public class InternalJUnitRunner implements RunnerImpl {

    private final BlockJUnit4ClassRunner runner;
    private final Class<?> testClass;
    private boolean filterRequested;

    public InternalJUnitRunner(Class<?> testClass) throws InitializationError {
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
        //TODO need to be able to opt out from this new feature
        //TODO need to be able to opt in for full stack trace instead of just relying on the stack trace filter
        UnnecessaryStubbingsReporter reporter = new UnnecessaryStubbingsReporter();
        MockitoJUnitListener listener = new MockitoJUnitListener(notifier);

        MockitoFramework.setStubbingListener(reporter);
        try {
            // add listener that validates framework usage at the end of each test
            notifier.addListener(listener);
            runner.run(notifier);
        } finally {
            MockitoFramework.setStubbingListener(null);
        }

        if (!filterRequested && listener.isSussessful()) {
            //only report when:
            //1. if all tests from given test have ran
            //   Otherwise we would report unnecessary stubs even if the user runs just single test from the class
            //2. tests are successful (we don't want to add an extra failure on top of any existing failure, to avoid confusion)
            //TODO JUnit runner should have a specific message explaining to the user how it works.
            reporter.report(testClass, notifier);
        }
    }

    public Description getDescription() {
        return runner.getDescription();
    }

    public void filter(Filter filter) throws NoTestsRemainException {
        filterRequested = true;
        runner.filter(filter);
    }
}