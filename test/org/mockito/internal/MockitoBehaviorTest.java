/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import static org.junit.Assert.assertTrue;

import java.util.*;

import org.junit.Test;

@SuppressWarnings("unchecked")
public class MockitoBehaviorTest {

    @Test
    public void shouldNotCheckForWrongNumberOfModificationsWhenVerifyingInOrder() throws Exception {
        ExpectedInvocation invocation = new ExpectedInvocation(new InvocationBuilder().toInvocation());
        MockitoBehavior behavior = new MockitoBehavior();
        
        VerifyingMode inOrder = VerifyingMode.inOrder(1, Arrays.asList(new Object()));
        assertTrue(inOrder.orderOfInvocationsMatters());
        
        behavior.checkForWrongNumberOfInvocations(invocation, inOrder);
    }
}
