/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import org.junit.Before;
import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.internal.progress.VerificationMode;

@SuppressWarnings("unchecked")
public class NumberOfInvocationsVerifierTest extends RequiresValidState {

    private NumberOfInvocationsVerifier verifier;
    //TODO other tests
    
    @Before
    public void setup() {
        verifier = new NumberOfInvocationsVerifier();
    }

    @Test
    public void shouldNotCheckForWrongNumberOfModificationsWhenAtLeastOnceVerification() throws Exception {
        verifier.verify(null, null, VerificationMode.atLeastOnce());
    }
}
