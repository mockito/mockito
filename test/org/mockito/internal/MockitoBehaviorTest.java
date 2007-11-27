/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.lang.reflect.Method;
import java.util.*;
import static org.junit.Assert.*;
import org.junit.*;

@SuppressWarnings("unchecked")
public class MockitoBehaviorTest {

    private MockitoBehavior behavior;
    private ExpectedInvocation toLowerCaseInvocation;

    @Before
    public void setup() throws Exception {
        Method toLowerCase = String.class.getMethod("toLowerCase", new Class[] {});
        
        toLowerCaseInvocation = new ExpectedInvocation(new Invocation("mock", toLowerCase , new Object[] {}, 0), Collections.EMPTY_LIST);
        
        behavior = new MockitoBehavior();
    }
    
    @Test
    public void shouldNotCheckForWrongNumberOfModificationsWhenVerifyingInOrder() throws Exception {
        VerifyingMode inOrder = VerifyingMode.inOrder(1, Arrays.asList(new Object()));
        assertTrue(inOrder.orderOfInvocationsMatters());
        
        behavior.checkForWrongNumberOfInvocations(toLowerCaseInvocation, inOrder);
    }
}
