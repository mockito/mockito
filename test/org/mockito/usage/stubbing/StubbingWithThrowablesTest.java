/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.usage.stubbing;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.util.LinkedList;

import org.junit.*;
import org.mockito.exceptions.MockitoException;

@SuppressWarnings("unchecked")
public class StubbingWithThrowablesTest {

    @Test
    public void shouldStubWithThrowable() throws Exception {
        LinkedList mock = mock(LinkedList.class);

        IllegalArgumentException expected = new IllegalArgumentException("thrown by mock");
        stub(mock.add("throw")).andThrows(expected);
        
        try {
            mock.add("throw");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(expected, e);
        }
    }
    
    @Test
    public void shouldSetThrowableToVoidMethod() throws Exception {
        LinkedList mock = mock(LinkedList.class);

        IllegalArgumentException expected = new IllegalArgumentException("thrown by mock");
        
        stubVoid(mock).toThrow(expected).on().clear();
        
        try {
            mock.clear();
            fail();
        } catch (Exception e) {
            assertEquals(expected, e);
        }
    }   
    
    @Test
    public void shouldAllowSettingCheckedException() throws Exception {
        Reader reader = mock(Reader.class);
        IOException ioException = new IOException();
        
        stub(reader.read()).andThrows(ioException);
        
        try {
            reader.read();
            fail();
        } catch (Exception e) {
            assertEquals(ioException, e);
        }
    }
    
    @Test
    public void shouldAllowSettingError() throws Exception {
        LinkedList mock = mock(LinkedList.class);
        Error error = new Error();
        
        stub(mock.add("quake")).andThrows(error);
        
        try {
            mock.add("quake");
            fail();
        } catch (Error e) {
            assertEquals(error, e);
        }
    }    
    
    @Test
    public void shouldNotAllowSettingInvalidCheckedException() throws Exception {
        LinkedList list = mock(LinkedList.class);
        Exception checkedException = new Exception();
        
        try {
            stub(list.add("monkey island")).andThrows(checkedException);
            fail();
        } catch (MockitoException e) {
            assertEquals("Given checked exception is invalid for this method", e.getMessage());
        }
    }
    
    @Test
    public void shouldNotAllowSettingNullThrowable() throws Exception {
        LinkedList list = mock(LinkedList.class);
        
        try {
            stub(list.add("monkey island")).andThrows(null);
            fail();
        } catch (MockitoException e) {
            assertEquals("Cannot set null throwable", e.getMessage());
        }
    }    
    
    @Ignore
    @Test
    public void shouldMixThrowablesAndReturnValuesOnDifferentMocks() throws Exception {
        
    }
    
    @Ignore
    @Test
    public void shouldVerifyWhenStubbedWithThrowable() throws Exception {
        
    }
}
