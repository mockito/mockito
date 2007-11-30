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

    private BaseInteface mock;

    private class BaseMessage {}
    
    private class Message extends BaseMessage {}

    private interface BaseInteface {
        public void print(BaseMessage message);
    }

    private interface DerivedInterface extends BaseInteface {
        public void print(Message message);
    }

    private void print(BaseMessage message) {
        mock.print(message);
    }

    @Test
    public void shouldUseArgumentTypeWhenOverloadingPuzzleDetected() throws Exception {
        DerivedInterface derivedMock = mock(DerivedInterface.class);
        mock = derivedMock;
        Message message = new Message();
        print(message);
        try {
            verify(derivedMock).print(message);
            fail();
        } catch (VerificationError error) {
            String expected = 
                "\n" +
        		"Invocation differs from actual" +
        		"\n" +
        		"Wanted: DerivedInterface.print(class org.mockito.usage.binding.IncorectBindingPuzzleFixedTest$Message)" +
        		"\n" +
        		"Actual: DerivedInterface.print(class org.mockito.usage.binding.IncorectBindingPuzzleFixedTest$BaseMessage)";
            
            assertEquals(expected, error.getMessage());
        }
    }
    
    @Test
    public void shouldUseArgumentTypeWhenOverloadingPuzzleDetectedByStrictly() throws Exception {
        DerivedInterface derivedMock = mock(DerivedInterface.class);
        mock = derivedMock;
        Message message = new Message();
        print(message);
        Strictly strictly = createStrictOrderVerifier(mock);
        try {
            strictly.verify(derivedMock).print(message);
            fail();
        } catch (VerificationError error) {
            String expected = 
                "\n" +
                "Strict order verification failed" +
                "\n" +
                "Wanted: DerivedInterface.print(class org.mockito.usage.binding.IncorectBindingPuzzleFixedTest$Message)" +
                "\n" +
                "Actual: DerivedInterface.print(class org.mockito.usage.binding.IncorectBindingPuzzleFixedTest$BaseMessage)";
            
            assertEquals(expected, error.getMessage());
        }
    }
}