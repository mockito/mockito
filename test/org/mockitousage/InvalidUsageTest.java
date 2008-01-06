/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import static org.mockito.Mockito.strictly;
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
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.MissingMethodInvocationException;

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
        strictly();
    }
    
    @Test(expected=MockitoException.class)
    public void shouldNotStrictlyVerifyUnfamilarMocks() {
        Strictly strictly = strictly(mock);
        strictly.verify(mockTwo).simpleMethod();
    }
    
    @Test(expected=MissingMethodInvocationException.class)
    public void shouldReportMissingMethodInvocationWhenStubbing() {
        stub(mock.simpleMethod()).toReturn("this stubbing is required to make sure Stubbable is pulled");
        stub("".toString()).toReturn("x");
    }
    
    @Test(expected=MockitoException.class)
    public void shouldNotAllowSettingInvalidCheckedException() throws Exception {
        stub(mock.simpleMethod()).toThrow(new Exception());
    }
    
    @Test(expected=MockitoException.class)
    public void shouldNotAllowSettingNullThrowable() throws Exception {
        stub(mock.simpleMethod()).toThrow(null);
    }    
    
    final class FinalClass {}
    
    @Test(expected=MockitoException.class)
    public void shouldNotAllowMockingFinalClasses() throws Exception {
        mock(FinalClass.class); 
    }
    
    //TODO what if interface has equals() method to stub? 
}