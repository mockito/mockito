package org.mockito.junit;

import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.progress.MockingProgressImpl;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;
import org.mockito.verification.VerificationStrategy;

/**
 * Mockito implementation of VerificationCollector.
 */
class VerificationCollectorImpl implements VerificationCollector {

    private StringBuilder builder;
    private int numberOfFailures;

    VerificationCollectorImpl() {
        this.resetBuilder();
    }

    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    VerificationCollectorImpl.this.assertLazily();
                    base.evaluate();
                    VerificationCollectorImpl.this.collectAndReport();
                } finally {
                    // If base.evaluate() throws an error, we must explicitly reset the VerificationStrategy
                    // to prevent subsequent tests to be assert lazily
                    mockingProgress().setVerificationStrategy(MockingProgressImpl.getDefaultVerificationStrategy());
                }
            }
        };
    }

    public void collectAndReport() throws MockitoAssertionError {
        mockingProgress().setVerificationStrategy(MockingProgressImpl.getDefaultVerificationStrategy());

        if (this.numberOfFailures > 0) {
            String error = this.builder.toString();

            this.resetBuilder();

            throw new MockitoAssertionError(error);
        }
    }

    public VerificationCollector assertLazily() {
        mockingProgress().setVerificationStrategy(new VerificationStrategy() {
            public VerificationMode maybeVerifyLazily(VerificationMode mode) {
                return new VerificationWrapper(mode);
            }
        });
        return this;
    }

    private void resetBuilder() {
        this.builder = new StringBuilder()
                .append("There were multiple verification failures:");
        this.numberOfFailures = 0;
    }

    private void append(String message) {
        this.numberOfFailures++;
        this.builder.append('\n')
                .append(this.numberOfFailures).append(". ")
                .append(message.substring(1, message.length()));
    }

    private class VerificationWrapper implements VerificationMode {

        private final VerificationMode delegate;

        private VerificationWrapper(VerificationMode delegate) {
            this.delegate = delegate;
        }

        public void verify(VerificationData data) {
            try {
                this.delegate.verify(data);
            } catch (MockitoAssertionError error) {
                VerificationCollectorImpl.this.append(error.getMessage());
            }
        }

        public VerificationMode description(String description) {
            throw new IllegalStateException("Should not fail in this mode");
        }
    }

}
