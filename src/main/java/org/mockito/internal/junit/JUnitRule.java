package org.mockito.internal.junit;

import org.junit.runners.model.Statement;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.debugging.WarningsCollector;
import org.mockito.internal.util.MockitoLogger;

/**
 * Internal implementation.
 */
public class JUnitRule {

    private final MockitoLogger logger;

    public JUnitRule(MockitoLogger logger) {
        this.logger = logger;
    }

    public Statement apply(final Statement base, final Object target) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                WarningsCollector c = new WarningsCollector();
                MockitoAnnotations.initMocks(target);
                try {
                    base.evaluate();
                } catch(Throwable t) {
                    logger.log(c.getWarnings());
                    throw t;
                }
                Mockito.validateMockitoUsage();
            }
        };
    }
}
