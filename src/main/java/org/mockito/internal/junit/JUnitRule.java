/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.mockito.junit.MockitoRule;
import org.mockito.plugins.MockitoLogger;
import org.mockito.quality.Strictness;

/** Internal implementation. */
public final class JUnitRule extends AbstractJUnitRule implements MockitoRule {

    /** @param strictness how strict mocking / stubbing is concerned */
    public JUnitRule(MockitoLogger logger, Strictness strictness) {
        super(logger, strictness);
    }

    @Override
    public Statement apply(final Statement base, final FrameworkMethod method, final Object target) {
        return createStatement(base, target.getClass().getSimpleName() + "." + method.getName(), target);
    }

    public MockitoRule silent() {
        return strictness(Strictness.LENIENT);
    }

    public MockitoRule strictness(Strictness strictness) {
        super.setStrictness(strictness);
        return this;
    }
}
