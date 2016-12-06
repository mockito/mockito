/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.quality.Strictness;
import org.mockito.internal.util.MockitoLogger;
import org.mockito.junit.MockitoRule;

/**
 * Internal implementation.
 */
public class JUnitRule implements MockitoRule {

    private final MockitoLogger logger;
    private final MockitoTestListener listener;

    /**
     * @param logger target for the stubbing warnings
     * @param strictness how strict mocking / stubbing is concerned
     */
    public JUnitRule(MockitoLogger logger, Strictness strictness) {
        this.logger = logger;
        switch (strictness) {
            case LENIENT: listener = new NoOpTestListener(); break;
            case WARN: listener = new WarningTestListener(logger); break;
            case STRICT_STUBS: listener = new StrictStubsTestListener(); break;
            default: throw new IllegalArgumentException("Illegal argument: " + strictness);
        }
    }

	@Override
	public Statement apply(final Statement base, final FrameworkMethod method, final Object target) {
        return new Statement() {
            public void evaluate() throws Throwable {
                Mockito.framework().addListener(listener);
                Throwable testFailure;
                try {
                    //mock initialization could be part of listeners but to avoid duplication I left it here:
                    MockitoAnnotations.initMocks(target);
                    testFailure = evaluateSafely(base);
                } finally {
                    Mockito.framework().removeListener(listener);
                }

                //If the 'testFinished' fails below, we don't see the original failure, thrown later
                DefaultTestFinishedEvent event = new DefaultTestFinishedEvent(target, method.getName(), testFailure);
                listener.testFinished(event);

                if (testFailure != null) {
                    throw testFailure;
                }

                //Validate only when there is no test failure to avoid reporting multiple problems
                //This could be part of the listener but to avoid duplication I left it here:
                Mockito.validateMockitoUsage();
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

    public MockitoRule silent() {
        return new JUnitRule(logger, Strictness.LENIENT);
    }

    public MockitoRule strictness(Strictness strictness) {
        return new JUnitRule(logger, strictness);
    }
}