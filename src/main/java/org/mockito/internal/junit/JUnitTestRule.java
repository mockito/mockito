/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.mockito.junit.MockitoTestRule;
import org.mockito.plugins.MockitoLogger;
import org.mockito.quality.Strictness;

public final class JUnitTestRule implements MockitoTestRule {

    private final Object testInstance;
    private final JUnitSessionStore sessionStore;

    public JUnitTestRule(MockitoLogger logger, Strictness strictness, Object testInstance) {
        this.sessionStore = new JUnitSessionStore(logger, strictness);
        this.testInstance = testInstance;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return sessionStore.createStatement(base, description.getDisplayName(), this.testInstance);
    }

    public MockitoTestRule silent() {
        return strictness(Strictness.LENIENT);
    }

    public MockitoTestRule strictness(Strictness strictness) {
        sessionStore.setStrictness(strictness);
        return this;
    }
}
