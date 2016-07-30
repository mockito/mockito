package org.mockito.internal.junit;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.runners.UnnecessaryStubbingsReporter;
import org.mockito.internal.util.MockitoLogger;
import org.mockito.junit.MockitoRule;

/**
 * Internal implementation.
 */
public class JUnitRule implements MockitoRule {
	
	private final MockitoLogger logger;

	public JUnitRule(MockitoLogger logger) {
		this.logger = logger;
	}
	@Override
	public Statement apply(final Statement base, FrameworkMethod method, final Object target) {
		return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                UnnecessaryStubbingsReporter reporter = new UnnecessaryStubbingsReporter();
                Mockito.framework().setStubbingListener(reporter);
                try {
                    performEvaluation(reporter);
                } finally {
                    Mockito.framework().setStubbingListener(null);
                }
            }

            private void performEvaluation(UnnecessaryStubbingsReporter reporter) throws Throwable {
                MockitoAnnotations.initMocks(target);
                try {
                    base.evaluate();
                } catch(Throwable t) {
                    logger.log(reporter.printStubbingMismatches());
                    throw t;
                }
                logger.log(reporter.printUnusedStubbings());
                Mockito.validateMockitoUsage();
            }
        };
    }
}