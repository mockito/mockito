/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockitoutil.TestBase;

public class RegisteredInvocationsTest extends TestBase {
    
    private RegisteredInvocations invocations;
    
    @Before
    public void setup() {
        invocations = new RegisteredInvocations();
    }
    
    @Test
    public void shouldNotReturnToStringMethod() throws Exception {
        Invocation toString = new InvocationBuilder().method("toString").toInvocation();
        Invocation simpleMethod = new InvocationBuilder().simpleMethod().toInvocation();
        
        invocations.add(toString);
        invocations.add(simpleMethod);
        
        assertTrue(invocations.getAll().contains(simpleMethod));
        assertFalse(invocations.getAll().contains(toString));
    }
}