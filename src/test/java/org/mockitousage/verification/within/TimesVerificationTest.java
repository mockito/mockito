/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.verification.within;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static junit.framework.TestCase.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.within;
import static org.mockito.verification.Within.untilNow;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.exceptions.verification.NeverWantedButInvoked;
import org.mockito.exceptions.verification.TooLittleActualInvocations;
import org.mockito.exceptions.verification.TooManyActualInvocations;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;


public class TimesVerificationTest extends TestBase {

    private IMethods mock;

    @Before
    public void setup() {
        mock = mock(IMethods.class);
    }

    @Test
    public void shouldDetectTooLittleActualInvocations() throws Exception {
        mock.simpleMethod();
        mock.simpleMethod();

        verify(mock, untilNow().times(2)).simpleMethod();
        try {
            verify(mock, untilNow().times(100)).simpleMethod();
            fail();
        } catch (TooLittleActualInvocations e) {
            assertThat(e)
                .hasMessageContaining("Wanted 100 times")
                .hasMessageContaining("was 2");
        }
    }

    @Test
    public void shouldDetectTooManyActualInvocations() throws Exception {
        mock.simpleMethod();
        mock.simpleMethod();

        verify(mock, untilNow().times(2)).simpleMethod();
        try {
            verify(mock, untilNow().times(1)).simpleMethod();
            fail();
        } catch (TooManyActualInvocations e) {
            assertThat(e)
                .hasMessageContaining("Wanted 1 time")
                .hasMessageContaining("was 2 times");
        }
    }

    @Test
    public void shouldDetectActualInvocationsCountIsMoreThanZero() throws Exception {
        verify(mock, untilNow().times(0)).simpleMethod();
        try {
            verify(mock, untilNow().times(15)).simpleMethod();
            fail();
        } catch (WantedButNotInvoked e) {}
    }

    @Test
    public void shouldDetectActuallyCalledOnce() throws Exception {
        mock.simpleMethod();

        try {
            verify(mock, untilNow().times(0)).simpleMethod();
            fail();
        } catch (NeverWantedButInvoked e) {
            assertThat(e).hasMessageContaining("Never wanted here");
        }
    }

    @Test
    public void shouldPassWhenMethodsActuallyNotCalled()   {
        verify(mock, untilNow().times(0)).simpleMethod();
        verify(mock, untilNow().times(0)).simpleMethod("yes, I wasn't called");
    }

    @Test
    public void shouldNotCountInStubbedInvocations()   {
        when(mock.booleanReturningMethod()).thenReturn(false);
        when(mock.booleanReturningMethod()).thenReturn(true);

        mock.booleanReturningMethod();
        mock.booleanReturningMethod();

        verify(mock, untilNow().times(2)).booleanReturningMethod();
    }
    
    @Test
    public void shouldAllowVerifyingInteractionNeverHappened()   {
        mock.simpleMethod("one");

        verify(mock, untilNow().never()).simpleMethod("two");
        verify(mock, untilNow().never()).simpleMethod();
        
        try {
            verify(mock, untilNow().never()).simpleMethod("one");
            fail();
        } catch (NeverWantedButInvoked e) {}
    }
    
    @Test(timeout=3000)
    public void shouldCapture10ArgumentsAsync() throws Exception {
        
        ExecutorService exec = Executors.newSingleThreadExecutor();
        exec.execute(new Runnable() {
            @Override
            public void run() {
                for (int j = 0; j < 10; j++) {
                    mock.intArgumentMethod(0);
                }
            }
        });
        
        exec.shutdown();
        exec.awaitTermination(1, DAYS);
        
        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
        
        verify(mock,within(1,MILLISECONDS).times(10)).intArgumentMethod(argumentCaptor.capture());
        
        List<Integer> captured = argumentCaptor.getAllValues();
        assertEquals(10, captured.size());
    }
    
}
