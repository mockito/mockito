/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoSession;
import org.mockito.internal.session.MockitoSessionLoggerAdapter;
import org.mockito.plugins.MockitoLogger;
import org.mockito.quality.Strictness;
import org.mockito.junit.MockitoRule;

/**
 * Internal implementation.
 */
public class JUnitRule implements MockitoRule {

    private final MockitoLogger logger;
    private Strictness strictness;
    private MockitoSession session;

    /**
     * @param strictness how strict mocking / stubbing is concerned
     */
    public JUnitRule(MockitoLogger logger, Strictness strictness) {
        this.logger = logger;
        this.strictness = strictness;
    }

	@Override
	public Statement apply(final Statement base, final FrameworkMethod method, final Object target) {
        return new Statement() {
            public void evaluate() throws Throwable {
                if (session == null) {
                    session = Mockito.mockitoSession()
                                     .name(target.getClass().getSimpleName() + "." + method.getName())
                                     .strictness(strictness)
                                     .logger(new MockitoSessionLoggerAdapter(logger))
                                     .initMocks(target)
                                     .startMocking();
                } else {
                    MockitoAnnotations.initMocks(target);
                }
                Throwable testFailure = evaluateSafely(base);
                session.finishMocking(testFailure);
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

    public MockitoRule silent() {
        return strictness(Strictness.LENIENT);
    }

    public MockitoRule strictness(Strictness strictness) {
        this.strictness = strictness;
        // session is null when this method is called during initialization of
        // the @Rule field of the test class
        if (session != null) {
            session.setStrictness(strictness);
        }
        return this;
    }
}
