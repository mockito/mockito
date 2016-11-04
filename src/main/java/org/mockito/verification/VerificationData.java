/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.verification;

import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;

import java.util.List;

/**
 * Data needed to perform verification of interactions.
 *
 * @since 2.2.12
 */
public interface VerificationData extends org.mockito.internal.verification.api.VerificationData {

    /**
     * All invocations recorded on the mock object that is being verified.
     * Does not include invocations recorded on other mock objects.
     *
     * @since 2.2.12
     */
    List<Invocation> getAllInvocations();

    /**
     * The target or wanted invocation.
     * Below example illustrates what is the 'target' invocation:
     * <pre class="code"><code class="java">
     *   mock.foo();   // <- invocation 1
     *   mock.bar();   // <- invocation 2
     *
     *   verify(mock).bar();  // <- target invocation
     * </code></pre>
     *
     * Target invocation can contain argument matchers therefore the returned type is {@link MatchableInvocation}
     * and not {@link Invocation}.
     *
     * @since 2.2.12
     */
    MatchableInvocation getTarget();
}