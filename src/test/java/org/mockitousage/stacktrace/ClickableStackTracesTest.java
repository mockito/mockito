/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.stacktrace;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static org.junit.Assert.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ClickableStackTracesTest extends TestBase {

    @Mock private IMethods mock;

    private void callMethodOnMock(String param) {
        mock.simpleMethod(param);
    }

    private void verifyTheMock(int times, String param) {
        verify(mock, times(times)).simpleMethod(param);
    }

    @Test
    public void shouldShowActualAndExpectedWhenArgumentsAreDifferent() {
        callMethodOnMock("foo");
        try {
            verifyTheMock(1, "not foo");
            fail();
        } catch (ArgumentsAreDifferent e) {
            assertThat(e).hasMessageContaining("callMethodOnMock(").hasMessageContaining("verifyTheMock(");
        }
    }
}
