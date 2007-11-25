/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.usage;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Test;
import org.mockito.exceptions.MockitoException;

public class NiceMessagesOnRuntimeExceptions {
    
    @Test
    public void shouldPrintThatRequiresArguments() {
        try {
            verifyNoMoreInteractions();
            fail();
        }
        catch (MockitoException e) {
            String expected = 
                    "\n" +
                    "verifyNoMoreInteractions() requires arguments." +
                    "\n" +
                    "Pass mocks that should be verified.";
            assertEquals(expected, e.getMessage());
        }
    }
}
