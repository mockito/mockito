/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.verification.within;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.within;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockitousage.IMethods;

public class AtLeastAndAtMostTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private IMethods mock;

    @Test
    public void atLeastAtMost_tooLowMinimum() throws Exception {
        exception.expect(MockitoException.class);
        exception.expectMessage("The minimum number of invocations must be greater that 0!");

        verify(mock, within(100, MILLISECONDS).atLeast(0).andAtMost(5)).simpleMethod();
    }

    @Test
    public void atLeastAtMost_negativeMinimum() throws Exception {
        exception.expect(MockitoException.class);
        exception.expectMessage("The minimum number of invocations must be greater that 0!");

        verify(mock, within(100, MILLISECONDS).atLeast(-1).andAtMost(5)).simpleMethod();
    }

    @Test
    public void atLeastAtMost_minimumEqualToMax() throws Exception {
        exception.expect(MockitoException.class);
        exception.expectMessage("The minimum number of invocations must be greater than the maximum!");

        verify(mock, within(100, MILLISECONDS).atLeast(2).andAtMost(2)).simpleMethod();
    }

    @Test
    public void atLeastAtMost_tooLowMaximum() throws Exception {
        exception.expect(MockitoException.class);
        exception.expectMessage("The maximum number of invocations must be greater than 1!");

        verify(mock, within(100, MILLISECONDS).atLeast(1).andAtMost(0)).simpleMethod();
    }

}
