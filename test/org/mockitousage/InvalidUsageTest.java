/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import static org.mockito.Mockito.*;

import org.junit.*;
import org.mockito.Strictly;
import org.mockito.exceptions.*;
import org.mockito.exceptions.parents.MockitoException;
import org.mockito.internal.StateResetter;
import org.mockito.util.RequiresValidState;

@SuppressWarnings("unchecked")
public class InvalidUsageTest extends RequiresValidState {

    private IMethods mock;
    private IMethods mockTwo;

    @Before
    @After
    public void resetState() {
        StateResetter.reset();
        mock = mock(IMethods.class);
        mockTwo = mock(IMethods.class);
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
    public void shouldNotStrictlyVerifyUnfamilarMocks() {
        Strictly strictly = createStrictOrderVerifier(mock);
        strictly.verify(mockTwo).simpleMethod();
    }
    
    @Test(expected=MissingMethodInvocationException.class)
    public void shouldReportMissingMethodInvocationWhenStubbing() {
        stub("".toString()).andReturn("x");
    }
    
    @Test(expected=MockitoException.class)
    public void shouldNotAllowSettingInvalidCheckedException() throws Exception {
        stub(mock.simpleMethod()).andThrows(new Exception());
    }
    
    @Test(expected=MockitoException.class)
    public void shouldNotAllowSettingNullThrowable() throws Exception {
        stub(mock.simpleMethod()).andThrows(null);
    }    
}