/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;
import org.mockito.util.RequiresValidState;

@SuppressWarnings("unchecked")
public class MockitoBehaviorTest extends RequiresValidState {

    @Test
    public void shouldNotCheckForWrongNumberOfModificationsWhenVerifyingInOrder() throws Exception {
        ExpectedInvocation invocation = new ExpectedInvocation(new InvocationBuilder().toInvocation());
        MockitoBehavior behavior = new MockitoBehavior();
        
        VerifyingMode inOrder = VerifyingMode.inOrder(1, Arrays.asList(new Object()));
        assertTrue(inOrder.orderOfInvocationsMatters());
        
        behavior.checkForWrongNumberOfInvocations(invocation, inOrder);
    }
}
