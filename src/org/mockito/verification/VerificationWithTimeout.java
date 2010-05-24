/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.verification;

import org.mockito.Mockito;

/**
 * VerificationWithTimeout is a {@link VerificationMode} that allows combining existing verification modes with 'timeout'. E.g:
 * 
 * <pre>
 * verify(mock, timeout(100).times(5)).foo();
 * 
 * verify(mock, timeout(100).never()).bar();
 * 
 * verify(mock, timeout(200).atLeastOnce()).baz();
 * </pre>
 * 
 * <p>
 * See examples in javadoc for {@link Mockito#verify(Object, VerificationMode)}
 */
public interface VerificationWithTimeout extends VerificationMode {
        
    /**
     * Allows verifying exact number of invocations within given timeout
     * <pre>
     *   verify(mock, timeout(100).times(2)).someMethod("some arg");
     * </pre>
     * 
     * See examples in javadoc for {@link Mockito} class
     * 
     * @param wantedNumberOfInvocations wanted number of invocations 
     * 
     * @return verification mode
     */
    public VerificationMode times(int wantedNumberOfInvocations);
    
    /**
     * Alias to times(0), see {@link #times(int)}
     * <p>
     * Verifies that interaction did not happen within given timeout. E.g:
     * <pre>
     *   verify(mock, timeout(100).never()).someMethod();
     * </pre>
     * 
     * <p>
     * If you want to verify there were NO interactions with the mock 
     * check out {@link Mockito#verifyNoMoreInteractions(Object...)}
     * <p>
     * See examples in javadoc for {@link Mockito} class
     * 
     * @return verification mode
     */
    public VerificationMode never();
    
    /**
     * Allows at-least-once verification withing given timeout. E.g:
     * <pre>
     *   verify(mock, timeout(100).atLeastOnce()).someMethod("some arg");
     * </pre>
     * Alias to atLeast(1)
     * <p>
     * See examples in javadoc for {@link Mockito} class
     * 
     * @return verification mode
     */
    public VerificationMode atLeastOnce();

    /**
     * Allows at-least-x verification withing given timeout. E.g:
     * <pre>
     *   verify(mock, timeout(100).atLeast(3)).someMethod("some arg");
     * </pre>
     * 
     * See examples in javadoc for {@link Mockito} class
     * 
     * @param minNumberOfInvocations minimum number of invocations 
     * 
     * @return verification mode
     */
    public VerificationMode atLeast(int minNumberOfInvocations);

    /**
     * Allows at-most-x verification within given timeout. E.g:
     * <pre>
     *   verify(mock, timeout(100).atMost(3)).someMethod("some arg");
     * </pre>
     * 
     * See examples in javadoc for {@link Mockito} class
     * 
     * @param maxNumberOfInvocations max number of invocations 
     * 
     * @return verification mode
     */
    public VerificationMode atMost(int maxNumberOfInvocations);

    /**
     * Allows checking if given method was the only one invoked. E.g:
     * <pre>
     *   verify(mock, only()).someMethod();
     *   //above is a shorthand for following 2 lines of code:
     *   verify(mock).someMethod();
     *   verifyNoMoreInvocations(mock);
     * </pre>
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