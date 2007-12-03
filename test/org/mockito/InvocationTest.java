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
    private Invocation newStringObjectShouldNotBeEqual;

    @Before
    public void setup() throws SecurityException, NoSuchMethodException {
        call            = new InvocationBuilder().args(" ").mock("mock").seq(1).toInvocation();
        equalCall       = new InvocationBuilder().args(" ").mock("mock").seq(2).toInvocation();
        nonEqualCall    = new InvocationBuilder().args("X").mock("mock").seq(3).toInvocation();
        
        newStringObjectShouldNotBeEqual    = new InvocationBuilder().args(new String(" ")).mock("mock").seq(4).toInvocation();
    }

    @Test
    public void shouldKnowIfIsEqualTo() {
        assertFalse(call.equals(null));
        assertFalse(call.equals(""));
        assertTrue(call.equals(equalCall));
        assertFalse(call.equals(nonEqualCall));
        assertFalse(call.equals(newStringObjectShouldNotBeEqual));
    }
    
    @Test
    public void shouldEqualToNotConsiderSequenceNumber() {
        assertTrue(call.getSequenceNumber() != equalCall.getSequenceNumber());
        assertTrue(call.equals(equalCall));
    }
    
    @Test
    public void shouldNotBeACitizenOfHashes() {
        Map map = new HashMap();
        try {
            map.put(call, "one");
        } catch (RuntimeException e) {
            assertEquals("hashCode() is not implemented", e.getMessage());
        }
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