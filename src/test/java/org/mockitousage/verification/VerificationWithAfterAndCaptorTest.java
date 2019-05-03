/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.verification;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoRule;
import org.mockitousage.IMethods;
import org.mockitoutil.Stopwatch;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.after;
import static org.mockito.Mockito.verify;
import static org.mockito.junit.MockitoJUnit.rule;
import static org.mockitoutil.Stopwatch.createNotStarted;

public class VerificationWithAfterAndCaptorTest {

    @Rule public MockitoRule mockito = rule();

    @Mock private IMethods mock;

    @Captor private ArgumentCaptor<Character> captor;

    private Stopwatch watch = createNotStarted();

    /**
     * Test for issue #345.
     */
    @Test
    public void shouldReturnListOfArgumentsWithSameSizeAsGivenInAtMostVerification() {
        // given
        int n = 3;

        // when
        exerciseMockNTimes(n);

        watch.start();

        // then
        verify(mock, after(200).atMost(n)).oneArg((char) captor.capture());

        watch.assertElapsedTimeIsMoreThan(200, MILLISECONDS);
        assertThat(captor.getAllValues()).containsExactly('0', '1', '2');
    }

    @Test
    @Ignore("TODO review after #936")
    public void shouldReturnListOfArgumentsWithSameSizeAsGivenInTimesVerification() {
        // given
        int n = 3;

        // when
        exerciseMockNTimes(n);

        //Then
        verify(mock, after(200).times(n)).oneArg((char) captor.capture());
        assertEquals(n, captor.getAllValues().size());
        assertEquals('0', (char) captor.getAllValues().get(0));
        assertEquals('1', (char) captor.getAllValues().get(1));
        assertEquals('2', (char) captor.getAllValues().get(2));
    }

    @Test
    @Ignore("TODO review after #936")
    public void shouldReturnListOfArgumentsWithSameSizeAsGivenInAtLeastVerification() {
        // given
        int n = 3;

        // when
        exerciseMockNTimes(n);

        //Then
        verify(mock, after(200).atLeast(n)).oneArg((char) captor.capture());
        assertEquals(n, captor.getAllValues().size());
        assertEquals('0', (char) captor.getAllValues().get(0));
        assertEquals('1', (char) captor.getAllValues().get(1));
        assertEquals('2', (char) captor.getAllValues().get(2));
    }

    private void exerciseMockNTimes(int n) {
        for (int i = 0; i < n; i++) {
            mock.oneArg((char) ('0' + i));
        }
    }
}
