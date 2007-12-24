/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.verification;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.*;
import static org.mockito.util.ExtraMatchers.*;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.RequiresValidState;
import org.mockito.exceptions.verification.TooLittleActualInvocationsError;
import org.mockito.exceptions.verification.TooManyActualInvocationsError;
import org.mockito.exceptions.verification.VerificationError;

@SuppressWarnings("unchecked")
public class ExactNumberOfTimesVerificationTest extends RequiresValidState {

    private LinkedList mock;

    @Before
    public void setup() {
        mock = mock(LinkedList.class);
    }

    @Test
    public void shouldDetectTooLittleActualInvocations() throws Exception {
        mock.clear();
        mock.clear();

        verify(mock, times(2)).clear();
        try {
            verify(mock, times(100)).clear();
            fail();
        } catch (TooLittleActualInvocationsError e) {
            assertThat(e, messageContains("Wanted 100 times but was 2"));
        }
    }

    @Test
    public void shouldDetectTooManyActualInvocations() throws Exception {
        mock.clear();
        mock.clear();

        verify(mock, times(2)).clear();
        try {
            verify(mock, times(1)).clear();
            fail();
        } catch (TooManyActualInvocationsError e) {
            assertThat(e, messageContains("Wanted 1 time but was 2"));
        }
    }

    @Test
    public void shouldDetectActualInvocationsCountIsMoreThanZero() throws Exception {
        verify(mock, times(0)).clear();
        try {
            verify(mock, times(15)).clear();
            fail();
        } catch (VerificationError e) {
            assertThat(e, messageContains("Wanted but not invoked"));
        }
    }

    @Test
    public void shouldDetectActuallyCalledOnce() throws Exception {
        mock.clear();

        try {
            verify(mock, times(0)).clear();
            fail();
        } catch (TooManyActualInvocationsError e) {
            assertThat(e, messageContains("Wanted 0 times but was 1"));
        }
    }

    @Test
    public void shouldPassWhenMethodsActuallyNotCalled() throws Exception {
        verify(mock, times(0)).clear();
        verify(mock, times(0)).add("yes, I wasn't called");
    }

    @Test
    public void shouldNotCountInStubbedInvocations() throws Exception {
        stub(mock.add("test")).andReturn(false);
        stub(mock.add("test")).andReturn(true);

        mock.add("test");
        mock.add("test");

        verify(mock, times(2)).add("test");
    }
}