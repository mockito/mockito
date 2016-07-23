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
import org.mockito.internal.runners.util.FrameworkUsageValidator;

public class SilentJUnitRunner implements RunnerImpl {

    private final BlockJUnit4ClassRunner runner;
    private final Class<?> testClass;

    public SilentJUnitRunner(Class<?> testClass) throws InitializationError {
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
        FrameworkUsageValidator listener = new FrameworkUsageValidator(notifier);
        // add listener that validates framework usage at the end of each test
        notifier.addListener(listener);
        runner.run(notifier);
    }

    public Description getDescription() {
        return runner.getDescription();
    }

    public void filter(Filter filter) throws NoTestsRemainException {
        runner.filter(filter);
    }
}