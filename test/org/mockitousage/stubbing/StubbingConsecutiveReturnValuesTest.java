/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.stubbing;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.TestBase;
import org.mockito.MockitoAnnotations.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockitousage.IMethods;

public class StubbingConsecutiveReturnValuesTest extends TestBase {

    @Mock private IMethods mock;
   
    @Test
    public void shouldReturnConsecutiveValues() throws Exception {
        stub(mock.simpleMethod())
            .toReturn("one")
            .toReturn("two")
            .toReturn("three");
        
        assertEquals("one", mock.simpleMethod());
        assertEquals("two", mock.simpleMethod());
        assertEquals("three", mock.simpleMethod());
        assertEquals("three", mock.simpleMethod());
        assertEquals("three", mock.simpleMethod());
    }
    
    @Test
    public void shouldThrowConsecutively() throws Exception {
        stub(mock.simpleMethod())
            .toThrow(new RuntimeException())
            .toThrow(new IllegalArgumentException())
            .toThrow(new NullPointerException());

        try {
            mock.simpleMethod();
        } catch (RuntimeException e) {}
        
        try {
            mock.simpleMethod();
        } catch (IllegalArgumentException e) {}
        
        try {
            mock.simpleMethod();
        } catch (NullPointerException e) {}
        
        try {
            mock.simpleMethod();
        } catch (NullPointerException e) {}
    }
    
    @Test
    public void shouldMixConsecutiveReturnsWithExcepions() throws Exception {
        stub(mock.simpleMethod())
            .toThrow(new IllegalArgumentException())
            .toReturn("one")
            .toThrow(new NullPointerException())
            .toReturn(null);
        
        try {
            mock.simpleMethod();
        } catch (IllegalArgumentException e) {}
        
        assertEquals("one", mock.simpleMethod());
        
        try {
            mock.simpleMethod();
        } catch (NullPointerException e) {}
        
        assertEquals(null, mock.simpleMethod());
        assertEquals(null, mock.simpleMethod());
    }
    
    @Test(expected=MockitoException.class)
    public void shouldValidateConsecutiveException() throws Exception {
        stub(mock.simpleMethod())
            .toReturn("one")
            .toThrow(new Exception());
    }
}