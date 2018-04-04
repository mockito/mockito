/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.stubbing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
public class StubbingWithThrowablesTest extends TestBase {

    private LinkedList mock;

    private Map mockTwo;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
        mock = mock(LinkedList.class);
        mockTwo = mock(HashMap.class);
    }

    @Test
    public void throws_same_exception_consecutively() {
        when(mock.add("")).thenThrow(new ExceptionOne());

        //1st invocation
        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            public void call() {
                mock.add("");
            }
        }).isInstanceOf(ExceptionOne.class);

        mock.add("1");

        //2nd invocation
        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            public void call() {
                mock.add("");
            }
        }).isInstanceOf(ExceptionOne.class);
    }

    @Test
    public void throws_same_exception_consecutively_with_doThrow() {
        doThrow(new ExceptionOne()).when(mock).clear();

        //1st invocation
        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            public void call() {
                mock.clear();
            }
        }).isInstanceOf(ExceptionOne.class);

        mock.add("1");

        //2nd invocation
        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            public void call() {
                mock.clear();
            }
        }).isInstanceOf(ExceptionOne.class);
    }

    @Test
    public void shouldStubWithThrowable() throws Exception {
        IllegalArgumentException expected = new IllegalArgumentException("thrown by mock");
        when(mock.add("throw")).thenThrow(expected);

        exception.expect(sameInstance(expected));
        mock.add("throw");
    }

    @Test
    public void shouldSetThrowableToVoidMethod() throws Exception {
        IllegalArgumentException expected = new IllegalArgumentException("thrown by mock");

        doThrow(expected).when(mock).clear();

        exception.expect(sameInstance(expected));

        mock.clear();

    }

    @Test
    public void shouldLastStubbingVoidBeImportant() throws Exception {
        doThrow(new ExceptionOne()).when(mock).clear();
        doThrow(new ExceptionTwo()).when(mock).clear();

        exception.expect(ExceptionTwo.class);

        mock.clear();
    }

    @Test
    public void shouldFailStubbingThrowableOnTheSameInvocationDueToAcceptableLimitation() throws Exception {
        when(mock.size()).thenThrow(new ExceptionOne());

        exception.expect(ExceptionOne.class);

        when(mock.size()).thenThrow(new ExceptionTwo());
    }

    @Test
    public void shouldAllowSettingCheckedException() throws Exception {
        Reader reader = mock(Reader.class);
        IOException ioException = new IOException();

        when(reader.read()).thenThrow(ioException);

        exception.expect(sameInstance(ioException));

        reader.read();
    }

    @Test
    public void shouldAllowSettingError() throws Exception {
        Error error = new Error();

        when(mock.add("quake")).thenThrow(error);

        exception.expect(Error.class);

        mock.add("quake");
    }

    @Test
    public void shouldNotAllowNullExceptionType() {
        exception.expect(MockitoException.class);
        exception.expectMessage("Cannot stub with null throwable");

        when(mock.add(null)).thenThrow((Exception) null);
    }

    @Test
    public void shouldInstantiateExceptionClassOnInteraction() {
        when(mock.add(null)).thenThrow(NaughtyException.class);

        exception.expect(NaughtyException.class);

        mock.add(null);
    }

    @Test
    public void shouldInstantiateExceptionClassWithOngoingStubbingOnInteraction() {
        doThrow(NaughtyException.class).when(mock).add(null);

        exception.expect(NaughtyException.class);

        mock.add(null);
    }

    @Test
    public void shouldNotAllowSettingInvalidCheckedException() {
        exception.expect(MockitoException.class);
        exception.expectMessage("Checked exception is invalid for this method");

        when(mock.add("monkey island")).thenThrow(new Exception());
    }

    @Test
    public void shouldNotAllowSettingNullThrowable() {
        exception.expect(MockitoException.class);
        exception.expectMessage("Cannot stub with null throwable");

        when(mock.add("monkey island")).thenThrow((Throwable) null);
    }

    @Test
    public void shouldNotAllowSettingNullThrowableArray() {
        exception.expect(MockitoException.class);
        exception.expectMessage("Cannot stub with null throwable");

        when(mock.add("monkey island")).thenThrow((Throwable[]) null);
    }

    @Test
    public void shouldNotAllowSettingNullThrowableClass() {
        exception.expect(MockitoException.class);
        exception.expectMessage("Exception type cannot be null");

        when(mock.isEmpty()).thenThrow((Class) null);
    }

    @Test
    public void shouldNotAllowSettingNullThrowableClasses() {
        exception.expect(MockitoException.class);
        exception.expectMessage("Exception type cannot be null");

        when(mock.isEmpty()).thenThrow(RuntimeException.class, (Class[]) null);
    }

    @Test
    public void shouldNotAllowSettingNullVarArgThrowableClass() {
        exception.expect(MockitoException.class);
        exception.expectMessage("Exception type cannot be null");

        when(mock.isEmpty()).thenThrow(RuntimeException.class, (Class) null);
    }

    @Test
    public void doThrowShouldNotAllowSettingNullThrowableClass() {
        exception.expect(MockitoException.class);
        exception.expectMessage("Exception type cannot be null");

        doThrow((Class) null).when(mock).isEmpty();
    }

    @Test
    public void doThrowShouldNotAllowSettingNullThrowableClasses() throws Exception {
        exception.expect(MockitoException.class);
        exception.expectMessage("Exception type cannot be null");

        doThrow(RuntimeException.class, (Class) null).when(mock).isEmpty();
    }

    @Test
    public void doThrowShouldNotAllowSettingNullVarArgThrowableClasses() throws Exception {
        exception.expect(MockitoException.class);
        exception.expectMessage("Exception type cannot be null");

        doThrow(RuntimeException.class, (Class[]) null).when(mock).isEmpty();
    }

    @Test
    public void shouldNotAllowSettingNullVarArgsThrowableClasses() throws Exception {
        exception.expect(MockitoException.class);
        exception.expectMessage("Exception type cannot be null");

        when(mock.isEmpty()).thenThrow(RuntimeException.class, (Class<RuntimeException>[]) null);
    }

    @Test
    public void shouldNotAllowDifferntCheckedException() throws Exception {
        IMethods mock = mock(IMethods.class);

        exception.expect(MockitoException.class);
        exception.expectMessage("Checked exception is invalid for this method");

        when(mock.throwsIOException(0)).thenThrow(CheckedException.class);
    }

    @Test
    public void shouldNotAllowCheckedExceptionWhenErrorIsDeclared() throws Exception {
        IMethods mock = mock(IMethods.class);

        exception.expect(MockitoException.class);
        exception.expectMessage("Checked exception is invalid for this method");

        when(mock.throwsError(0)).thenThrow(CheckedException.class);
    }

    @Test
    public void shouldNotAllowCheckedExceptionWhenNothingIsDeclared() throws Exception {
        IMethods mock = mock(IMethods.class);

        exception.expect(MockitoException.class);
        exception.expectMessage("Checked exception is invalid for this method");

        when(mock.throwsNothing(true)).thenThrow(CheckedException.class);
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

    private class CheckedException extends Exception {
    }

    public class NaughtyException extends RuntimeException {
        public NaughtyException() {
            throw new RuntimeException("boo!");
        }
    }
}
