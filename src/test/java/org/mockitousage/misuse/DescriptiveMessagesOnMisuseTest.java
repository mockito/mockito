/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.misuse;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.exceptions.misusing.NullInsteadOfMockException;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static org.mockito.Mockito.*;

public class DescriptiveMessagesOnMisuseTest extends TestBase {

    @Mock private IMethods mock;

    class Foo {
        public final String finalMethod() {
            return null;
        }
    }

    @SuppressWarnings("all")
    @Test
    public void tryDescriptiveMessagesOnMisuse() {
        Foo foo = mock(Foo.class);

//        when(foo.finalMethod()).thenReturn("foo");
//        doReturn("foo").when(foo).finalMethod();
//        verify(foo).finalMethod();

//        doReturn("foo");
//        doReturn("bar");

//        verifyNoMoreInteractions();
//        verifyNoMoreInteractions(null);
//        verifyNoMoreInteractions("");
//        verifyZeroInteractions();
//        verifyZeroInteractions(null);
//        verifyZeroInteractions("");
//
//        inOrder();
//        inOrder(null);
//        inOrder("test");
//        InOrder inOrder = inOrder(mock(List.class));
//        inOrder.verify(mock).simpleMethod();

//        verify(null);
//        verify(mock.booleanReturningMethod());

//        verify(mock).varargs("test", anyString());

//        when("x").thenReturn("x");

//        when(mock.simpleMethod());
//        when(mock.differentMethod()).thenReturn("");
    }

    @SuppressWarnings({"MockitoUsage", "CheckReturnValue"})
    @Test(expected=NotAMockException.class)
    public void shouldScreamWhenWholeMethodPassedToVerify() {
        verify(mock.booleanReturningMethod());
    }

    @Test(expected=NotAMockException.class)
    public void shouldScreamWhenWholeMethodPassedToVerifyNoMoreInteractions() {
        verifyNoMoreInteractions(mock.byteReturningMethod());
    }

    @SuppressWarnings({"CheckReturnValue", "MockitoUsage"})
    @Test(expected=NotAMockException.class)
    public void shouldScreamWhenInOrderCreatedWithDodgyMock() {
        inOrder("not a mock");
    }

    @SuppressWarnings({"CheckReturnValue", "MockitoUsage"})
    @Test(expected=NullInsteadOfMockException.class)
    public void shouldScreamWhenInOrderCreatedWithNulls() {
        inOrder(mock, null);
    }

    @SuppressWarnings({"MockitoUsage", "CheckReturnValue"})
    @Test(expected=NullInsteadOfMockException.class)
    public void shouldScreamNullPassedToVerify() {
        verify(null);
    }

    @Test(expected=NullInsteadOfMockException.class)
    public void shouldScreamWhenNotMockPassedToVerifyNoMoreInteractions() {
        verifyNoMoreInteractions(null, "blah");
    }

    @SuppressWarnings("all")
    @Test(expected=MockitoException.class)
    public void shouldScreamWhenNullPassedToVerifyNoMoreInteractions() {
        verifyNoMoreInteractions((Object[])null);
    }
}
