/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import org.mockito.internal.*;

@SuppressWarnings("unchecked")
public class InvocationTest {

    private Invocation call;
    private Invocation equalCall;
    private Invocation nonEqualCall;

    @Before
    public void setup() throws SecurityException, NoSuchMethodException {
        call            = new InvocationBuilder().args(" ").mock("mock").seq(1).toInvocation();
        equalCall       = new InvocationBuilder().args(" ").mock("mock").seq(2).toInvocation();
        nonEqualCall    = new InvocationBuilder().args("X").mock("mock").seq(3).toInvocation();
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
    
    @Test
    public void shouldBeACitizenOfHashes() {
        Map map = new HashMap();
        map.put(call, "one");
        map.put(nonEqualCall, "two");
        
        assertEquals(2, map.size());
    }
    
    @Test
    public void shouldPrintMethodName() {
        call = new InvocationBuilder().toInvocation();
        assertEquals("Object.simpleMethod()", call.toString());
    }
    
    @Test
    public void shouldPrintMethodArgs() {
        call = new InvocationBuilder().args("foo").toInvocation();
        assertEquals("Object.simpleMethod(\"foo\")", call.toString());
    }
    
    @Test
    public void shouldPrintMethodIntegerArgAndString() {
        call = new InvocationBuilder().args("foo", 1).toInvocation();
        assertEquals("Object.simpleMethod(\"foo\", 1)", call.toString());
    }
}