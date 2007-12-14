/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsCalculator;
import org.mockito.internal.progress.OngoingVerifyingMode;


public interface Verifier {

    void verify(InvocationsCalculator calculator, InvocationMatcher wanted, OngoingVerifyingMode mode);
    
}
