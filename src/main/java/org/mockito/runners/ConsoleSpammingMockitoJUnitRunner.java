/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.runners;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.debugging.WarningsCollector;
import org.mockito.internal.runners.RunnerFactory;
import org.mockito.internal.runners.InternalRunner;
import org.mockito.plugins.MockitoLogger;

import java.lang.reflect.InvocationTargetException;

/**
 * @deprecated as of 2.1.0. Use the {@link org.mockito.junit.MockitoJUnitRunner} runner instead
 * which contains support for detecting unused stubs.
 * <p>
 * If you still prefer using this runner, tell us why (create ticket in our issue tracker).
 */
@Deprecated
public class ConsoleSpammingMockitoJUnitRunner extends Runner implements Filterable {

    private final MockitoLogger logger;
    private final InternalRunner runner;

    public ConsoleSpammingMockitoJUnitRunner(Class<?> klass) throws InvocationTargetException {
        this(Plugins.getMockitoLogger(), new RunnerFactory().create(klass));
    }

    ConsoleSpammingMockitoJUnitRunner(MockitoLogger logger, InternalRunner runner) {
        this.runner = runner;
        this.logger = logger;
    }

    @Override
    public void run(RunNotifier notifier) {
        RunListener listener = new RunListener() {
            WarningsCollector warningsCollector;

            @Override
            public void testStarted(Description description) throws Exception {
                warningsCollector = new WarningsCollector();
            }

            @Override public void testFailure(Failure failure) throws Exception {
                logger.log(warningsCollector.getWarnings());
            }
        };

        notifier.addListener(listener);

        runner.run(notifier);
    }

    @Override
    public Description getDescription() {
        return runner.getDescription();
    }

    public void filter(Filter filter) throws NoTestsRemainException {
        //filter is required because without it UnrootedTests show up in Eclipse
        runner.filter(filter);
    }
}
