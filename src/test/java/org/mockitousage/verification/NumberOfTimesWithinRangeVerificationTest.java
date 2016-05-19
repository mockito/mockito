/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.verification;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.verification.NeverWantedButInvoked;
import org.mockito.exceptions.verification.TooLittleActualInvocations;
import org.mockito.exceptions.verification.TooManyActualInvocations;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockitoutil.TestBase;

import java.util.LinkedList;

import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class NumberOfTimesWithinRangeVerificationTest extends TestBase {

    private LinkedList mock;

    @Before
    public void setup() {
        mock = mock(LinkedList.class);
    }

    @Test
    public void shouldDetectTooLittleActualInvocations() throws Exception {
        mock.clear();
        mock.clear();

        verify(mock, withinRange(2, 3)).clear();
        try {
            verify(mock, withinRange(3, 4)).clear();
            fail();
        } catch (TooLittleActualInvocations e) {
            assertContains("Wanted 3 times", e.getMessage());
            assertContains("was 2", e.getMessage());
        }
    }

    @Test
    public void shouldDetectTooManyActualInvocations() throws Exception {
        mock.clear();
        mock.clear();

        verify(mock, withinRange(2, 3)).clear();
        try {
            verify(mock, withinRange(0, 1)).clear();
            fail();
        } catch (TooManyActualInvocations e) {
            assertContains("Wanted 1 time", e.getMessage());
            assertContains("was 2 times", e.getMessage());
        }
    }

    @Test
    public void shouldDetectActualInvocationsCountIsMoreThanZero() throws Exception {
        verify(mock, withinRange(0, 0)).clear();
        try {
            verify(mock, withinRange(1, 2)).clear();
            fail();
        } catch (WantedButNotInvoked e) {}
    }

    @Test
    public void shouldDetectActuallyCalledOnce() throws Exception {
        mock.clear();

        try {
            verify(mock, withinRange(0, 0)).clear();
            fail();
        } catch (NeverWantedButInvoked e) {
            assertContains("Never wanted here", e.getMessage());
        }
    }

    @Test
    public void shouldNotCountInStubbedInvocations() throws Exception {
        when(mock.add("test")).thenReturn(false);
        when(mock.add("test")).thenReturn(true);

        mock.add("test");
        mock.add("test");

        verify(mock, withinRange(2, 3)).add("test");
    }

    @Test
    public void shouldPassWhenMethodsActuallyNotCalled() throws Exception {
        verify(mock, withinRange(0, 0)).clear();
        verify(mock, withinRange(0, 0)).add("yes, I wasn't called");
    }

    @Test
    public void shouldPassWhenRangeIsOneElement() throws Exception {
        mock.clear();

        verify(mock, withinRange(1, 1)).clear();
    }

    @Test
    public void shouldPassWhenAtTheBottomOfRange() throws Exception {
        mock.clear();

        verify(mock, withinRange(1, 3)).clear();
    }

    @Test
    public void shouldPassWhenInTheMiddleOfRange() throws Exception {
        mock.clear();
        mock.clear();

        verify(mock, withinRange(1, 3)).clear();
    }

    @Test
    public void shouldPassWhenAtTheTopOfRange() throws Exception {
        mock.clear();
        mock.clear();
        mock.clear();

        verify(mock, withinRange(1, 3)).clear();
    }
}