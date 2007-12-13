/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import org.junit.Before;
import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.internal.progress.OngoingVerifyingMode;

@SuppressWarnings("unchecked")
public class NumberOfInvocationsVerifierTest extends RequiresValidState {

    private NumberOfInvocationsVerifier verifier;
    
    @Before
    public void setup() {
        verifier = new NumberOfInvocationsVerifier();
    }

    @Test
    public void shouldNotCheckForWrongNumberOfModificationsWhenVerifyingAtLeastOnce() throws Exception {
        OngoingVerifyingMode inOrder = OngoingVerifyingMode.atLeastOnce();
        
        verifier.verify(null, null, inOrder);
    }
}
