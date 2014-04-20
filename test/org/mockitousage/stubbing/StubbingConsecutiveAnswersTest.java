/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.stubbing;

import static org.mockito.Mockito.*;
import static org.mockito.stubbing.Times.times;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.stubbing.Times;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

@SuppressWarnings("deprecation")
public class StubbingConsecutiveAnswersTest extends TestBase {

    @Mock private IMethods mock;
   
    @Test
    public void shouldReturnConsecutiveValues() throws Exception {
        when(mock.simpleMethod())
            .thenReturn("one")
            .thenReturn("two")
            .thenReturn("three");
        
        assertEquals("one", mock.simpleMethod());
        assertEquals("two", mock.simpleMethod());
        assertEquals("three", mock.simpleMethod());
        assertEquals("three", mock.simpleMethod());
        assertEquals("three", mock.simpleMethod());
    }

    @SuppressWarnings("all")
    @Test
    public void shouldReturnConsecutiveValuesForTwoNulls() throws Exception {
        String nullString = null;
        when(mock.simpleMethod()).thenReturn(null, nullString);
        
        assertNull(mock.simpleMethod());        
        assertNull(mock.simpleMethod());        
    }

    @Test
    public void shouldReturnConsecutiveValuesSetByShortenThenReturnMethod() throws Exception {        
        when(mock.simpleMethod())
            .thenReturn("one", "two", "three");
        
        assertEquals("one", mock.simpleMethod());
        assertEquals("two", mock.simpleMethod());
        assertEquals("three", mock.simpleMethod());
        assertEquals("three", mock.simpleMethod());
        assertEquals("three", mock.simpleMethod());
    }

    @Test
    public void shouldReturnConsecutiveValueAndThrowExceptionssSetByShortenReturnMethods()
            throws Exception {
        when(mock.simpleMethod())
            .thenReturn("zero")
            .thenReturn("one", "two")
            .thenThrow(new NullPointerException(), new RuntimeException())
            .thenReturn("three")
            .thenThrow(new IllegalArgumentException());

        assertEquals("zero", mock.simpleMethod());
        assertEquals("one", mock.simpleMethod());
        assertEquals("two", mock.simpleMethod());
        try {
            mock.simpleMethod();
            fail();
        } catch (NullPointerException e) {}
        try {
            mock.simpleMethod();
            fail();
        } catch (RuntimeException e) {}
        assertEquals("three", mock.simpleMethod());
        try {
            mock.simpleMethod();
            fail();
        } catch (IllegalArgumentException e) {}
    }
    
    @Test
    public void shouldThrowConsecutively() throws Exception {
        when(mock.simpleMethod())
            .thenThrow(new RuntimeException())
            .thenThrow(new IllegalArgumentException())
            .thenThrow(new NullPointerException());

        try {
            mock.simpleMethod();
            fail();
        } catch (RuntimeException e) {}
        
        try {
            mock.simpleMethod();
            fail();
        } catch (IllegalArgumentException e) {}
        
        try {
            mock.simpleMethod();
            fail();
        } catch (NullPointerException e) {}
        
        try {
            mock.simpleMethod();
            fail();
        } catch (NullPointerException e) {}
    }

    @Test
    public void shouldThrowConsecutivelySetByShortenThenThrowMethod() throws Exception {
        when(mock.simpleMethod())
            .thenThrow(new RuntimeException(), new IllegalArgumentException(), new NullPointerException());

        try {
            mock.simpleMethod();
            fail();
        } catch (RuntimeException e) {}
        
        try {
            mock.simpleMethod();
            fail();
        } catch (IllegalArgumentException e) {}
        
        try {
            mock.simpleMethod();
            fail();
        } catch (NullPointerException e) {}
        
        try {
            mock.simpleMethod();
            fail();
        } catch (NullPointerException e) {}
    }
    
    @Test
    public void shouldMixConsecutiveReturnsWithExcepions() throws Exception {
        when(mock.simpleMethod())
            .thenThrow(new IllegalArgumentException())
            .thenReturn("one")
            .thenThrow(new NullPointerException())
            .thenReturn(null);
        
        try {
            mock.simpleMethod();
            fail();
        } catch (IllegalArgumentException e) {}
        
        assertEquals("one", mock.simpleMethod());
        
        try {
            mock.simpleMethod();
            fail();
        } catch (NullPointerException e) {}
        
        assertEquals(null, mock.simpleMethod());
        assertEquals(null, mock.simpleMethod());
    }
    
    @Test(expected=MockitoException.class)
    public void shouldValidateConsecutiveException() throws Exception {
        when(mock.simpleMethod())
            .thenReturn("one")
            .thenThrow(new Exception());
    }
    
    @Test
    public void shouldStubVoidMethodAndContinueThrowing() throws Exception {
        stubVoid(mock)
            .toThrow(new IllegalArgumentException())
            .toReturn()
            .toThrow(new NullPointerException())
            .on().voidMethod();
        
        try {
            mock.voidMethod();
            fail();
        } catch (IllegalArgumentException e) {}
        
        mock.voidMethod();
        
        try {
            mock.voidMethod();
            fail();
        } catch (NullPointerException e) {}
        
        try {
            mock.voidMethod();
            fail();
        } catch (NullPointerException e) {}        
    }
    
    @Test
    public void shouldStubVoidMethod() throws Exception {
        stubVoid(mock)
            .toReturn()
            .toThrow(new NullPointerException())
            .toReturn()
            .on().voidMethod();
        
        mock.voidMethod();
        
        try {
            mock.voidMethod();
            fail();
        } catch (NullPointerException e) {}
        
        mock.voidMethod();
        mock.voidMethod();
    }
    
    @Test(expected=MockitoException.class)
    public void shouldValidateConsecutiveExceptionForVoidMethod() throws Exception {
        stubVoid(mock)
            .toReturn()
            .toThrow(new Exception())
            .on().voidMethod();
    }

    @Test (expected=IllegalArgumentException.class)
    public void shouldStubMethodNoTimes() {
        when(mock.booleanReturningMethod()).thenReturn(true, times(0));
    }

    @Test
    public void shouldStubMethodOnce() {
        when(mock.booleanReturningMethod()).thenReturn(true, times(1));
        assertTrue(mock.booleanReturningMethod());
    }

    @Test
    public void shouldStubMethodSpecifiedNumberOfTimes() {
        when(mock.booleanReturningMethod()).thenReturn(true, times(3));
        assertTrue(mock.booleanReturningMethod());
        assertTrue(mock.booleanReturningMethod());
        assertTrue(mock.booleanReturningMethod());
    }

    @Test
    public void shouldStubMethodSpecifiedNumberOfTimesFollowedByADifferentValue() {
        when(mock.booleanReturningMethod()).thenReturn(true, times(3))
                .thenReturn(false);

        assertTrue(mock.booleanReturningMethod());
        assertTrue(mock.booleanReturningMethod());
        assertTrue(mock.booleanReturningMethod());
        assertFalse(mock.booleanReturningMethod());

    }

    @Test
    public void shouldHandleChainedConsecutiveStubbing() {
        when(mock.booleanReturningMethod()).thenReturn(true, times(3))
                .thenReturn(false, times(3)).thenReturn(true);

        assertTrue(mock.booleanReturningMethod());
        assertTrue(mock.booleanReturningMethod());
        assertTrue(mock.booleanReturningMethod());
        assertFalse(mock.booleanReturningMethod());
        assertFalse(mock.booleanReturningMethod());
        assertFalse(mock.booleanReturningMethod());
        assertTrue(mock.booleanReturningMethod());
    }
}