package org.mockito.internal.junit;

import org.junit.runners.model.Statement;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * Internal implementation.
 */
public class JUnitRule {
    public Statement apply(final Statement base, final Object target) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                MockitoAnnotations.initMocks(target);
                base.evaluate();
                Mockito.validateMockitoUsage();
            }
        };
    }
}
