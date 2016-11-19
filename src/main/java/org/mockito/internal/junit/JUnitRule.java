/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.MockitoLogger;
import org.mockito.internal.util.SimpleMockitoLogger;
import org.mockito.junit.MockitoRule;

/**
 * Internal implementation.
 */
public class JUnitRule implements MockitoRule {

    public enum Strictness { SILENT, WARN, ERROR }
	private final MockitoLogger logger;
    private final Strictness strictness;

    /**
     * @param logger target for the stubbing warnings
     * @param strictness whether the rule
     */
    public JUnitRule(MockitoLogger logger, Strictness strictness) {
		this.logger = logger;
        this.strictness = strictness;
    }

	@Override
	public Statement apply(final Statement base, FrameworkMethod method, final Object target) {
        if (strictness == Strictness.SILENT) {
            return new SilentStatement(target, base);
        } else if (strictness == Strictness.WARN) {
            String testName = target.getClass().getSimpleName() + "." + method.getName();
            return new DefaultStatement(target, testName, base, strictness);
        } else {
            String testName = target.getClass().getSimpleName() + "." + method.getName();
            return new DefaultStatement(target, testName, base, strictness);
        }
    }

    public JUnitRule silent() {
        return new JUnitRule(logger, Strictness.SILENT);
    }

    @Override
    public MockitoRule strict() {
        return new JUnitRule(logger, Strictness.ERROR);
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
        private final Strictness strictness;

        DefaultStatement(Object target, String testName, Statement base, Strictness strictness) {
            this.target = target;
            this.testName = testName;
            this.base = base;
            this.strictness = strictness;
        }

        public void evaluate() throws Throwable {
            RuleStubbingHintsReporter reporter = new RuleStubbingHintsReporter();
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
                reporter.getStubbingArgMismatches().format(testName, logger);
                throw t;
            }
            Mockito.validateMockitoUsage();
            if (strictness == Strictness.ERROR) {
                SimpleMockitoLogger log = new SimpleMockitoLogger();
                reporter.getUnusedStubbings().format(testName, log);
                if (!log.isEmpty()) {
                    throw new MockitoAssertionError(log.getLoggedInfo());
                }
            } else if (strictness == Strictness.WARN) {
                reporter.getUnusedStubbings().format(testName, logger);
            }

        }
    }
}