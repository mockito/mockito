/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.stacktrace;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class ClickableStackTracesTest extends TestBase {
    
    private IMethods mock;

    @Before
    public void setup() {
        mock = Mockito.mock(IMethods.class);
    }

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
            assertThat(e, messageContains("callMethodOnMock("));
            assertThat(e, messageContains("verifyTheMock("));
        }
    }
}