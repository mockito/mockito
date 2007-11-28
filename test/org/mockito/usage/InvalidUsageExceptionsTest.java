/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.usage;

import static org.mockito.Mockito.*;

import java.util.LinkedList;

import org.junit.*;
import org.mockito.Strictly;
import org.mockito.exceptions.MockitoException;

@SuppressWarnings("unchecked")
public class InvalidUsageExceptionsTest {
    
    private LinkedList mock;
    private LinkedList mockTwo;

    @Before public void setup() {
        mock = mock(LinkedList.class);
        mockTwo = mock(LinkedList.class);
    }
    
    @Test(expected=MockitoException.class)
    public void shouldRequireArgumentsWhenVerifyingNoMoreInteractions() {
        verifyNoMoreInteractions();
    }
    
    @Test(expected=MockitoException.class)
    public void shouldRequireArgumentsWhenVerifyingZeroInteractions() {
        verifyZeroInteractions();
    }
    
    @Test(expected=MockitoException.class)
    public void shouldNotCreateStrictlyWithoutMocks() {
        createStrictOrderVerifier();
    }
    
    @Test(expected=MockitoException.class)
    public void shouldNotVerifyStrictlyUnfamilarMocks() {
        Strictly strictly = createStrictOrderVerifier(mock);
        strictly.verify(mockTwo).clear();
    }
    
    @Test(expected=MockitoException.class)
    public void shouldNotAllowSettingInvalidCheckedException() throws Exception {
        stub(mock.add("monkey island")).andThrows(new Exception());
    }
    
    @Test(expected=MockitoException.class)
    public void shouldNotAllowSettingNullThrowable() throws Exception {
        stub(mock.add("monkey island")).andThrows(null);
    }
}