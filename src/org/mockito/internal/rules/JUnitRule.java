package org.mockito.internal.rules;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * Internal implementation.
 */
public class JUnitRule {
    private Object object;

    public JUnitRule(Object object) {
        this.object = object;
    }

    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                MockitoAnnotations.initMocks(object);
                base.evaluate();
                Mockito.validateMockitoUsage();
            }
        };
    }
}
