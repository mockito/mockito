/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.usage.stubbing;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.util.ExtraMatchers.firstMethodOnStackEqualsTo;

import java.io.*;
import java.util.*;

import org.junit.*;
import org.mockito.exceptions.MockitoException;

@SuppressWarnings({"serial", "unchecked"})
public class StubbingWithThrowablesTest {

    private LinkedList mock;
    private Map mockTwo;

    @Before 
    public void setup() {
        mock = mock(LinkedList.class);
        mockTwo = mock(HashMap.class);
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
    public void shouldLastStubbingVoidBeImportant() throws Exception {
        stubVoid(mock).toThrow(new ExceptionOne()).on().clear();
        stubVoid(mock).toThrow(new ExceptionTwo()).on().clear();
        
        try {
            mock.clear();
            fail();
        } catch (ExceptionTwo e) {}
    }
    
    @Test
    public void shouldFailStubbingThrowableOnTheSameInvocationDueToAcceptableLimitation() throws Exception {
        stub(mock.get(1)).andThrows(new ExceptionOne());
        
        try {
            stub(mock.get(1)).andThrows(new ExceptionTwo());
            fail();
        } catch (ExceptionOne e) {}
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
    
    @Test
    public void shouldMixThrowablesAndReturnValuesOnDifferentMocks() throws Exception {
        stub(mock.add("ExceptionOne")).andThrows(new ExceptionOne());
        stub(mock.getLast()).andReturn("last");
        stubVoid(mock).toThrow(new ExceptionTwo()).on().clear();
        
        stubVoid(mockTwo).toThrow(new ExceptionThree()).on().clear();
        stub(mockTwo.containsValue("ExceptionFour")).andThrows(new ExceptionFour());
        stub(mockTwo.get("Are you there?")).andReturn("Yes!");

        assertNull(mockTwo.get("foo"));
        assertTrue(mockTwo.keySet().isEmpty());
        assertEquals("Yes!", mockTwo.get("Are you there?"));
        try {
            mockTwo.clear();
        } catch (ExceptionThree e) {}
        try {
            mockTwo.containsKey("ExceptionFour");
        } catch (ExceptionFour e) {}
        
        assertNull(mock.getFirst());
        assertEquals("last", mock.getLast());
        try {
            mock.add("ExceptionOne");
        } catch (ExceptionOne e) {}
        try {
            mock.clear();
        } catch (ExceptionTwo e) {}
    }
    
    @Ignore
    @Test
    public void shouldDoTheStackTraceProperly() throws Exception {
        stub(mock.add("ExceptionOne")).andThrows(new ExceptionOne());

        try {
            addObjectToMockedList("ExceptionOne");
            fail();
        } catch (ExceptionOne e) {
            assertThat(e, firstMethodOnStackEqualsTo("addObjectToMockedList"));
        }
    }
    
    private void addObjectToMockedList(String string) {
        mock.add("ExceptionOne");
    }

    @Ignore
    @Test
    public void shouldVerifyWhenStubbedWithThrowable() throws Exception {
        
    }
    
    private class ExceptionOne extends RuntimeException {};
    private class ExceptionTwo extends RuntimeException {};
    private class ExceptionThree extends RuntimeException {};
    private class ExceptionFour extends RuntimeException {};
}
