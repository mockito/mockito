/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.verification;

import org.mockito.Mockito;
import org.mockito.internal.verification.api.VerificationData;

/**
 * Allows verifying that certain behavior happened at least once / exact number
 * of times / never. E.g:
 * 
 * <pre class="code"><code class="java">
 * verify(mock, times(5)).someMethod(&quot;was called five times&quot;);
 * 
 * verify(mock, never()).someMethod(&quot;was never called&quot;);
 * 
 * verify(mock, atLeastOnce()).someMethod(&quot;was called at least once&quot;);
 * 
 * verify(mock, atLeast(2)).someMethod(&quot;was called at least twice&quot;);
 * 
 * verify(mock, atMost(3)).someMethod(&quot;was called at most 3 times&quot;);
 * 
 * </code></pre>
 * 
 * <b>times(1) is the default</b> and can be omitted
 * <p>
 * See examples in javadoc for {@link Mockito#verify(Object, VerificationMode)}
 */
public interface VerificationMode {
    
    void verify(VerificationData data);
    
    /**
     * Description will be prepended to the assertion error if verification fails.
     * @param description The custom failure message
     * @return VerificationMode
     * @since 2.0.0
     */
    VerificationMode description(String description);
}