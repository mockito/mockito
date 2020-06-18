/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.junit;

import java.lang.reflect.InvocationTargetException;

import org.junit.runners.model.InitializationError;
import org.mockito.internal.junit.MismatchReportingTestListener;
import org.mockito.internal.junit.MockitoTestListener;
import org.mockito.internal.runners.RunnerFactory;
import org.mockito.internal.runners.StrictRunner;
import org.mockito.internal.util.SimpleMockitoLogger;
import org.mockito.internal.util.Supplier;

public class TestableJUnitRunner extends MockitoJUnitRunner {

    private static final ThreadLocal<SimpleMockitoLogger> LOGGER =
            new ThreadLocal<SimpleMockitoLogger>() {
                protected SimpleMockitoLogger initialValue() {
                    return new SimpleMockitoLogger();
                }
            };

    public TestableJUnitRunner(Class<?> klass)
            throws InvocationTargetException, InitializationError {
        super(
                new StrictRunner(
                        new RunnerFactory()
                                .create(
                                        klass,
                                        new Supplier<MockitoTestListener>() {
                                            public MockitoTestListener get() {
                                                return new MismatchReportingTestListener(
                                                        LOGGER.get());
                                            }
                                        }),
                        klass));
    }

    public static SimpleMockitoLogger refreshedLogger() {
        return LOGGER.get().clear();
    }
}
