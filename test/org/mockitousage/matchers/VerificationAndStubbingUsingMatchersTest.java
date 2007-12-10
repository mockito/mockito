/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.matchers;

import static org.junit.Assert.*;
import static org.mockito.CrazyMatchers.startsWith;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.*;
import org.mockito.RequiresValidState;
import org.mockito.exceptions.verification.VerificationError;
import org.mockitousage.IMethods;

@SuppressWarnings("unchecked")  
public class VerificationAndStubbingUsingMatchersTest extends RequiresValidState {
    private IMethods one;
    private IMethods two;
    private IMethods three;

    @Before
    public void setUp() {
        one = mock(IMethods.class);
        two = mock(IMethods.class);
        three = mock(IMethods.class);
    }
    
    @Test
    public void shouldStubUsingMatchers() {
        stub(one.simpleMethod(2)).andReturn("2");
        stub(two.simpleMethod(anyString())).andReturn("any");
        stub(three.simpleMethod(startsWith("test"))).andThrows(new RuntimeException());

        assertEquals(null, one.simpleMethod(1));
        assertEquals("2", one.simpleMethod(2));
        
        assertEquals("any", two.simpleMethod("two"));
        assertEquals("any", two.simpleMethod("two again"));
        
        assertEquals(null, three.simpleMethod("three"));
        assertEquals(null, three.simpleMethod("three again"));
       
        try {
            three.simpleMethod("test three again");
            fail();
        } catch (RuntimeException e) {};
    }
    
    @Test
    public void shouldVerifyUsingMatchers() {
        stubVoid(one).toThrow(new RuntimeException()).on().oneArg(true);
        stub(three.varargsObject(5, "first arg", "second arg")).andReturn("stubbed");

        try {
            one.oneArg(true);
            fail();
        } catch (RuntimeException e) {}

        one.simpleMethod(100);
        two.simpleMethod("test Mockito");
        three.varargsObject(10, "first arg", "second arg");
        
        assertEquals("stubbed", three.varargsObject(5, "first arg", "second arg"));

        verify(one).oneArg(eq(true));
        verify(one).simpleMethod(anyInt());
        verify(two).simpleMethod(startsWith("test"));
        verify(three).varargsObject(5, "first arg", "second arg");
        verify(three).varargsObject(eq(10), eq("first arg"), startsWith("second"));
        
        verifyNoMoreInteractions(one, two, three);
        
        try {
            verify(three).varargsObject(eq(10), eq("first arg"), startsWith("third"));
            fail();
        } catch (VerificationError e) {}
    }
}