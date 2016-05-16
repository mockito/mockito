package org.mockito.junit;

import org.mockito.internal.junit.JUnitRule;
import org.mockito.internal.util.ConsoleMockitoLogger;

/**
 * The JUnit rule can be used instead of {@link org.mockito.runners.MockitoJUnitRunner}. See {@link MockitoRule}.
 *
 * @since 1.10.17
 */
public class MockitoJUnit {

    /**
     * Creates rule instance that initiates &#064;Mocks
     * See {@link MockitoRule}.
     *
     * @return the rule instance
     */
    public static MockitoRule rule() {
        return new JUnitRule(new ConsoleMockitoLogger());
    }

    /**
     * Creates a rule instance that can perform lazy verifications.
     *
     * @see VerificationCollector
     * @return the rule instance
     */
    public static VerificationCollector collector() {
        return new VerificationCollectorImpl();
    }
}
