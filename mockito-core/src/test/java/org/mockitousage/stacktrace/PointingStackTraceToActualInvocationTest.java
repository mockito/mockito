/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.stacktrace;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.exceptions.verification.NeverWantedButInvoked;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

// This is required to make sure stack trace is well filtered when runner is ON
@RunWith(MockitoJUnitRunner.class)
public class PointingStackTraceToActualInvocationTest extends TestBase {

    @Mock private IMethods mock;
    @Mock private IMethods mockTwo;

    @Before
    public void setup() {
        first();
        second();
        third();
        fourth();
    }

    private void first() {
        mock.simpleMethod(1);
    }

    private void second() {
        mockTwo.simpleMethod(2);
    }

    private void third() {
        mock.simpleMethod(3);
    }

    private void fourth() {
        mockTwo.simpleMethod(4);
    }

    @Test
    public void shouldPointToTooManyInvocationsChunkOnError() {
        try {
            verify(mock, times(0)).simpleMethod(1);
            fail();
        } catch (NeverWantedButInvoked e) {
            assertThat(e).hasMessageContaining("first(");
        }
    }

    @Test
    public void shouldNotPointStackTracesToRunnersCode() {
        try {
            verify(mock, times(0)).simpleMethod(1);
            fail();
        } catch (NeverWantedButInvoked e) {
            assertThat(e.getMessage()).doesNotContain(".runners.");
        }
    }
}
