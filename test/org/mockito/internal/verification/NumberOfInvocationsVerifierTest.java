/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.*;
import org.mockito.RequiresValidState;
import org.mockito.internal.state.OngoingVerifyingMode;
import org.mockito.internal.verification.NumberOfInvocationsVerifier;

@SuppressWarnings("unchecked")
public class NumberOfInvocationsVerifierTest extends RequiresValidState {

    private NumberOfInvocationsVerifier verifier;
    
    @Before
    public void setup() {
        verifier = new NumberOfInvocationsVerifier();
    }

    @Test
    public void shouldNotCheckForWrongNumberOfModificationsWhenVerifyingInOrder() throws Exception {
        OngoingVerifyingMode inOrder = OngoingVerifyingMode.inOrder(1, Arrays.asList(new Object()));
        assertTrue(inOrder.orderOfInvocationsMatters());
        
        verifier.verify(null, null, inOrder);
    }
    
    @Test
    public void shouldNotCheckForWrongNumberOfModificationsWhenVerifyingAtLeastOnce() throws Exception {
        OngoingVerifyingMode inOrder = OngoingVerifyingMode.atLeastOnce();
        
        verifier.verify(null, null, inOrder);
    }
}
