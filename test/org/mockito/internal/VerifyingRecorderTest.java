/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;
import org.mockito.RequiresValidState;

@SuppressWarnings("unchecked")
public class VerifyingRecorderTest extends RequiresValidState {

    @Test
    public void shouldNotCheckForWrongNumberOfModificationsWhenVerifyingInOrder() throws Exception {
        InvocationMatcher invocation = new InvocationMatcher(new InvocationBuilder().toInvocation());
        VerifyingRecorder behavior = new VerifyingRecorder();
        
        VerifyingMode inOrder = VerifyingMode.inOrder(1, Arrays.asList(new Object()));
        assertTrue(inOrder.orderOfInvocationsMatters());
        
        behavior.checkForWrongNumberOfInvocations(invocation, inOrder);
    }
    
    @Test
    public void shouldNotCheckForWrongNumberOfModificationsWhenVerifyingAtLeastOnce() throws Exception {
        InvocationMatcher invocation = new InvocationMatcher(new InvocationBuilder().toInvocation());
        VerifyingRecorder behavior = new VerifyingRecorder();
        
        VerifyingMode inOrder = VerifyingMode.atLeastOnce();
        
        behavior.checkForWrongNumberOfInvocations(invocation, inOrder);
    }
}
