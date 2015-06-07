/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.puzzlers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockitoutil.TestBase;

public class OverloadingPuzzleTest extends TestBase {

    private Super mock;

    private void setMockWithDowncast(final Super mock) {
        this.mock = mock;
    }

    private interface Super {
        void say(final Object message);
    }

    private interface Sub extends Super {
        void say(final String message);
    }

    private void say(final Object message) {
        mock.say(message);
    }

    @Test
    public void shouldUseArgumentTypeWhenOverloadingPuzzleDetected() throws Exception {
        final Sub sub = mock(Sub.class);
        setMockWithDowncast(sub);
        say("Hello");
        try {
            verify(sub).say("Hello");
            fail();
        } catch (final WantedButNotInvoked e) {}
    }
}