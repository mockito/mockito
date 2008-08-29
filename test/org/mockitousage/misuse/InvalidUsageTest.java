/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.misuse;

import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.StateMaster;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.MissingMethodInvocationException;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class InvalidUsageTest extends TestBase {

    private IMethods mock;
    private IMethods mockTwo;

    @Before
    public void setup() {
        mock = mock(IMethods.class);
        mockTwo = mock(IMethods.class);
    }
    
    @After
    public void resetState() {
        StateMaster.reset();
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
    public void shouldNotCreateInOrderObjectWithoutMocks() {
        inOrder();
    }
    
    @Test(expected=MockitoException.class)
    public void shouldNotAllowVerifyingInOrderUnfamilarMocks() {
        InOrder inOrder = inOrder(mock);
        inOrder.verify(mockTwo).simpleMethod();
    }
    
    @Test(expected=MissingMethodInvocationException.class)
    public void shouldReportMissingMethodInvocationWhenStubbing() {
        when(mock.simpleMethod()).thenReturn("this stubbing is required to make sure Stubbable is pulled");
        when("".toString()).thenReturn("x");
    }
    
    @Test(expected=MockitoException.class)
    public void shouldNotAllowSettingInvalidCheckedException() throws Exception {
        when(mock.simpleMethod()).thenThrow(new Exception());
    }
    
    @Test(expected=MockitoException.class)
    public void shouldNotAllowSettingNullThrowable() throws Exception {
        when(mock.simpleMethod()).thenThrow(null);
    }    
    
    final class FinalClass {}
    
    @Test(expected=MockitoException.class)
    public void shouldNotAllowMockingFinalClasses() throws Exception {
        mock(FinalClass.class); 
    }
    
    interface ObjectLikeInterface {
        boolean equals(Object o);
        String toString();
        int hashCode();
    }
    
    @Test
    public void shouldNotMockObjectMethodsOnInterface() throws Exception {
        ObjectLikeInterface inter = mock(ObjectLikeInterface.class);
        
        inter.equals(null);
        inter.toString();
        inter.hashCode();
        
        verifyZeroInteractions(inter);
    }
    
    public void shouldNotMockObjectMethodsOnClass() throws Exception {
        Object clazz = mock(ObjectLikeInterface.class);
        
        clazz.equals(null);
        clazz.toString();
        clazz.hashCode();
        
        verifyZeroInteractions(clazz);
    }
}