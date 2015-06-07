/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.misuse;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.MissingMethodInvocationException;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class InvalidUsageTest extends TestBase {

    @Mock private IMethods mock;
    @Mock private IMethods mockTwo;

    @After
    public void resetState() {
        super.resetState();
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
        final InOrder inOrder = inOrder(mock);
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
        when(mock.simpleMethod()).thenThrow(new Throwable[] {null});
    }    

    @SuppressWarnings("all")
    @Test(expected=MockitoException.class)
    public void shouldNotAllowSettingNullThrowableVararg() throws Exception {
        when(mock.simpleMethod()).thenThrow((Throwable) null);
    }    

    @Test(expected=MockitoException.class)
    public void shouldNotAllowSettingNullConsecutiveThrowable() throws Exception {
        when(mock.simpleMethod()).thenThrow(new RuntimeException(), null);
    }    
    
    final class FinalClass {}
    
    @Test(expected=MockitoException.class)
    public void shouldNotAllowMockingFinalClasses() throws Exception {
        mock(FinalClass.class); 
    }
    
    @Test(expected=MockitoException.class)
    public void shouldNotAllowMockingPrimitves() throws Exception {
        mock(Integer.TYPE); 
    }
    
    interface ObjectLikeInterface {
        boolean equals(final Object o);
        String toString();
        int hashCode();
    }
    
    @Test
    public void shouldNotMockObjectMethodsOnInterface() throws Exception {
        final ObjectLikeInterface inter = mock(ObjectLikeInterface.class);
        
        inter.equals(null);
        inter.toString();
        inter.hashCode();
        
        verifyZeroInteractions(inter);
    }
    
    public void shouldNotMockObjectMethodsOnClass() throws Exception {
        final Object clazz = mock(ObjectLikeInterface.class);
        
        clazz.equals(null);
        clazz.toString();
        clazz.hashCode();
        
        verifyZeroInteractions(clazz);
    }
}
