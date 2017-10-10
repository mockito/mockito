/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.junit;

import org.junit.rules.TestRule;
import org.mockito.Incubating;
import org.mockito.exceptions.base.MockitoAssertionError;

/**
 * Use this rule in order to collect multiple verification failures and report at once.
 * This new API in incubating - let us know if you find this feature useful.
 * Should it be turned on by default with Mockito JUnit Rule?
 * <p>
 * Although {@code VerificationCollector} is a JUnit Rule, it does not necessarily have to be used as a Test Rule
 * - see {@link #collectAndReport()}.
 * <p>
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
 * @see org.mockito.Mockito#verify(Object, org.mockito.verification.VerificationMode)
 * @since 2.1.0
 */
@Incubating
public interface VerificationCollector extends TestRule {

    /**
     * Collect all lazily verified behaviour. If there were failed verifications, it will
     * throw a MockitoAssertionError containing all messages indicating the failed verifications.
     * <p>
     * Normally, users don't need to call this method because it is automatically invoked when test finishes
     * (part of the JUnit Rule behavior).
     * However, in some circumstances and edge cases, it might be useful to collect and report verification
     * errors in the middle of the test (for example: some scenario tests or during debugging).
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
     *
     *       //report all verification errors now:
     *       collector.collectAndReport();
     *
     *       //some other test code
     *   }
     * </code></pre>
     *
     * @throws MockitoAssertionError If there were failed verifications
     * @since 2.1.0
     */
    @Incubating
    void collectAndReport() throws MockitoAssertionError;

    /**
     * Enforce all verifications are performed lazily. This method is automatically called when
     * used as JUnitRule and normally users don't need to use it.
     * <p>
     * You should only use this method if you are using a VerificationCollector
     * inside a method where only this method should be verified lazily. The other methods can
     * still be verified directly.
     *
     * <pre class="code"><code class="java">
     *   &#064;Test
     *   public void should_verify_lazily() {
     *       VerificationCollector collector = MockitoJUnit.collector().assertLazily();
     *
     *       verify(methods).byteReturningMethod();
     *       verify(methods).simpleMethod();
     *
     *       collector.collectAndReport();
     *   }
     * </code></pre>
     *
     * @return this
     * @since 2.1.0
     */
    @Incubating
    VerificationCollector assertLazily();
}
