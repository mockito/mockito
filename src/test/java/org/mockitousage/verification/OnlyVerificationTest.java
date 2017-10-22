/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.verification;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockitoutil.TestBase;

import java.util.List;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

public class OnlyVerificationTest extends TestBase {

    @Mock private List<Object> mock;

    @Mock private List<Object> mock2;

    @Test
    public void shouldVerifyMethodWasInvokedExclusively() {
        mock.clear();
        verify(mock, only()).clear();
    }

    @Test
    public void shouldVerifyMethodWasInvokedExclusivelyWithMatchersUsage() {
        mock.get(0);
        verify(mock, only()).get(anyInt());
    }

    @Test
    public void shouldFailIfMethodWasNotInvoked() {
        mock.clear();
        try {
            verify(mock, only()).get(0);
            fail();
        } catch (WantedButNotInvoked e) {}
    }

    @Test
    public void shouldFailIfMethodWasInvokedMoreThanOnce() {
        mock.clear();
        mock.clear();
        try {
            verify(mock, only()).clear();
            fail();
        } catch (NoInteractionsWanted e) {}
    }

    @Test
    public void shouldFailIfMethodWasInvokedButWithDifferentArguments() {
        mock.get(0);
        mock.get(2);
        try {
            verify(mock, only()).get(999);
            fail();
        } catch (WantedButNotInvoked e) {}
    }

    @Test
    public void shouldFailIfExtraMethodWithDifferentArgsFound() {
        mock.get(0);
        mock.get(2);
        try {
            verify(mock, only()).get(2);
            fail();
        } catch (NoInteractionsWanted e) {}
    }

    @Test
    public void shouldVerifyMethodWasInvokedExclusivelyWhenTwoMocksInUse() {
        mock.clear();
        mock2.get(0);
        verify(mock, only()).clear();
        verify(mock2, only()).get(0);
    }

}
