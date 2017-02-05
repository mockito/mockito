/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.runners;

import java.lang.reflect.InvocationTargetException;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;


/**
 * Runner moved to a new place see {@link org.mockito.junit.MockitoJUnitRunner}
 *
 * @deprecated Moved to {@link org.mockito.junit.MockitoJUnitRunner}, this class will be removed with Mockito 3
 */
@Deprecated
public class MockitoJUnitRunner extends org.mockito.junit.MockitoJUnitRunner {

    /**
     * Silent runner moved to a new place see {@link org.mockito.junit.MockitoJUnitRunner.Silent}
     *
     * @deprecated Moved to {@link org.mockito.junit.MockitoJUnitRunner.Silent}, this class will be removed with Mockito 3
     */
    @Deprecated
    public static class Silent extends MockitoJUnitRunner {
        public Silent(Class<?> klass) throws InvocationTargetException {
            super(klass);
        }
    }

    /**
     * Silent runner moved to a new place see {@link org.mockito.junit.MockitoJUnitRunner.Strict}
     *
     * @deprecated Moved to {@link org.mockito.junit.MockitoJUnitRunner.Strict}, this class will be removed with Mockito 3
     */
    @Deprecated
    public static class Strict extends MockitoJUnitRunner {
        public Strict(Class<?> klass) throws InvocationTargetException {
            super(klass);
        }
    }

    public MockitoJUnitRunner(Class<?> klass) throws InvocationTargetException {
        super(klass);
    }

    @Deprecated
    @Override
    public void run(final RunNotifier notifier) {
        super.run(notifier);
    }

    @Deprecated
    @Override
    public Description getDescription() {
        return super.getDescription();
    }

    @Deprecated
    public void filter(Filter filter) throws NoTestsRemainException {
        super.filter(filter);
    }
}
