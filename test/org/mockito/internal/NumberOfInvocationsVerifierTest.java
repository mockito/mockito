/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.*;
import org.mockito.RequiresValidState;

@SuppressWarnings("unchecked")
public class NumberOfInvocationsVerifierTest extends RequiresValidState {

    private NumberOfInvocationsVerifier verifier;
    
    @Before
    public void setup() {
        verifier = new NumberOfInvocationsVerifier();
    }

    @Test
    public void shouldNotCheckForWrongNumberOfModificationsWhenVerifyingInOrder() throws Exception {
        VerifyingMode inOrder = VerifyingMode.inOrder(1, Arrays.asList(new Object()));
        assertTrue(inOrder.orderOfInvocationsMatters());
        
        verifier.verify(null, null, inOrder);
    }
    
    @Test
    public void shouldNotCheckForWrongNumberOfModificationsWhenVerifyingAtLeastOnce() throws Exception {
        VerifyingMode inOrder = VerifyingMode.atLeastOnce();
        
        verifier.verify(null, null, inOrder);
    }
}
