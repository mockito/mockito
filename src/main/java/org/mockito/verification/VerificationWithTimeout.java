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
    public VerificationMode times(int wantedNumberOfInvocations);
    
    /**
     * @deprecated
     * Validation with timeout combined with never simply does not make sense, as never() will typically immediately pass,
     * and therefore not wait the timeout. The behaviour you may be looking for is actually provided by after().never(). 
     * <p>
     * To avoid compilation errors upon upgrade the method is deprecated and it throws a "friendly reminder" exception.
     * <p>
     * In a future release we will remove timeout(x).atMost(y) and timeout(x).never() from the API.
     * <p>
     * Do you want to find out more? See <a href="http://code.google.com/p/mockito/issues/detail?id=235">issue 235</a>
     * 
     * @return verification mode
     */
    @Deprecated    
    public VerificationMode never();
    
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
    public VerificationMode atLeastOnce();

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
    public VerificationMode atLeast(int minNumberOfInvocations);

    /**
     * @deprecated
     *
     * <b>Deprecated</b>
     * Validation with timeout combined with never simply does not make sense, as atMost() will typically immediately pass,
     * and therefore not wait the timeout. The behaviour you may be looking for is actually provided by after().atMost(). 
     * <p>
     * To avoid compilation errors upon upgrade the method is deprecated and it throws a "friendly reminder" exception.
     * <p>
     * In a future release we will remove timeout(x).atMost(y) and timeout(x).never() from the API.
     * <p>
     * Do you want to find out more? See <a href="http://code.google.com/p/mockito/issues/detail?id=235">issue 235</a>
     *
     * @return verification mode
     */
    @Deprecated
    public VerificationMode atMost(int maxNumberOfInvocations);

    /**
     * Allows checking if given method was the only one invoked. E.g:
     * <pre class="code"><code class="java">
     *   verify(mock, only()).someMethod();
     *   //above is a shorthand for following 2 lines of code:
     *   verify(mock).someMethod();
     *   verifyNoMoreInvocations(mock);
     * </code></pre>
     * 
     * <p>
     * See also {@link Mockito#verifyNoMoreInteractions(Object...)}
     * <p>
     * See examples in javadoc for {@link Mockito} class
     * 
     * @return verification mode
     */
    public VerificationMode only();       
}