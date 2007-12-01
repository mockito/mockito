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

    private LinkedList mock;

    @Before 
    public void setup() {
        mock = mock(LinkedList.class);
    }
    
    @Test
    public void shouldStubWithThrowable() throws Exception {
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
        Error error = new Error();
        
        stub(mock.add("quake")).andThrows(error);
        
        try {
            mock.add("quake");
            fail();
        } catch (Error e) {
            assertEquals(error, e);
        }
    }    
    
    @Test(expected=MockitoException.class)
    public void shouldNotAllowSettingInvalidCheckedException() throws Exception {
        stub(mock.add("monkey island")).andThrows(new Exception());
    }
    
    @Test(expected=MockitoException.class)
    public void shouldNotAllowSettingNullThrowable() throws Exception {
        stub(mock.add("monkey island")).andThrows(null);
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
