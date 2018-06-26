/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.verification.within;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.verification.Within.untilNow;

import java.util.List;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class AtMostVerificationTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    @Mock
    private List<String> mock;

    @Test
    public void shouldVerifyAtMostXTimes() throws Exception {
        mock.clear();
        mock.clear();

        verify(mock, untilNow().atMost(2)).clear();
        verify(mock, untilNow().atMost(3)).clear();

        exception.expect(MockitoAssertionError.class);

        verify(mock, untilNow().atMost(1)).clear();
    }

    @Test
    public void shouldWorkWithArgumentMatchers() {
        mock.add("one");
        verify(mock, untilNow().atMost(5)).add(anyString());

        exception.expect(MockitoAssertionError.class);

        verify(mock, untilNow().atMost(0)).add(anyString());
    }

    @Test
    public void shouldNotAllowNegativeNumber() {
        exception.expect(MockitoException.class);
        exception.expectMessage("At most doesn't accept negative values");

        verify(mock, untilNow().atMost(-1)).clear();

    }

    @Test
    public void shouldPrintDecentMessage() {
        mock.clear();
        mock.clear();

        exception.expect(MockitoAssertionError.class);
        exception.expectMessage("Wanted 1 time");
        exception.expectMessage("But was 2 times");

        verify(mock, untilNow().atMost(1)).clear();
    }

    @Test
    @Ignore
    public void shouldNotAllowInOrderMode() {
        mock.clear();
        InOrder inOrder = inOrder(mock);

        exception.expect(MockitoException.class);
        exception.expectMessage(" is not implemented to work with InOrder");

        inOrder.verify(mock, untilNow().atMost(1)).clear();
    }

    @Test
    public void shouldMarkInteractionsAsVerified() {
        mock.clear();
        mock.clear();

        verify(mock, untilNow().atMost(3)).clear();
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void shouldDetectUnverifiedInMarkInteractionsAsVerified() {
        mock.clear();
        mock.clear();
        mock.add("");

        verify(mock, untilNow().atMost(3)).clear();

        exception.expect(NoInteractionsWanted.class);
        exception.expectMessage("No interactions wanted here");
        exception.expectMessage("But found this interaction on mock 'mock'");

        verifyNoMoreInteractions(mock);
    }
}
