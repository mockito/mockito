/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.verification.within;

import static junit.framework.TestCase.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.verification.Within.untilNow;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.exceptions.verification.NeverWantedButInvoked;
import org.mockito.exceptions.verification.TooLittleActualInvocations;
import org.mockito.exceptions.verification.TooManyActualInvocations;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class TimesVerificationTest extends TestBase {

    private LinkedList<String> mock;

    @Before
    public void setup() {
        mock = mock(LinkedList.class);
    }

    @Test
    public void shouldDetectTooLittleActualInvocations() throws Exception {
        mock.clear();
        mock.clear();

        verify(mock, untilNow().times(2)).clear();
        try {
            verify(mock, untilNow().times(100)).clear();
            fail();
        } catch (TooLittleActualInvocations e) {
            assertThat(e)
                .hasMessageContaining("Wanted 100 times")
                .hasMessageContaining("was 2");
        }
    }

    @Test
    public void shouldDetectTooManyActualInvocations() throws Exception {
        mock.clear();
        mock.clear();

        verify(mock, untilNow().times(2)).clear();
        try {
            verify(mock, untilNow().times(1)).clear();
            fail();
        } catch (TooManyActualInvocations e) {
            assertThat(e)
                .hasMessageContaining("Wanted 1 time")
                .hasMessageContaining("was 2 times");
        }
    }

    @Test
    public void shouldDetectActualInvocationsCountIsMoreThanZero() throws Exception {
        verify(mock, untilNow().times(0)).clear();
        try {
            verify(mock, untilNow().times(15)).clear();
            fail();
        } catch (WantedButNotInvoked e) {}
    }

    @Test
    public void shouldDetectActuallyCalledOnce() throws Exception {
        mock.clear();

        try {
            verify(mock, untilNow().times(0)).clear();
            fail();
        } catch (NeverWantedButInvoked e) {
            assertThat(e).hasMessageContaining("Never wanted here");
        }
    }

    @Test
    public void shouldPassWhenMethodsActuallyNotCalled()   {
        verify(mock, untilNow().times(0)).clear();
        verify(mock, untilNow().times(0)).add("yes, I wasn't called");
    }

    @Test
    public void shouldNotCountInStubbedInvocations()   {
        when(mock.add("test")).thenReturn(false);
        when(mock.add("test")).thenReturn(true);

        mock.add("test");
        mock.add("test");

        verify(mock, untilNow().times(2)).add("test");
    }
    
    @Test
    public void shouldAllowVerifyingInteractionNeverHappened()   {
        mock.add("one");

        verify(mock, untilNow().never()).add("two");
        verify(mock, untilNow().never()).clear();
        
        try {
            verify(mock, untilNow().never()).add("one");
            fail();
        } catch (NeverWantedButInvoked e) {}
    }
    
}