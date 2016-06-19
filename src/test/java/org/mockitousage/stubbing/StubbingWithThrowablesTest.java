/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.stubbing;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockitoutil.TestBase;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static junit.framework.TestCase.*;
import static org.mockito.Mockito.*;

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
        IllegalArgumentException expected = new IllegalArgumentException("thrown by mock");
        when(mock.add("throw")).thenThrow(expected);

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

        doThrow(expected).when(mock).clear();
        try {
            mock.clear();
            fail();
        } catch (Exception e) {
            assertEquals(expected, e);
        }
    }

    @Test
    public void shouldLastStubbingVoidBeImportant() throws Exception {
        doThrow(new ExceptionOne()).when(mock).clear();
        doThrow(new ExceptionTwo()).when(mock).clear();

        try {
            mock.clear();
            fail();
        } catch (ExceptionTwo e) {
        }
    }

    @Test
    public void shouldFailStubbingThrowableOnTheSameInvocationDueToAcceptableLimitation() throws Exception {
        when(mock.get(1)).thenThrow(new ExceptionOne());

        try {
            when(mock.get(1)).thenThrow(new ExceptionTwo());
            fail();
        } catch (ExceptionOne e) {
        }
    }

    @Test
    public void shouldAllowSettingCheckedException() throws Exception {
        Reader reader = mock(Reader.class);
        IOException ioException = new IOException();

        when(reader.read()).thenThrow(ioException);

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

        when(mock.add("quake")).thenThrow(error);

        try {
            mock.add("quake");
            fail();
        } catch (Error e) {
            assertEquals(error, e);
        }
    }

    @Test(expected = MockitoException.class)
    public void shouldNotAllowNullExceptionType() {
        when(mock.add(null)).thenThrow((Exception) null);
    }


    @Test(expected = NaughtyException.class)
    public void shouldInstantiateExceptionClassOnInteraction() {
        when(mock.add(null)).thenThrow(NaughtyException.class);

        mock.add(null);
    }

    @Test(expected = NaughtyException.class)
    public void shouldInstantiateExceptionClassWithOngoingStubbingOnInteraction() {
        doThrow(NaughtyException.class).when(mock).add(null);

        mock.add(null);
    }

    @Test(expected = MockitoException.class)
    public void shouldNotAllowSettingInvalidCheckedException() throws Exception {
        when(mock.add("monkey island")).thenThrow(new Exception());
    }

    @Test(expected = MockitoException.class)
    public void shouldNotAllowSettingNullThrowable() throws Exception {
        when(mock.add("monkey island")).thenThrow((Throwable) null);
    }

    @Test(expected = MockitoException.class)
    public void shouldNotAllowSettingNullThrowableArray() throws Exception {
        when(mock.add("monkey island")).thenThrow((Throwable[]) null);
    }

    @Test
    public void shouldMixThrowablesAndReturnsOnDifferentMocks() throws Exception {
        when(mock.add("ExceptionOne")).thenThrow(new ExceptionOne());
        when(mock.getLast()).thenReturn("last");
        doThrow(new ExceptionTwo()).when(mock).clear();

        doThrow(new ExceptionThree()).when(mockTwo).clear();
        when(mockTwo.containsValue("ExceptionFour")).thenThrow(new ExceptionFour());
        when(mockTwo.get("Are you there?")).thenReturn("Yes!");

        assertNull(mockTwo.get("foo"));
        assertTrue(mockTwo.keySet().isEmpty());
        assertEquals("Yes!", mockTwo.get("Are you there?"));
        try {
            mockTwo.clear();
            fail();
        } catch (ExceptionThree e) {
        }
        try {
            mockTwo.containsValue("ExceptionFour");
            fail();
        } catch (ExceptionFour e) {
        }

        assertNull(mock.getFirst());
        assertEquals("last", mock.getLast());
        try {
            mock.add("ExceptionOne");
            fail();
        } catch (ExceptionOne e) {
        }
        try {
            mock.clear();
            fail();
        } catch (ExceptionTwo e) {
        }
    }

    @Test
    public void shouldStubbingWithThrowableBeVerifiable() {
        when(mock.size()).thenThrow(new RuntimeException());
        doThrow(new RuntimeException()).when(mock).clone();

        try {
            mock.size();
            fail();
        } catch (RuntimeException e) {
        }

        try {
            mock.clone();
            fail();
        } catch (RuntimeException e) {
        }

        verify(mock).size();
        verify(mock).clone();
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void shouldStubbingWithThrowableFailVerification() {
        when(mock.size()).thenThrow(new RuntimeException());
        doThrow(new RuntimeException()).when(mock).clone();

        verifyZeroInteractions(mock);

        mock.add("test");

        try {
            verify(mock).size();
            fail();
        } catch (WantedButNotInvoked e) {
        }

        try {
            verify(mock).clone();
            fail();
        } catch (WantedButNotInvoked e) {
        }

        try {
            verifyNoMoreInteractions(mock);
            fail();
        } catch (NoInteractionsWanted e) {
        }
    }

    private class ExceptionOne extends RuntimeException {

    }

    private class ExceptionTwo extends RuntimeException {

    }

    private class ExceptionThree extends RuntimeException {

    }

    private class ExceptionFour extends RuntimeException {

    }

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
