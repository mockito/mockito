/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import org.junit.runners.model.Statement;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoSession;
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
                session.finishMocking(testFailure);
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
