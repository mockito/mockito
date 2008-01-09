/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.stubbing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.stubVoid;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.WantedButNotInvoked;

@SuppressWarnings({"serial", "unchecked"})
public class StubbingWithThrowablesTest extends RequiresValidState {

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
        stub(mock.add("throw")).toThrow(expected);
        
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
        stub(mock.get(1)).toThrow(new ExceptionOne());
        
        try {
            stub(mock.get(1)).toThrow(new ExceptionTwo());
            fail();
        } catch (ExceptionOne e) {}
    }   
    
    @Test
    public void shouldAllowSettingCheckedException() throws Exception {
        Reader reader = mock(Reader.class);
        IOException ioException = new IOException();
        
        stub(reader.read()).toThrow(ioException);
        
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
        
        stub(mock.add("quake")).toThrow(error);
        
        try {
            mock.add("quake");
            fail();
        } catch (Error e) {
            assertEquals(error, e);
        }
    }    
    
    @Test(expected=MockitoException.class)
    public void shouldNotAllowSettingInvalidCheckedException() throws Exception {
        stub(mock.add("monkey island")).toThrow(new Exception());
    }
    
    @Test(expected=MockitoException.class)
    public void shouldNotAllowSettingNullThrowable() throws Exception {
        stub(mock.add("monkey island")).toThrow(null);
    }    
    
    @Test
    public void shouldMixThrowablesAndReturnValuesOnDifferentMocks() throws Exception {
        stub(mock.add("ExceptionOne")).toThrow(new ExceptionOne());
        stub(mock.getLast()).toReturn("last");
        stubVoid(mock).toThrow(new ExceptionTwo()).on().clear();
        
        stubVoid(mockTwo).toThrow(new ExceptionThree()).on().clear();
        stub(mockTwo.containsValue("ExceptionFour")).toThrow(new ExceptionFour());
        stub(mockTwo.get("Are you there?")).toReturn("Yes!");

        assertNull(mockTwo.get("foo"));
        assertTrue(mockTwo.keySet().isEmpty());
        assertEquals("Yes!", mockTwo.get("Are you there?"));
        try {
            mockTwo.clear();
            fail();
        } catch (ExceptionThree e) {}
        try {
            mockTwo.containsValue("ExceptionFour");
            fail();
        } catch (ExceptionFour e) {}
        
        assertNull(mock.getFirst());
        assertEquals("last", mock.getLast());
        try {
            mock.add("ExceptionOne");
            fail();
        } catch (ExceptionOne e) {}
        try {
            mock.clear();
            fail();
        } catch (ExceptionTwo e) {}
    }
    
    @Test
    public void shouldStubbingWithThrowableBeVerifiable() {
        stub(mock.size()).toThrow(new RuntimeException());
        stubVoid(mock).toThrow(new RuntimeException()).on().clone();
        
        try {
            mock.size();
            fail();
        } catch (RuntimeException e) {}
        
        try {
            mock.clone();
            fail();
        } catch (RuntimeException e) {}
        
        verify(mock).size();
        verify(mock).clone();
        verifyNoMoreInteractions(mock);
    }
    
    @Test
    public void shouldStubbingWithThrowableFailVerification() {
        stub(mock.size()).toThrow(new RuntimeException());
        stubVoid(mock).toThrow(new RuntimeException()).on().clone();
        
        verifyZeroInteractions(mock);
        
        mock.add("test");
        
        try {
            verify(mock).size();
            fail();
        } catch (WantedButNotInvoked e) {}
        
        try {
            verify(mock).clone();
            fail();
        } catch (WantedButNotInvoked e) {}
        
        try {
            verifyNoMoreInteractions(mock);
            fail();
        } catch (NoInteractionsWanted e) {}
    }
    
    private class ExceptionOne extends RuntimeException {};
    private class ExceptionTwo extends RuntimeException {};
    private class ExceptionThree extends RuntimeException {};
    private class ExceptionFour extends RuntimeException {};
}
