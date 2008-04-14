/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.misuse;

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
import org.mockitousage.IMethods;

public class DescriptiveMessagesOnMisuseTest extends TestBase {
    
    @Mock private IMethods mock;

    @SuppressWarnings("all")
    @Ignore("just for tuning up the error messages")
    @Test
    public void tryDescriptiveMessagesOnMisuse() {
        verifyNoMoreInteractions();
        verifyNoMoreInteractions(null);
        verifyNoMoreInteractions("");
        verifyZeroInteractions();
        verifyZeroInteractions(null);
        verifyZeroInteractions("");

        inOrder();
        inOrder(null);
        inOrder("test");
        InOrder inOrder = inOrder(mock(List.class));
        inOrder.verify(mock).simpleMethod();

        verify(mock.differentMethod());
        verify(null);

        verify(mock).varargs("test", anyString());

        stub("x").toReturn("x");

        stub(mock.simpleMethod());
        stub(mock.differentMethod()).toReturn("");
    } 
    
    @Test(expected=NotAMockException.class)
    public void shouldScreamWhenWholeMethodPassedToVerify() {
        verify(mock.booleanReturningMethod());
    }   
    
    @Test(expected=NotAMockException.class)
    public void shouldScreamWhenWholeMethodPassedToVerifyNoMoreInteractions() {
        verifyNoMoreInteractions(mock.byteReturningMethod());
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
}