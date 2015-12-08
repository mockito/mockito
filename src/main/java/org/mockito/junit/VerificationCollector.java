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
 *       collector.verify(methods).byteReturningMethod();
 *       collector.verify(methods).simpleMethod();
 *   }
 * </code></pre>
 *
 * @see org.mockito.Mockito#verify(Object)
 * @see org.mockito.Mockito#verify(Object, VerificationMode)
 */
public interface VerificationCollector extends TestRule {

    /**
     * Lazily verify certain behaviour happened once.
     *
     * @see org.mockito.Mockito#verify(Object)
     *
     * @param <T> The type of the mock
     * @param mock to be verified
     * @return mock object itself
     */
    <T> T verify(T mock);

    /**
     * Lazily verify certain behaviour happened at least once / exact number of times / never.
     *
     * @see org.mockito.Mockito#verify(Object, VerificationMode)
     *
     * @param mock to be verified
     * @param mode times(x), atLeastOnce() or never()
     * @param <T> The type of the mock
     * @return mock object itself
     */
    <T> T verify(T mock, VerificationMode mode);

    /**
     * Collect all lazily verified behaviour. If there were failed verifications, it will
     * throw a MockitoAssertionError containing all messages indicating the failed verifications.
     *
     * @throws MockitoAssertionError If there were failed verifications
     */
    void collectAndReport() throws MockitoAssertionError;
}
