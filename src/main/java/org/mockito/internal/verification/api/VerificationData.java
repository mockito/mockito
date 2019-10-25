/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification.api;

import java.util.List;

import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;

/**
 * Data needed to perform verification of interactions.
 * This interface is considered public even though it lives in private package.
 * In the next major version of Mockito, this class will be moved to public space.
 */
public interface VerificationData {

    /**
     * All invocations recorded on the mock object that is being verified.
     * Does not include invocations recorded on other mock objects.
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

    /**
     * @deprecated - This internal method leaks internal class <code>InvocationMatcher</code>.
     * Please use {@link org.mockito.internal.verification.api.VerificationData#getTarget()} instead.
     *
     * Deprecated since 2.2.12
     */
    @Deprecated
    InvocationMatcher getWanted();
}
