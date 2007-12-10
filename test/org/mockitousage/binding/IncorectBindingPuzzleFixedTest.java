/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.binding;

import static org.junit.Assert.*;
import static org.mockito.util.ExtraMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.*;
import org.mockito.exceptions.verification.VerificationError;

public class IncorectBindingPuzzleFixedTest extends RequiresValidState {

    private Super mock;
    
    private void setMockWithDowncast(Super mock) {
        this.mock = mock;
    }

    private interface Super {
        public void say(Object message);
    }

    private interface Sub extends Super {
        public void say(String message);
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
        } catch (VerificationError error) {
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
    
    @Test
    public void shouldUseArgumentTypeWhenOverloadingPuzzleDetectedByStrictly() throws Exception {
        Sub sub = mock(Sub.class);
        setMockWithDowncast(sub);
        say("Hello");
        Strictly strictly = createStrictOrderVerifier(mock);
        try {
            strictly.verify(sub).say("Hello");
            fail();
        } catch (VerificationError e) {
            assertThat(e, messageContains("Sub.say(class java.lang.String)"));
            assertThat(e, causeMessageContains("Sub.say(class java.lang.Object)"));
        }
    }
}