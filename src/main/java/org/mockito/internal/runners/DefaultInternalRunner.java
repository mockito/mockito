/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.runners;

import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.junit.MockitoTestListener;
import org.mockito.internal.util.Supplier;

public class DefaultInternalRunner implements InternalRunner {

    private final BlockJUnit4ClassRunner runner;

    public DefaultInternalRunner(Class<?> testClass, final Supplier<MockitoTestListener> listenerSupplier) throws InitializationError {
        runner = new BlockJUnit4ClassRunner(testClass) {

            public Object target;
            private MockitoTestListener mockitoTestListener;

            protected Statement withBefores(FrameworkMethod method, Object target, Statement statement) {
                // get new test listener and add add it to the framework
                mockitoTestListener = listenerSupplier.get();
                Mockito.framework().addListener(mockitoTestListener);

                // init annotated mocks before tests
                MockitoAnnotations.initMocks(target);
                this.target = target;
                return super.withBefores(method, target, statement);
            }

            public void run(final RunNotifier notifier) {
                RunListener listener = new RunListener() {
                    Throwable failure;

                    public void testFailure(Failure failure) throws Exception {
                        this.failure = failure.getException();
                    }

                    public void testFinished(Description description) throws Exception {
                        super.testFinished(description);
                        if (mockitoTestListener != null) {
                            Mockito.framework().removeListener(mockitoTestListener);
                            mockitoTestListener.testFinished(new DefaultTestFinishedEvent(target, description.getMethodName(), failure));
                        }
                        try {
                            Mockito.validateMockitoUsage();
                        } catch(Throwable t) {
                            notifier.fireTestFailure(new Failure(description, t));
                        }
                    }
                };

                notifier.addListener(listener);
                super.run(notifier);
            }
        };
    }

    public void run(final RunNotifier notifier) {
        runner.run(notifier);
    }

    public Description getDescription() {
        return runner.getDescription();
    }

    public void filter(Filter filter) throws NoTestsRemainException {
        runner.filter(filter);
    }
}