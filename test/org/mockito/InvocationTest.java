/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import static org.junit.Assert.*;

import org.junit.*;
import org.mockito.internal.*;

public class InvocationTest {

    private Invocation call;
    private Invocation equalCall;
    private Invocation nonEqualCall;

    @Before
    public void setup() throws SecurityException, NoSuchMethodException {
        call = new InvocationBuilder().args("").seq(1).toInvocation();
        equalCall = new InvocationBuilder().args("").seq(2).toInvocation();
        nonEqualCall = new InvocationBuilder().args("X").seq(3).toInvocation();
    }

    @Test
    public void shouldKnowIfIsEqualTo() {
        assertFalse(call.equals(null));
        assertFalse(call.equals(""));
        assertTrue(call.equals(equalCall));
        assertFalse(call.equals(nonEqualCall));
    }
    
    @Test
    public void shouldEqualToNotConsiderSequenceNumber() {
        assertTrue(call.getSequenceNumber() != equalCall.getSequenceNumber());
        assertTrue(call.equals(equalCall));
    }
}