/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.mockito.Mockito;
import org.mockito.internal.util.MockitoLogger;
import org.mockito.junit.MockitoRule;
import org.mockito.listeners.MockCreationListener;
import org.mockito.mock.MockCreationSettings;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Internal implementation.
 */
public class JUnitRule implements MockitoRule {

    public enum Strictness { SILENT, WARN, STRICT_STUBS }
	private final MockitoLogger logger;
    private final Collection<MockitoTestListener> listeners = new LinkedList<MockitoTestListener>();

    /**
     * @param logger target for the stubbing warnings
     * @param silent whether the rule emits warnings
     */
    public JUnitRule(MockitoLogger logger, boolean silent) {
        //TODO avoid 2 very similar constructors
		this(logger, silent? Strictness.SILENT : Strictness.WARN);
    }

    /**
     * @param logger target for the stubbing warnings
     * @param strictness how strict mocking / stubbing is concerned
     */
    public JUnitRule(MockitoLogger logger, Strictness strictness) {
        this.logger = logger;
        if (strictness == Strictness.SILENT) {
            listeners.add(new SilentTestListener());
        } else if (strictness == Strictness.WARN) {
            listeners.add(new WarningTestListener(logger));
        } else if (strictness == Strictness.STRICT_STUBS) {
            listeners.add(new StrictStubsTestListener());
        }
    }

	@Override
	public Statement apply(final Statement base, final FrameworkMethod method, final Object target) {
        return new Statement() {
            public void evaluate() throws Throwable {
                MockCollector mockCollector = new MockCollector();
                Mockito.framework().addListener(mockCollector);

                Throwable testFailure;
                try {
                    for (MockitoTestListener listener : listeners) {
                        listener.beforeTest(target, method.getName());
                    }

                    testFailure = evaluateSafely(base);
                } finally {
                     Mockito.framework().removeListener(mockCollector);
                }

                //If the infrastructure fails below, we don't see the original failure, thrown later
                for (MockitoTestListener listener : listeners) {
                    listener.afterTest(mockCollector.mocks, testFailure);
                }

                if (testFailure != null) {
                    throw testFailure;
                }
            }

            private Throwable evaluateSafely(Statement base) {
                try {
                    base.evaluate();
                    return null;
                } catch (Throwable throwable) {
                    return throwable;
                }
            }
        };
    }

    public JUnitRule silent() {
        return new JUnitRule(logger, true);
    }

    private static class MockCollector implements MockCreationListener {
        private final List<Object> mocks = new LinkedList<Object>();

        public void onMockCreated(Object mock, MockCreationSettings settings) {
            mocks.add(mock);
        }
    }

}