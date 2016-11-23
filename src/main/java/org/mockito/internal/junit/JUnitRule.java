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

import java.util.Collection;
import java.util.LinkedList;

/**
 * Internal implementation.
 */
public class JUnitRule implements MockitoRule {

    private enum Strictness { SILENT, WARN, STRICT_STUBS;}
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
    private JUnitRule(MockitoLogger logger, Strictness strictness) {
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
                Throwable testFailure;
                try {
                    for (MockitoTestListener listener : listeners) {
                        Mockito.framework().addListener(listener);
                        listener.beforeTest(target, method.getName());
                    }
                    testFailure = evaluateSafely(base);
                } finally {
                    for (MockitoTestListener listener : listeners) {
                        //TODO add tests for safety removing listeners that were not added
                        Mockito.framework().removeListener(listener);
                    }
                }

                //If the infrastructure fails below, we don't see the original failure, thrown later
                for (MockitoTestListener listener : listeners) {
                    listener.afterTest(testFailure);
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

    public JUnitRule strictStubs() {
        return new JUnitRule(logger, Strictness.STRICT_STUBS);
    }
}