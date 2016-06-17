package org.mockito.junit;

import org.junit.rules.TestRule;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.verification.VerificationMode;

/**
 * Use this rule in order to collect multiple verification failures and report at once.
 *
 * In the example below, the verification failure thrown by {@code byteReturningMethod()} does not block
 * verifying against the {@code simpleMethod()}. After the test is run, a report is generated stating all
 * collect verification failures.
 *
 * <pre class="code"><code class="java">
 *   &#064;Rule
 *   public VerificationCollector collector = MockitoJUnit.collector();
 *
 *   &#064;Test
 *   public void should_fail() {
 *       IMethods methods = mock(IMethods.class);
 *
 *       verify(methods).byteReturningMethod();
 *       verify(methods).simpleMethod();
 *   }
 * </code></pre>
 *
 * @see org.mockito.Mockito#verify(Object)
 * @see org.mockito.Mockito#verify(Object, VerificationMode)
 */
public interface VerificationCollector extends TestRule {

    /**
     * Collect all lazily verified behaviour. If there were failed verifications, it will
     * throw a MockitoAssertionError containing all messages indicating the failed verifications.
     *
     * @throws MockitoAssertionError If there were failed verifications
     */
    void collectAndReport() throws MockitoAssertionError;

    /**
     * Enforce all verifications are performed lazily. This method is automatically called when
     * used as JUnitRule.
     *
     * You should only use this method if you are using a VerificationCollector
     * inside a method where only this method should be verified lazily. The other methods can
     * still be verified directly.
     *
     * @return this
     */
    VerificationCollector assertLazily();
}
