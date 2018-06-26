/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.verification.within;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.verification.TooLittleActualInvocations;
import org.mockito.exceptions.verification.TooManyActualInvocations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockitousage.IMethods;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.within;

public class AtLeastAndAtMostTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private IMethods mock;

    @Test
    public void atLeastAtMost_tooLowMinimum() {
        exception.expect(MockitoException.class);
        exception.expectMessage("The minimum number of invocations must be greater that 0!");

        verify(mock, within(100, MILLISECONDS).atLeast(0).andAtMost(5)).simpleMethod();
    }

    @Test
    public void atLeastAtMost_negativeMinimum() {
        exception.expect(MockitoException.class);
        exception.expectMessage("The minimum number of invocations must be greater that 0!");

        verify(mock, within(100, MILLISECONDS).atLeast(-1).andAtMost(5)).simpleMethod();
    }

    @Test
    public void atLeastAtMost_minimumEqualToMax() {
        exception.expect(MockitoException.class);
        exception.expectMessage("The minimum number of invocations must be greater than the maximum!");

        verify(mock, within(100, MILLISECONDS).atLeast(2).andAtMost(2)).simpleMethod();
    }

    @Test
    public void atLeastAtMost_tooLowMaximum() {
        exception.expect(MockitoException.class);
        exception.expectMessage("The maximum number of invocations must be greater than 1!");

        verify(mock, within(100, MILLISECONDS).atLeast(1).andAtMost(0)).simpleMethod();
    }

    @Test
    public void atLeastAtMost_minNumberOfInvocations() {
        mock.simpleMethod();
        verify(mock, within(100, MILLISECONDS).atLeast(1).andAtMost(2)).simpleMethod();
    }

    @Test
    public void atLeastAtMost_maxNumberOfInvocations() {
        mock.simpleMethod();
        mock.simpleMethod();
        verify(mock, within(100, MILLISECONDS).atLeast(1).andAtMost(2)).simpleMethod();
    }

    @Test
    public void atLeastAtMost_tooManyInvocations() {
        exception.expect(TooManyActualInvocations.class);
        exception.expectMessage("mock.simpleMethod()");
        exception.expectMessage("Wanted 2 times");
        exception.expectMessage("But was 3 times");


        mock.simpleMethod();
        mock.simpleMethod();
        mock.simpleMethod();
        verify(mock, within(100, MILLISECONDS).atLeast(1).andAtMost(2)).simpleMethod();
    }

    @Test
    public void atLeastAtMost_tooLittleInvocations() {
        exception.expect(TooLittleActualInvocations.class);
        exception.expectMessage("mock.simpleMethod()");
        exception.expectMessage("Wanted *at least* 1 time");
        exception.expectMessage("But was 0 times");

        verify(mock, within(100, MILLISECONDS).atLeast(1).andAtMost(2)).simpleMethod();
    }

}
