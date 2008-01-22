/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.binding;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.util.ExtraMatchers.*;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.RequiresValidState;
import org.mockito.exceptions.verification.InvocationDiffersFromActual;

public class IncorectBindingPuzzleFixedTest extends RequiresValidState {

    private Super mock;

    private void setMockWithDowncast(Super mock) {
        this.mock = mock;
    }

    private interface Super {
        void say(Object message);
    }

    private interface Sub extends Super {
        void say(String message);
    }

    private void say(Object message) {
        mock.say(message);
    }

    @Test
    public void shouldUseArgumentTypeWhenOverloadingPuzzleDetected() throws Exception {
        Sub sub = mock(Sub.class);
        setMockWithDowncast(sub);
        say("Hello");
        try {
            verify(sub).say("Hello");
            fail();
        } catch (InvocationDiffersFromActual error) {
            String expected =
                "\n" +
                "Invocation differs from actual" +
                "\n" +
                "Wanted invocation:" +
                "\n" +
                "Sub.say(class java.lang.String)";

            assertEquals(expected, error.getMessage());

            String expectedCause =
                "\n" +
                "Actual invocation:" +
                "\n" +
                "Sub.say(class java.lang.Object)";
            assertEquals(expectedCause, error.getCause().getMessage());
        }
    }

    @Ignore("that's a very edge case, don't care for it now")
    @Test
    public void shouldUseArgumentTypeWhenOverloadingPuzzleDetectedByVerificationInOrder() throws Exception {
        Sub sub = mock(Sub.class);
        setMockWithDowncast(sub);
        say("Hello");
        InOrder inOrder = inOrder(mock);
        inOrder.verify(sub).say("Hello");
    }

    @Test
    public void shouldUseArgumentTypeWhenMatcherUsed() throws Exception {
        Sub sub = mock(Sub.class);
        setMockWithDowncast(sub);
        say("Hello world");
        try {
            verify(sub).say(contains("world"));
            fail();
        } catch (InvocationDiffersFromActual e) {
            assertThat(e, messageContains("Sub.say(class java.lang.String)"));
            assertThat(e, causeMessageContains("Sub.say(class java.lang.Object)"));
        }
    }
}