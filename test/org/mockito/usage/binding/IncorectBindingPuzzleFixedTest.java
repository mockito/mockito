/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.usage.binding;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Strictly;
import org.mockito.exceptions.VerificationError;

public class IncorectBindingPuzzleFixedTest {

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
                "Wanted: Sub.say(class java.lang.String)" +
                "\n" +
                "Actual: Sub.say(class java.lang.Object)";
            
            assertEquals(expected, error.getMessage());
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
        } catch (VerificationError error) {
            String expected = 
                "\n" +
                "Strict order verification failed" +
                "\n" +
                "Wanted: Sub.say(class java.lang.String)" +
                "\n" +
                "Actual: Sub.say(class java.lang.Object)";
            
            assertEquals(expected, error.getMessage());
        }
    }
}