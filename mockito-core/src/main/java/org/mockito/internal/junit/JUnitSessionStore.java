/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import org.junit.runners.model.Statement;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoSession;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.DisabledMockException;
import org.mockito.internal.session.MockitoSessionLoggerAdapter;
import org.mockito.plugins.MockitoLogger;
import org.mockito.quality.Strictness;

class JUnitSessionStore {

    private final MockitoLogger logger;
    private MockitoSession session;
    protected Strictness strictness;

    JUnitSessionStore(MockitoLogger logger, Strictness strictness) {
        this.logger = logger;
        this.strictness = strictness;
    }

    Statement createStatement(final Statement base, final String methodName, final Object target) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                AutoCloseable closeable;
                if (session == null) {
                    session =
                            Mockito.mockitoSession()
                                    .name(methodName)
                                    .strictness(strictness)
                                    .logger(new MockitoSessionLoggerAdapter(logger))
                                    .initMocks(target)
                                    .startMocking();
                    closeable = null;
                } else {
                    closeable = MockitoAnnotations.openMocks(target);
                }
                Throwable testFailure = evaluateSafely(base);
                try {
                    session.finishMocking(testFailure);
                } catch (DisabledMockException e) {
                    // The user cleared the inline mocks (e.g.
                    // Mockito.framework().clearInlineMocks()
                    // in @After) while this rule was still holding a session. Finishing the session
                    // inspects the mocks it created, but cleared mocks reject any access. Explain
                    // the
                    // situation instead of surfacing a bare DisabledMockException, see #1902.
                    throw new MockitoException(
                            "The JUnit rule failed to finish mocking because inline mocks were already cleared. "
                                    + "Mockito.framework().clearInlineMocks() was likely called by @After or an outer rule, "
                                    + "which runs before MockitoJUnit.rule() finishes the Mockito session. "
                                    + "Call clearInlineMocks() after all tests have finished, or use MockitoAnnotations.openMocks() "
                                    + "instead of the rule. See https://github.com/mockito/mockito/issues/1902",
                            e);
                }
                if (closeable != null) {
                    closeable.close();
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

    void setStrictness(Strictness strictness) {
        this.strictness = strictness;
        // session is null when this method is called during initialization of
        // the @Rule field of the test class
        if (session != null) {
            session.setStrictness(strictness);
        }
    }
}
