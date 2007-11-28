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
public class InvalidUsageExceptionsTest {
    
    @Test
    public void shouldRequireArgumentsWhenVerifyingNoMoreInteractions() {
        try {
            verifyNoMoreInteractions();
            fail();
        }
        catch (MockitoException e) {}
    }
    
    @Test
    public void shouldRequireArgumentsWhenVerifyingZeroInteractions() {
        try {
            verifyZeroInteractions();
            fail();
        }
        catch (MockitoException e) {}
    }
    
    @Test
    public void shouldNotCreateStrictlyWithoutMocks() {
        try {
            createStrictOrderVerifier();
            fail();
        } catch (MockitoException e) {}
    }
    
    @Test
    public void shouldNotStrictlyVerifyUnfamilarMocks() {
        List mockOne = mock(List.class);
        List mockTwo = mock(List.class);
        Strictly strictly = createStrictOrderVerifier(mockOne);
        try {
            strictly.verify(mockTwo).clear();
            fail();
        } catch (MockitoException e) {}
    }
}
