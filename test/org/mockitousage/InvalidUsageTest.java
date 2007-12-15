/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import static org.mockito.Mockito.createStrictOrderVerifier;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.StateResetter;
import org.mockito.Strictly;
import org.mockito.exceptions.misusing.MissingMethodInvocationException;
import org.mockito.exceptions.parents.MockitoException;

@SuppressWarnings("unchecked")
public class InvalidUsageTest extends RequiresValidState {

    private IMethods mock;
    private IMethods mockTwo;

    @Before
    public void setup() {
        mock = mock(IMethods.class);
        mockTwo = mock(IMethods.class);
    }
    
    @After
    public void resetState() {
        StateResetter.reset();
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
        stub(mock.simpleMethod()).andReturn("this stubbing is required to make sure Stubable is pulled");
        stub("".toString()).andReturn("x");
    }
    
    @Test(expected=MockitoException.class)
    public void shouldNotAllowSettingInvalidCheckedException() throws Exception {
        stub(mock.simpleMethod()).andThrow(new Exception());
    }
    
    @Test(expected=MockitoException.class)
    public void shouldNotAllowSettingNullThrowable() throws Exception {
        stub(mock.simpleMethod()).andThrow(null);
    }    
}