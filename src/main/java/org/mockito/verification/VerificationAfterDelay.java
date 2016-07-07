/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.verification;

import org.mockito.Mockito;


/**
 * VerificationAfterDelay is a {@link VerificationMode} that allows combining existing verification modes with an initial delay, e.g. 
 * <pre class="code"><code class="java">
 * verify(mock, after(100).atMost(5)).foo();
 * 
 * verify(mock, after(100).never()).bar();
 * 
 * verify(mock, after(200).atLeastOnce()).baz();
 * </code></pre>
 * 
 * This is similar to {@link VerificationWithTimeout timeout()} except the assertion will not terminate until either the condition is 
 * definitively failed, or the full time has elapsed (whereas timeout() will also stop if the conditions is true at any point, as is
 * typically the case with never() etc initially). 
 * 
 * <p>
 * See examples in javadoc for {@link Mockito#verify(Object, VerificationMode)}
 *
 */
public interface VerificationAfterDelay extends VerificationMode {
    
    /**
     * Verifies that there are exactly N invocations during the given period. This will wait the full period given.
     */
    VerificationMode times(int wantedNumberOfInvocations);

    /**
     * Allows verification that there are no invocations at any point during the given period. This will wait the 
     * full period given, unless an invocation occurs (in which case there will be immediate failure)
     */
    VerificationMode never();
    
    /**
     * Verifies that there is at least 1 invocation during the given period. This will wait the full period given.
     */
    VerificationMode atLeastOnce();
    
    /**
     * Verifies that there is are least N invocations during the given period. This will wait the full period given.
     */
    VerificationMode atLeast(int minNumberOfInvocations);
    
    /**
     * Verifies that there is are most N invocations during the given period. This will wait the full period given,
     * unless too many invocations occur (in which case there will be an immediate failure)
     */
    VerificationMode atMost(int maxNumberOfInvocations);
    
    /**
     * Verifies that there the given method is invoked and is the only method invoked. This will wait the full 
     * period given, unless another method is invoked (in which case there will be an immediate failure)
     */
    VerificationMode only();
    
}
