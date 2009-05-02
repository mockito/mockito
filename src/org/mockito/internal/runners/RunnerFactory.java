package org.mockito.internal.runners;

import org.junit.runner.Runner;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.runners.util.RunnerProvider;

public class RunnerFactory {

    private final RunnerProvider classProvider;

    RunnerFactory(RunnerProvider classProvider) {
        this.classProvider = classProvider;
    }

    public RunnerFactory() {
        this(new RunnerProvider());
    }

    public Runner create(Class<?> klass) {
        try {
            if (classProvider.isJUnit45OrHigherAvailable()) {
                return classProvider.newInstance("org.mockito.internal.runners.MockitoJUnit45AndUpRunner", klass);
            } else {
                return classProvider.newInstance("org.mockito.internal.runners.MockitoJUnit44RunnerImpl", klass);
            }
        } catch (Throwable t) {
            throw new MockitoException(
                    "\n" +
                    "MockitoRunner can only be used with JUnit 4.4 or higher.\n" +
                    "You can upgrade your JUnit version or write your own Runner (please consider contributing your runner to the Mockito community).\n" +
                    "Bear in mind that you can still enjoy all features of the framework without using runners (they are completely optional).\n" +
                    "If you get this error despite using JUnit 4.4 or higher then please report this error to the mockito mailing list.\n"
                    , t);
        }
    }
}