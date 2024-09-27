/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.stubbing;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

@SuppressWarnings({"serial", "unchecked", "rawtypes"})
public class StubbingWithThrowablesTest extends TestBase {

    private LinkedList mock;

    private Map mockTwo;

    @Before
    public void setup() {
        mock = mock(LinkedList.class);
        mockTwo = mock(HashMap.class);
    }

    @Test
    public void throws_same_exception_consecutively() {
        when(mock.add("")).thenThrow(new ExceptionOne());

        // 1st invocation
        assertThatThrownBy(
                        () -> {
                            mock.add("");
                        })
                .isInstanceOf(ExceptionOne.class);

        mock.add("1");

        // 2nd invocation
        assertThatThrownBy(
                        () -> {
                            mock.add("");
                        })
                .isInstanceOf(ExceptionOne.class);
    }

    @Test
    public void throws_same_exception_consecutively_with_doThrow() {
        doThrow(new ExceptionOne()).when(mock).clear();

        // 1st invocation
        assertThatThrownBy(
                        () -> {
                            mock.clear();
                        })
                .isInstanceOf(ExceptionOne.class);

        mock.add("1");

        // 2nd invocation
        assertThatThrownBy(
                        () -> {
                            mock.clear();
                        })
                .isInstanceOf(ExceptionOne.class);
    }

    @Test
    public void throws_new_exception_consecutively_from_class() {
        when(mock.add(null)).thenThrow(NaughtyException.class);

        NaughtyException first =
                Assertions.catchThrowableOfType(() -> mock.add(null), NaughtyException.class);
        NaughtyException second =
                Assertions.catchThrowableOfType(() -> mock.add(null), NaughtyException.class);

        assertNotSame(first, second);
    }

    @Test
    public void throws_new_exception_consecutively_from_class_with_doThrow() {
        doThrow(NaughtyException.class).when(mock).add(null);

        NaughtyException first =
                Assertions.catchThrowableOfType(() -> mock.add(null), NaughtyException.class);
        NaughtyException second =
                Assertions.catchThrowableOfType(() -> mock.add(null), NaughtyException.class);

        assertNotSame(first, second);
    }

    @Test
    public void shouldStubWithThrowable() {
        IllegalArgumentException expected = new IllegalArgumentException("thrown by mock");
        when(mock.add("throw")).thenThrow(expected);

        assertThatThrownBy(
                        () -> {
                            mock.add("throw");
                        })
                .isEqualTo(expected);
    }

    @Test
    public void shouldSetThrowableToVoidMethod() {
        IllegalArgumentException expected = new IllegalArgumentException("thrown by mock");

        doThrow(expected).when(mock).clear();

        assertThatThrownBy(
                        () -> {
                            mock.clear();
                        })
                .isEqualTo(expected);
    }

    @Test
    public void shouldLastStubbingVoidBeImportant() {
        doThrow(new ExceptionOne()).when(mock).clear();
        doThrow(new ExceptionTwo()).when(mock).clear();

        assertThatThrownBy(
                        () -> {
                            mock.clear();
                        })
                .isInstanceOf(ExceptionTwo.class);
    }

    @Test
    public void shouldFailStubbingThrowableOnTheSameInvocationDueToAcceptableLimitation() {
        when(mock.size()).thenThrow(new ExceptionOne());

        assertThatThrownBy(
                        () -> {
                            when(mock.size()).thenThrow(new ExceptionTwo());
                        })
                .isInstanceOf(ExceptionOne.class);
    }

    @Test
    public void shouldAllowSettingCheckedException() throws Exception {
        Reader reader = mock(Reader.class);
        IOException ioException = new IOException();

        when(reader.read()).thenThrow(ioException);

        assertThatThrownBy(
                        () -> {
                            reader.read();
                        })
                .isEqualTo(ioException);
    }

    @Test
    public void shouldAllowSettingError() {
        Error error = new Error();

        when(mock.add("quake")).thenThrow(error);

        assertThatThrownBy(
                        () -> {
                            mock.add("quake");
                        })
                .isEqualTo(error);
    }

    @Test
    public void shouldNotAllowNullExceptionType() {
        assertThatThrownBy(
                        () -> {
                            when(mock.add(null)).thenThrow((Exception) null);
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining("Cannot stub with null throwable");
    }

    @Test
    public void shouldInstantiateExceptionClassOnInteraction() {
        when(mock.add(null)).thenThrow(NaughtyException.class);

        assertThatThrownBy(
                        () -> {
                            mock.add(null);
                        })
                .isInstanceOf(NaughtyException.class);
    }

    @Test
    public void shouldInstantiateExceptionClassWithOngoingStubbingOnInteraction() {
        doThrow(NaughtyException.class).when(mock).add(null);

        assertThatThrownBy(
                        () -> {
                            mock.add(null);
                        })
                .isInstanceOf(NaughtyException.class);
    }

    @Test
    public void shouldNotAllowSettingInvalidCheckedException() {
        assertThatThrownBy(
                        () -> {
                            when(mock.add("monkey island")).thenThrow(new Exception());
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining("Checked exception is invalid for this method");
    }

    @Test
    public void shouldNotAllowSettingNullThrowable() {
        assertThatThrownBy(
                        () -> {
                            when(mock.add("monkey island")).thenThrow((Throwable) null);
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining("Cannot stub with null throwable");
    }

    @Test
    public void shouldNotAllowSettingNullThrowableArray() {
        assertThatThrownBy(
                        () -> {
                            when(mock.add("monkey island")).thenThrow((Throwable[]) null);
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining("Cannot stub with null throwable");
    }

    private void assertExceptionTypeCanNotBeNull(ThrowingCallable throwingCallable) {
        assertThatThrownBy(throwingCallable)
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining("Exception type cannot be null");
    }

    @Test
    public void shouldNotAllowSettingNullThrowableClass() {
        assertExceptionTypeCanNotBeNull(
                () -> {
                    when(mock.isEmpty()).thenThrow((Class) null);
                });
    }

    @Test
    public void shouldNotAllowSettingNullThrowableClasses() {
        assertExceptionTypeCanNotBeNull(
                () -> {
                    when(mock.isEmpty()).thenThrow(RuntimeException.class, (Class[]) null);
                });
    }

    @Test
    public void shouldNotAllowSettingNullVarArgThrowableClass() {
        assertExceptionTypeCanNotBeNull(
                () -> {
                    when(mock.isEmpty()).thenThrow(RuntimeException.class, (Class) null);
                });
    }

    @Test
    public void doThrowShouldNotAllowSettingNullThrowableClass() {
        assertThatThrownBy(
                        () -> {
                            doThrow((Class) null).when(mock).isEmpty();
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining("Exception type cannot be null");
    }

    @Test
    public void doThrowShouldNotAllowSettingNullThrowableClasses() {
        assertThatThrownBy(
                        () -> {
                            doThrow(RuntimeException.class, (Class) null).when(mock).isEmpty();
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining("Exception type cannot be null");
    }

    @Test
    public void doThrowShouldNotAllowSettingNullVarArgThrowableClasses() {
        assertThatThrownBy(
                        () -> {
                            doThrow(RuntimeException.class, (Class[]) null).when(mock).isEmpty();
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining("Exception type cannot be null");
    }

    @Test
    public void shouldNotAllowSettingNullVarArgsThrowableClasses() {
        assertThatThrownBy(
                        () -> {
                            when(mock.isEmpty())
                                    .thenThrow(
                                            RuntimeException.class,
                                            (Class<RuntimeException>[]) null);
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining("Exception type cannot be null");
    }

    @Test
    public void shouldNotAllowDifferentCheckedException() {
        IMethods mock = mock(IMethods.class);

        assertThatThrownBy(
                        () -> {
                            when(mock.throwsIOException(0)).thenThrow(CheckedException.class);
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining("Checked exception is invalid for this method");
    }

    @Test
    public void shouldNotAllowCheckedExceptionWhenErrorIsDeclared() {
        IMethods mock = mock(IMethods.class);

        assertThatThrownBy(
                        () -> {
                            when(mock.throwsError(0)).thenThrow(CheckedException.class);
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining("Checked exception is invalid for this method");
    }

    @Test
    public void shouldNotAllowCheckedExceptionWhenNothingIsDeclared() {
        IMethods mock = mock(IMethods.class);

        assertThatThrownBy(
                        () -> {
                            when(mock.throwsNothing(true)).thenThrow(CheckedException.class);
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining("Checked exception is invalid for this method");
    }

    @Test
    public void shouldMixThrowablesAndReturnsOnDifferentMocks() {
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

        verifyNoInteractions(mock);

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

    private class ExceptionOne extends RuntimeException {}

    private class ExceptionTwo extends RuntimeException {}

    private class ExceptionThree extends RuntimeException {}

    private class ExceptionFour extends RuntimeException {}

    private class CheckedException extends Exception {}

    public class NaughtyException extends RuntimeException {
        public NaughtyException() {
            throw new RuntimeException("boo!");
        }
    }
}
