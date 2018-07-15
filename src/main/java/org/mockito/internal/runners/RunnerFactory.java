/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.runners;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.junit.MismatchReportingTestListener;
import org.mockito.internal.junit.MockitoTestListener;
import org.mockito.internal.junit.NoOpTestListener;
import org.mockito.internal.junit.StrictStubsRunnerTestListener;
import org.mockito.internal.runners.util.RunnerProvider;
import org.mockito.internal.util.Supplier;

import java.lang.reflect.InvocationTargetException;

import static org.mockito.internal.runners.util.TestMethodsFinder.hasTestMethods;

/**
 * Creates instances of Mockito JUnit Runner in a safe way, e.g. detecting inadequate version of JUnit, etc.
 */
public class RunnerFactory {

    /**
     * Creates silent runner implementation
     */
    public InternalRunner create(Class<?> klass) throws InvocationTargetException {
        return create(klass, new Supplier<MockitoTestListener>() {
            public MockitoTestListener get() {
                return new NoOpTestListener();
            }
        });
    }

    /**
     * Creates strict runner implementation
     */
    public InternalRunner createStrict(Class<?> klass) throws InvocationTargetException {
        return create(klass, new Supplier<MockitoTestListener>() {
            public MockitoTestListener get() {
                return new MismatchReportingTestListener(Plugins.getMockitoLogger());
            }
        });
    }

    /**
     * Creates strict stubs runner implementation
     *
     * TODO, let's try to apply Brice suggestion and use switch + Strictness
     */
    public InternalRunner createStrictStubs(Class<?> klass) throws InvocationTargetException {
        return create(klass, new Supplier<MockitoTestListener>() {
            public MockitoTestListener get() {
                return new StrictStubsRunnerTestListener();
            }
        });
    }

    /**
     * Creates runner implementation with provided listener supplier
     */
    public InternalRunner create(Class<?> klass, Supplier<MockitoTestListener> listenerSupplier) throws InvocationTargetException {
        try {
            String runnerClassName = "org.mockito.internal.runners.DefaultInternalRunner";
            //Warning: I'm using String literal on purpose!
            //When JUnit is not on classpath, we want the code to throw exception here so that we can catch it
            //If we statically link the class, we will get Error when class is loaded
            return new RunnerProvider().newInstance(runnerClassName, klass, listenerSupplier);
        } catch (InvocationTargetException e) {
            if (!hasTestMethods(klass)) {
                throw new MockitoException(
                    "\n" +
                    "\n" +
                    "No tests found in " + klass.getSimpleName() + "\n" +
                    "Is the method annotated with @Test?\n" +
                    "Is the method public?\n"
                    , e);
            }
            throw e;
        } catch (Throwable t) {
            throw new MockitoException(
                    "\n" +
                    "\n" +
                    "MockitoRunner can only be used with JUnit 4.5 or higher.\n" +
                    "You can upgrade your JUnit version or write your own Runner (please consider contributing your runner to the Mockito community).\n" +
                    "Bear in mind that you can still enjoy all features of the framework without using runners (they are completely optional).\n" +
                    "If you get this error despite using JUnit 4.5 or higher then please report this error to the mockito mailing list.\n"
                    , t);
        }
    }
}
