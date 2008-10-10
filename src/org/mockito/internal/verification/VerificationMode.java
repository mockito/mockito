/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.List;

import org.mockito.Mockito;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.verification.VerificationModeImpl.Verification;

/**
 * Allows verifying that certain behavior happened at least once / exact number
 * of times / never. E.g:
 * 
 * <pre>
 * verify(mock, times(5)).someMethod(&quot;was called five times&quot;);
 * 
 * verify(mock, never()).someMethod(&quot;was never called&quot;);
 * 
 * verify(mock, atLeastOnce()).someMethod(&quot;was called at least once&quot;);
 * 
 * verify(mock, atLeast(2)).someMethod(&quot;was called at least twice&quot;);
 * 
 * </pre>
 * 
 * <b>times(1) is the default</b> and can be omitted
 * <p>
 * See examples in javadoc for {@link Mockito#verify(Object, VerificationMode)}
 */
public interface VerificationMode {

    Integer wantedCount();

    List<? extends Object> getMocksToBeVerifiedInOrder();

    Verification getVerification();
    
    void verify(List<Invocation> invocations, InvocationMatcher wanted);

    void setMocksToBeVerifiedInOrder(List<Object> mocks);
}