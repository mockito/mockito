/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.verification;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.TooManyActualInvocations;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import java.util.List;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class BasicVerificationTest extends TestBase {

    @Mock private List<String> mock;
    @Mock private List<String> mockTwo;

    @Test
    public void shouldVerify() {
        mock.clear();
        verify(mock).clear();

        mock.add("test");
        verify(mock).add("test");

        verifyNoMoreInteractions(mock);
    }

    @Test(expected=WantedButNotInvoked.class)
    public void shouldFailVerification() {
        verify(mock).clear();
    }

    @Test
    public void shouldFailVerificationOnMethodArgument() {
        mock.clear();
        mock.add("foo");

        verify(mock).clear();
        try {
            verify(mock).add("bar");
            fail();
        } catch (AssertionError expected) {}
    }

    @Test
    public void shouldFailOnWrongMethod() {
        mock.clear();
        mock.clear();

        mockTwo.add("add");

        verify(mock, atLeastOnce()).clear();
        verify(mockTwo, atLeastOnce()).add("add");
        try {
            verify(mockTwo, atLeastOnce()).add("foo");
            fail();
        } catch (WantedButNotInvoked e) {}
    }

    @Test
    public void shouldDetectRedundantInvocation() {
        mock.clear();
        mock.add("foo");
        mock.add("bar");

        verify(mock).clear();
        verify(mock).add("foo");

        try {
            verifyNoMoreInteractions(mock);
            fail();
        } catch (NoInteractionsWanted e) {}
    }

    @Test
    public void shouldDetectWhenInvokedMoreThanOnce() {
        mock.add("foo");
        mock.clear();
        mock.clear();

        verify(mock).add("foo");

        try {
            verify(mock).clear();
            fail();
        } catch (TooManyActualInvocations e) {}
    }

    @Test
    public void shouldVerifyStubbedMethods() {
        when(mock.add("test")).thenReturn(Boolean.FALSE);

        mock.add("test");

        verify(mock).add("test");
    }


    @Test
    public void shouldDetectWhenOverloadedMethodCalled() {
        IMethods mockThree = mock(IMethods.class);

        mockThree.varargs((Object[]) new Object[] {});
        try {
            verify(mockThree).varargs((String[]) new String[] {});
            fail();
        } catch(WantedButNotInvoked e) {}
    }
}
