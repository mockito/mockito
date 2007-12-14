/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.internal.progress.VerificationMode;

public interface Strictly {

    <T> T verify(T mock);

    //TODO get rid of interface with int
    <T> T verify(T mock, int wantedNumberOfInvocations);
    
    <T> T verify(T mock, VerificationMode verificationMode);
    
}