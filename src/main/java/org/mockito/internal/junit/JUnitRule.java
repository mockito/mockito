package org.mockito.internal.junit;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.MockitoLogger;
import org.mockito.junit.MockitoRule;

/**
 * Internal implementation.
 */
public class JUnitRule implements MockitoRule {
	
	private final MockitoLogger logger;
    private final boolean silent;

    public JUnitRule(MockitoLogger logger, boolean silent) {
		this.logger = logger;
        this.silent = silent;
    }

	@Override
	public Statement apply(final Statement base, FrameworkMethod method, final Object target) {
        if (silent) {
            return new SilentStatement(target, base);
        } else {
            String testName = target.getClass().getSimpleName() + "." + method.getName();
            return new DefaultStatement(target, testName, base);
        }
    }

    public JUnitRule silent() {
        return new JUnitRule(logger, true);
    }

    private class SilentStatement extends Statement {
        private final Object target;
        private final Statement base;

        public SilentStatement(Object target, Statement base) {
            this.target = target;
            this.base = base;
        }

        public void evaluate() throws Throwable {
            MockitoAnnotations.initMocks(target);
            base.evaluate();
            Mockito.validateMockitoUsage();
        }
    }

    private class DefaultStatement extends Statement {
        private final Object target;
        private final String testName;
        private final Statement base;

        DefaultStatement(Object target, String testName, Statement base) {
            this.target = target;
            this.testName = testName;
            this.base = base;
        }

        public void evaluate() throws Throwable {
            RuleStubbingHintsReporter reporter = new RuleStubbingHintsReporter(testName);
            Mockito.framework().addListener(reporter);
            try {
                performEvaluation(reporter);
            } finally {
                Mockito.framework().removeListener(reporter);
            }
        }

        private void performEvaluation(RuleStubbingHintsReporter reporter) throws Throwable {
            MockitoAnnotations.initMocks(target);
            try {
                base.evaluate();
            } catch(Throwable t) {
                reporter.printStubbingMismatches(logger);
                throw t;
            }
            reporter.printUnusedStubbings(logger);
            Mockito.validateMockitoUsage();
        }
    }
}