/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.TestBase;
import org.mockito.MockitoAnnotations.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.NotAMockException;

public class DescriptiveMessagesOnMisuseTest extends TestBase {
    
    @Mock private IMethods mock;

    @Test(expected=NotAMockException.class)
    public void shouldScreamWhenWholeMethodPassedToVerify() {
        verify(mock.booleanReturningMethod(2));
    }   
    
    @Test(expected=NotAMockException.class)
    public void shouldScreamWhenWholeMethodPassedToVerifyNoMoreInteractions() {
        verifyNoMoreInteractions(mock.byteReturningMethod(1));
    }  
    
    @Test(expected=MockitoException.class)
    public void shouldScreamNullPassedToVerify() {
        verify(null);
    }  
    
    @Test(expected=NotAMockException.class)
    public void shouldScreamWhenNotMockPassedToVerifyNoMoreInteractions() {
        verifyNoMoreInteractions(null, "blah");
    } 
    
    @SuppressWarnings("all")
    @Test(expected=MockitoException.class)
    public void shouldScreamWhenNullPassedToVerifyNoMoreInteractions() {
        verifyNoMoreInteractions(null);
    }
    
    @SuppressWarnings("all")
    @Ignore
    @Test
    public void shouldExceptionMessageProvideUsefulInfo() {
        verifyNoMoreInteractions(null);
        verifyNoMoreInteractions("");
        verifyZeroInteractions(null);
        verifyZeroInteractions("");
        
        inOrder(null);
        inOrder("test");
        InOrder inOrder = inOrder(mock(List.class));
        inOrder.verify(mock).simpleMethod();
        
        verify(mock.differentMethod());
        verify(null);
        
        verify(mock).varargs("test", anyString());
        //TODO replace 'Not a mock' with arg that ... or something ???
    } 
}