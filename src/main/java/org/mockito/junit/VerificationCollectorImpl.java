package org.mockito.junit;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.times;

/**
 * Mockito implementation of VerificationCollector.
 */
class VerificationCollectorImpl implements VerificationCollector {

    private static final MockitoCore MOCKITO_CORE = new MockitoCore();

    private StringBuilder builder;
    private int numberOfFailures;

    public VerificationCollectorImpl() {
        this.resetBuilder();
    }

    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                base.evaluate();
                VerificationCollectorImpl.this.collectAndReport();
            }
        };
    }

    public <T> T verify(T mock) {
        return MOCKITO_CORE.verify(mock, new VerificationWrapper(times(1)));
    }

    public <T> T verify(T mock, VerificationMode mode) {
        return MOCKITO_CORE.verify(mock, new VerificationWrapper(mode));
    }

    public void collectAndReport() throws MockitoAssertionError {
        if (this.numberOfFailures > 0) {
            String error = this.builder.toString();

            this.resetBuilder();

            throw new MockitoAssertionError(error);
        }
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
