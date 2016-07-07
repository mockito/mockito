/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.verification;

import org.mockito.Mockito;

/**
 * VerificationWithTimeout is a {@link VerificationMode} that allows combining existing verification modes with 'timeout'. E.g:
 * 
 * <pre class="code"><code class="java">
 * verify(mock, timeout(100).times(5)).foo();
 * 
 * verify(mock, timeout(100).never()).bar();
 * 
 * verify(mock, timeout(200).atLeastOnce()).baz();
 * </code></pre>
 * 
 * This is similar to {@link VerificationAfterDelay after()} except this assertion will immediately pass if it becomes true at any point,
 * whereas after() will wait the full period. Assertions which are consistently expected to be initially true and potentially become false 
 * are deprecated below, and after() should be used instead.
 * 
 * <p>
 * See examples in javadoc for {@link Mockito#verify(Object, VerificationMode)}
 */
public interface VerificationWithTimeout extends VerificationMode {
        
    /**
     * Allows verifying exact number of invocations within given timeout
     * <pre class="code"><code class="java">
     *   verify(mock, timeout(100).times(2)).someMethod("some arg");
     * </code></pre>
     * 
     * See examples in javadoc for {@link Mockito} class
     * 
     * @param wantedNumberOfInvocations wanted number of invocations 
     * 
     * @return verification mode
     */
    VerificationMode times(int wantedNumberOfInvocations);
    
    /**
     * Allows at-least-once verification within given timeout. E.g:
     * <pre class="code"><code class="java">
     *   verify(mock, timeout(100).atLeastOnce()).someMethod("some arg");
     * </code></pre>
     * Alias to atLeast(1)
     * <p>
     * See examples in javadoc for {@link Mockito} class
     * 
     * @return verification mode
     */
    VerificationMode atLeastOnce();

    /**
     * Allows at-least-x verification within given timeout. E.g:
     * <pre class="code"><code class="java">
     *   verify(mock, timeout(100).atLeast(3)).someMethod("some arg");
     * </code></pre>
     * 
     * See examples in javadoc for {@link Mockito} class
     * 
     * @param minNumberOfInvocations minimum number of invocations 
     * 
     * @return verification mode
     */
    VerificationMode atLeast(int minNumberOfInvocations);

    /**
     * Allows checking if given method was the only one invoked. E.g:
     * <pre class="code"><code class="java">
     *   verify(mock, only()).someMethod();
     *   //above is a shorthand for following 2 lines of code:
     *   verify(mock).someMethod();
     *   verifyNoMoreInteractions(mock);
     * </code></pre>
     * 
     * <p>
     * See also {@link Mockito#verifyNoMoreInteractions(Object...)}
     * <p>
     * See examples in javadoc for {@link Mockito} class
     * 
     * @return verification mode
     */
    VerificationMode only();
}