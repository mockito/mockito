/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.misuse;

import static org.mockito.Mockito.*;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.exceptions.misusing.NullInsteadOfMockException;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class DescriptiveMessagesOnMisuseTest extends TestBase {
    
    @Mock private IMethods mock;

    @SuppressWarnings("all")
    @Ignore("just for tuning up the error messages")
    @Test
    public void tryDescriptiveMessagesOnMisuse() {
        doReturn("foo");
        doReturn("bar");
        
//        verifyNoMoreInteractions();
//        verifyNoMoreInteractions(null);
//        verifyNoMoreInteractions("");
//        verifyZeroInteractions();
//        verifyZeroInteractions(null);
//        verifyZeroInteractions("");
//
//        inOrder();
//        inOrder(null);
//        inOrder("test");
//        InOrder inOrder = inOrder(mock(List.class));
//        inOrder.verify(mock).simpleMethod();

//        verify(null);
//        verify(mock.booleanReturningMethod());

//        verify(mock).varargs("test", anyString());

//        when("x").thenReturn("x");

//        when(mock.simpleMethod());
//        when(mock.differentMethod()).thenReturn("");
    } 
    
    @Test(expected=NotAMockException.class)
    public void shouldScreamWhenWholeMethodPassedToVerify() {
        verify(mock.booleanReturningMethod());
    }   
    
    @Test(expected=NotAMockException.class)
    public void shouldScreamWhenWholeMethodPassedToVerifyNoMoreInteractions() {
        verifyNoMoreInteractions(mock.byteReturningMethod());
    }
    
    @Test(expected=NotAMockException.class)
    public void shouldScreamWhenInOrderCreatedWithDodgyMock() {
        inOrder("not a mock");
    }
    
    @Test(expected=NullInsteadOfMockException.class)
    public void shouldScreamWhenInOrderCreatedWithNulls() {
        inOrder(mock, null);
    }
    
    @Test(expected=NullInsteadOfMockException.class)
    public void shouldScreamNullPassedToVerify() {
        verify(null);
    }  
    
    @Test(expected=NullInsteadOfMockException.class)
    public void shouldScreamWhenNotMockPassedToVerifyNoMoreInteractions() {
        verifyNoMoreInteractions(null, "blah");
    } 
    
    @SuppressWarnings("all")
    @Test(expected=MockitoException.class)
    public void shouldScreamWhenNullPassedToVerifyNoMoreInteractions() {
        verifyNoMoreInteractions(null);
    }
}