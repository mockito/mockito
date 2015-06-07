/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.stubbing;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stubVoid;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockitoutil.TestBase;

@SuppressWarnings({"serial", "unchecked", "all", "deprecation"})
public class StubbingWithThrowablesTest extends TestBase {

    private LinkedList mock;
    private Map mockTwo;

    @Before 
    public void setup() {
        mock = mock(LinkedList.class);
        mockTwo = mock(HashMap.class);
    }
    
    @Test
    public void shouldStubWithThrowable() throws Exception {
        final IllegalArgumentException expected = new IllegalArgumentException("thrown by mock");
        when(mock.add("throw")).thenThrow(expected);
        
        try {
            mock.add("throw");
            fail();
        } catch (final IllegalArgumentException e) {
            assertEquals(expected, e);
        }
    }
    
    @Test
    public void shouldSetThrowableToVoidMethod() throws Exception {
        final IllegalArgumentException expected = new IllegalArgumentException("thrown by mock");
        
        stubVoid(mock).toThrow(expected).on().clear();
        try {
            mock.clear();
            fail();
        } catch (final Exception e) {
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
        } catch (final ExceptionTwo e) {}
    }
    
    @Test
    public void shouldFailStubbingThrowableOnTheSameInvocationDueToAcceptableLimitation() throws Exception {
        when(mock.get(1)).thenThrow(new ExceptionOne());
        
        try {
            when(mock.get(1)).thenThrow(new ExceptionTwo());
            fail();
        } catch (final ExceptionOne e) {}
    }   
    
    @Test
    public void shouldAllowSettingCheckedException() throws Exception {
        final Reader reader = mock(Reader.class);
        final IOException ioException = new IOException();
        
        when(reader.read()).thenThrow(ioException);
        
        try {
            reader.read();
            fail();
        } catch (final Exception e) {
            assertEquals(ioException, e);
        }
    }
    
    @Test
    public void shouldAllowSettingError() throws Exception {
        final Error error = new Error();
        
        when(mock.add("quake")).thenThrow(error);
        
        try {
            mock.add("quake");
            fail();
        } catch (final Error e) {
            assertEquals(error, e);
        }
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldInstantiateExceptionClassOnInteraction() {
        when(mock.add(null)).thenThrow(IllegalArgumentException.class);

        mock.add(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldInstantiateExceptionClassWithOngoingStubbingOnInteraction() {
        Mockito.doThrow(IllegalArgumentException.class).when(mock).add(null);

        mock.add(null);
    }
    
    @Test(expected=MockitoException.class)
    public void shouldNotAllowSettingInvalidCheckedException() throws Exception {
        when(mock.add("monkey island")).thenThrow(new Exception());
    }
    
    @Test(expected=MockitoException.class)
    public void shouldNotAllowSettingNullThrowable() throws Exception {
        when(mock.add("monkey island")).thenThrow((Throwable) null);
    }

    @Test(expected=MockitoException.class)
    public void shouldNotAllowSettingNullThrowableArray() throws Exception {
        when(mock.add("monkey island")).thenThrow((Throwable[]) null);
    }
    
    @Test
    public void shouldMixThrowablesAndReturnsOnDifferentMocks() throws Exception {
        when(mock.add("ExceptionOne")).thenThrow(new ExceptionOne());
        when(mock.getLast()).thenReturn("last");
        stubVoid(mock).toThrow(new ExceptionTwo()).on().clear();
        
        stubVoid(mockTwo).toThrow(new ExceptionThree()).on().clear();
        when(mockTwo.containsValue("ExceptionFour")).thenThrow(new ExceptionFour());
        when(mockTwo.get("Are you there?")).thenReturn("Yes!");

        assertNull(mockTwo.get("foo"));
        assertTrue(mockTwo.keySet().isEmpty());
        assertEquals("Yes!", mockTwo.get("Are you there?"));
        try {
            mockTwo.clear();
            fail();
        } catch (final ExceptionThree e) {}
        try {
            mockTwo.containsValue("ExceptionFour");
            fail();
        } catch (final ExceptionFour e) {}
        
        assertNull(mock.getFirst());
        assertEquals("last", mock.getLast());
        try {
            mock.add("ExceptionOne");
            fail();
        } catch (final ExceptionOne e) {}
        try {
            mock.clear();
            fail();
        } catch (final ExceptionTwo e) {}
    }
    
    @Test
    public void shouldStubbingWithThrowableBeVerifiable() {
        when(mock.size()).thenThrow(new RuntimeException());
        stubVoid(mock).toThrow(new RuntimeException()).on().clone();
        
        try {
            mock.size();
            fail();
        } catch (final RuntimeException e) {}
        
        try {
            mock.clone();
            fail();
        } catch (final RuntimeException e) {}
        
        verify(mock).size();
        verify(mock).clone();
        verifyNoMoreInteractions(mock);
    }
    
    @Test
    public void shouldStubbingWithThrowableFailVerification() {
        when(mock.size()).thenThrow(new RuntimeException());
        stubVoid(mock).toThrow(new RuntimeException()).on().clone();
        
        verifyZeroInteractions(mock);
        
        mock.add("test");
        
        try {
            verify(mock).size();
            fail();
        } catch (final WantedButNotInvoked e) {}
        
        try {
            verify(mock).clone();
            fail();
        } catch (final WantedButNotInvoked e) {}
        
        try {
            verifyNoMoreInteractions(mock);
            fail();
        } catch (final NoInteractionsWanted e) {}
    }
    
    private class ExceptionOne extends RuntimeException {}
    private class ExceptionTwo extends RuntimeException {}
    private class ExceptionThree extends RuntimeException {}
    private class ExceptionFour extends RuntimeException {}

    public class NaughtyException extends RuntimeException {
        public NaughtyException() {
            throw new RuntimeException("boo!");
        }
    }

    @Test(expected = NaughtyException.class)
    public void shouldShowDecentMessageWhenExcepionIsNaughty() throws Exception {
        when(mock.add("")).thenThrow(NaughtyException.class);
        mock.add("");
    }
}