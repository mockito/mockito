/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.usage;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Test;
import org.mockito.Strictly;
import org.mockito.exceptions.MockitoException;

@SuppressWarnings("unchecked")
public class NiceMessagesOnInvalidUsageTest {
    
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
    
    @Test
    public void shouldPrintThatStrictlyCannotBeCreatedWithoutMocks() {
        try {
            createStrictOrderVerifier();
            fail();
        } catch (MockitoException e) {
            String expected =  
                "\n" +
                "Method requires arguments." +
                "\n" +
                "Pass mocks that require strict order verification.";
            assertEquals(expected, e.getMessage());
        }
    }
    
    @Test
    public void shouldPrintThatStrictlyCannotVerifyUnfamilarMocks() {
        List mockOne = mock(List.class);
        List mockTwo = mock(List.class);
        Strictly strictly = createStrictOrderVerifier(mockOne);
        try {
            strictly.verify(mockTwo).clear();
            fail();
        } catch (MockitoException e) {}
    }
}
