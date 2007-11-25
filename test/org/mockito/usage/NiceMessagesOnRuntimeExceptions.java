/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.usage;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.exceptions.MockitoException;

public class NiceMessagesOnRuntimeExceptions {
    
    @Test
    public void shouldPrintThatRequiresArgumentsWhenVerifyingNoMoreInteractions() {
        try {
            verifyNoMoreInteractions();
            fail();
        }
        catch (MockitoException e) {
            String expected = 
                    "\n" +
                    "Method requires arguments." +
                    "\n" +
                    "Pass mocks that should be verified.";
            assertEquals(expected, e.getMessage());
        }
    }
    
    @Test
    public void shouldPrintThatRequiresArgumentsWhenVerifyingZeroInteractions() {
        try {
            verifyZeroInteractions();
            fail();
        }
        catch (MockitoException e) {
            String expected = 
                    "\n" +
                    "Method requires arguments." +
                    "\n" +
                    "Pass mocks that should be verified.";
            assertEquals(expected, e.getMessage());
        }
    }
}
